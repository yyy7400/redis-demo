package com.yang.redis.controller;

import com.yang.redis.RedisUtil;
import com.yang.redis.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yangyuyang
 * @date 2019-11-29
 */
@Slf4j
@RestController
public class RedisController {

    private static int expireTime = 60;

    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/redis/set")
    public boolean set(String key, String value) {
        User user = new User();
        user.setId(Long.valueOf(1));
        user.setGuid(String.valueOf(1));
        user.setName("yang");
        user.setAge(String.valueOf(20));
        user.setCreateTime(new Date());

        return true;
        //return redisUtil.set(key, value);
    }

    @RequestMapping("/redis/get")
    public Object get(String key) {
        //return "kkk";

        Map<String, String> map = new HashMap<>();
        for(int i = 0; i < 10000; i++) {
            map.put(i+"", "kkk");
        }

        long t1 = System.currentTimeMillis();
        for(int i = 0; i < Integer.valueOf(key); i++) {
            //map.get("123");
            redisUtil.get("13");
            //redisUtil.set("1", "jjj");
        }
        long t2 = System.currentTimeMillis();
        System.out.println(t2 -t1);

        return (t2 -t1);
        //return redisUtil.get(key);
    }

    @RequestMapping("/redis/expire")
    public boolean expire(String key) {
        return redisUtil.expire(key, expireTime);
    }

    @PostMapping("/redis/post")
    public Integer post(@RequestBody List<Integer> list){
        return list.size();
    }
}
