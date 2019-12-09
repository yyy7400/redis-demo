package com.yang.redis.controller;

import com.yang.redis.aop.Limiter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangyuyang
 */
@RestController
public class LimiterController {

    @Limiter(frequency = 5)
    @GetMapping("/limiter/getTest")
    public String getTest() {
        return "show";
    }
}
