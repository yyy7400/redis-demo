package com.yang.redis;

import java.util.concurrent.TimeUnit;

/**
 * 过期枚举类
 * @author yangyuyang
 * @date 2019-11-29
 */
public abstract class AbstractStatus {
    /**
     * 过期时间相关枚举
     */
    public static enum ExpireEnum{

        //未读消息的有效期为30天
        UNREAD_MSG(30L, TimeUnit.DAYS);

        /**
         * 过期时间
         */
        private Long time;
        /**
         * 时间单位
         */
        private TimeUnit timeUnit;

        ExpireEnum(Long time, TimeUnit timeUnit) {
            this.time = time;
            this.timeUnit = timeUnit;
        }

        public Long getTime() {
            return time;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }
    }
}
