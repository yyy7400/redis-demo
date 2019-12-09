package com.yang.redis.aop;

import java.lang.annotation.*;

/**
 * 防刷注解，
 * @author yangyuyang
 * @date 2019-12-06
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Prevent {

    /**
     * 限制的时间值
     * @return
     */
    String value() default "60";

    /**
     * 提示
     * @return
     */
    String message() default "";

    /**
     * 策略
     * @return
     */
    PreventStrategy strategy() default PreventStrategy.DEFAULT;
}
