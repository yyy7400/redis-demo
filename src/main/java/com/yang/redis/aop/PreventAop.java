package com.yang.redis.aop;

import com.alibaba.fastjson.JSON;
import com.yang.redis.RedisUtil;
import common.BusinessException;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Base64;

/**
 * 防刷切面实现类，使用前置通知方式
 *
 * @author yangyuyang
 * @date 2019-12-06
 */
@Slf4j
@Component
@Aspect
public class PreventAop {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.yang.redis.aop.Prevent)")
    public void pointcut() {
    }

    /**
     * 处理前
     *
     * @param joinPoint
     * @throws Exception
     */
    @Before("pointcut()")
    public void joinPoint(JoinPoint joinPoint) throws Exception {

        String requestStr = JSON.toJSONString(joinPoint.getArgs()[0]);
        if (StringUtils.isEmpty(requestStr) || requestStr.equalsIgnoreCase("{}")) {
            throw new BusinessException("[防刷]入参不允许为空");
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());

        Prevent prevent = method.getAnnotation(Prevent.class);
        String methodFullName = method.getDeclaringClass().getName() + method.getName();

        entrance(prevent, requestStr, methodFullName);

        return;
    }

    private void entrance(Prevent prevent, String requestStr, String methodFullName) throws Exception {
        PreventStrategy strategy = prevent.strategy();
        switch (strategy) {
            case DEFAULT:
                defaultHandle(requestStr, prevent, methodFullName);
                break;
            default:
                throw new BusinessException("无效的策略");
        }
    }

    /**
     * 默认处理方式
     * @param requestStr
     * @param prevent
     * @param methodFullName
     * @throws Exception
     */
    private void defaultHandle(String requestStr, Prevent prevent, String methodFullName) throws Exception {
        String base64Str = toBase64String(requestStr);
        long expire = Long.parseLong(prevent.value());

        String key = methodFullName + base64Str;
        String resp = (String) redisUtil.get(key);
        if(StringUtils.isEmpty(resp)) {
            redisUtil.set(key, requestStr, expire);
        } else {
            String message = !StringUtils.isEmpty(prevent.message()) ? prevent.message() : expire + "秒内不允许重复请求";
            throw new BusinessException(message);
        }
    }

    /**
     * 对象转换为base64字符串
     * @param obj
     * @return
     * @throws Exception
     */
    private String toBase64String(String obj) throws Exception {
        if(StringUtils.isEmpty(obj)) {
            return null;
        }

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] bytes = obj.getBytes("UTF-8");
        return encoder.encodeToString(bytes);
    }

}
