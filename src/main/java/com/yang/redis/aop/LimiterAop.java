package com.yang.redis.aop;

import com.yang.redis.RedisUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 使用环绕通知方式, 设置某ip单位时间内多次请求
 * @author yangyuyang
 */
@Aspect
@Component
public class LimiterAop {

    private static final String LIMITING_KEY = "limiting:%s:%s";
    private static final String LIMITING_BEGINTIME = "beginTime";
    private static final String LIMITING_EXFREQUENCY = "exFrequency";

    @Autowired
    private RedisUtil redisUtil;

    @Pointcut("@annotation(limiter)")
    public void pointcut(Limiter limiter) {
    }

    @Around("pointcut(limiter)")
    public Object around(ProceedingJoinPoint pjp, Limiter limiter) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getRemoteAddr();
        String methodName = pjp.getSignature().toLongString();

        //获取方法的访问周期和频率
        long cycle = limiter.cycle();
        int frequency = limiter.frequency();
        long currentTime = System.currentTimeMillis();
        String key = String.format(LIMITING_KEY, ip, methodName);

        //获取redis中周期内第一次访问方法的时间和执行的次数
        Long beginTimeLong = (Long) redisUtil.hget(key, LIMITING_BEGINTIME);
        Integer exFrequencyLong = (Integer) redisUtil.hget(key, LIMITING_EXFREQUENCY);

        long beginTime = beginTimeLong == null ? 0L : beginTimeLong;
        int exFrequency = exFrequencyLong == null ? 0 : exFrequencyLong;

        //如果当前时间减去周期内第一次访问方法的时间大于周期时间，则正常访问
        //并将周期内第一次访问方法的时间和执行次数初始化
        if(currentTime - beginTime > cycle) {
            redisUtil.hset(key, LIMITING_BEGINTIME, currentTime, limiter.expireTime());
            redisUtil.hset(key, LIMITING_EXFREQUENCY, 1, limiter.expireTime());
            return pjp.proceed();
        } else {
            //如果在周期时间内，执行次数小于频率，则正常访问
            //并将执行次数加一
            if(exFrequency < frequency) {
                redisUtil.hset(key, LIMITING_EXFREQUENCY, exFrequency + 1, limiter.expireTime());
                return pjp.proceed();
            } else {
                throw new RuntimeException(limiter.message());
            }
        }
    }

}
