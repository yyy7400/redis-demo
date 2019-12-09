package com.yang.redis;

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
        get();

    }

    public static void get(){

        app.redisUtil.set("123", "kkk");

        while (true){
            long t1 = System.currentTimeMillis();
            for(int i = 0; i < 10000; i++) {
                //map.get("123");
                app.redisUtil.get("123");
                //redisUtil.set("1", "jjj");
            }
            long t2 = System.currentTimeMillis();
            System.out.println(t2 -t1);
        }
    }

    /**
     * http重定向到https
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
