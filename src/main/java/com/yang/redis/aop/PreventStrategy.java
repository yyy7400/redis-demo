package com.yang.redis.aop;

/**
 * 防刷策略枚举
 * @author yangyuyang
 * @date 2019-12-06
 */
public enum PreventStrategy {
    /**
     * 默认，一分钟内不允许再次请求
     */
    DEFAULT
}
