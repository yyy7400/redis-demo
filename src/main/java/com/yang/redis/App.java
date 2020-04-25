package com.yang.redis;

import com.google.common.collect.Interner;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.yang.redis.utils.BloomFilterHelper;
import com.yang.redis.utils.RedisUtil;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author yangyuyang
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class App {

    @Resource
    private RedisUtil redisUtil;
    //	//rabbit mq
    public static App app;

    @Value("${server.port}")
    private Integer httpsPort;
    @Value("${server.http.port}")
    private Integer httpPort;

    @PostConstruct
    public void init() {
        app = this;
        app.redisUtil = this.redisUtil;
        app.httpsPort = this.httpsPort;
        app.httpPort = this.httpPort;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        //get();
        bloomFilter();
    }

    private static int total = 1000;
    private static BloomFilter<Integer> bf = BloomFilter.create(Funnels.integerFunnel(), total);
    private static BloomFilterHelper<Integer> bloomFilterHelper = new BloomFilterHelper<>(Funnels.integerFunnel(),total, 0.03 );

    public static void bloomFilter() {
        // 初始化1000000条数据到过滤器中
        for (int i = 0; i < total; i++) {
            //bf.put(i);
            app.redisUtil.addByBloomFilter(bloomFilterHelper, String.valueOf(i), i);
        }

        // 匹配已在过滤器中的值，是否有匹配不上的
        for (int i = 0; i < total; i++) {
            //if (!bf.mightContain(i)) {
            if(!app.redisUtil.includeByBloomFilter(bloomFilterHelper, String.valueOf(i), i)) {
                System.out.println("有坏人逃脱了~~~");
            }
        }

        // 匹配不在过滤器中的10000个值，有多少匹配出来
        int count = 0;
        for (int i = total; i < total + 10000; i++) {
            //if (bf.mightContain(i)) {
           if(app.redisUtil.includeByBloomFilter(bloomFilterHelper, String.valueOf(i), i)) {
                count++;
            }
        }
        System.out.println("误伤的数量：" + count);



    }

    public static void get() {

        app.redisUtil.set("123", "kkk");

        while (true) {
            long t1 = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                //map.get("123");
                app.redisUtil.get("123");
                //redisUtil.set("1", "jjj");
            }
            long t2 = System.currentTimeMillis();
            System.out.println(t2 - t1);
        }

    }

    /**
     * http重定向到https
     *
     * @return
     */
    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint constraint = new SecurityConstraint();
                constraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                constraint.addCollection(collection);
                context.addConstraint(constraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(httpConnector());
        return tomcat;
    }


    @Bean
    public Connector httpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        //Connector监听的http的端口号
        connector.setPort(app.httpPort);
        connector.setSecure(false);
        //监听到http的端口号后转向到的https的端口号
        connector.setRedirectPort(app.httpsPort);
        return connector;
    }

}
