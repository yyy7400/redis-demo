package com.yang.redis.controller;

import com.yang.redis.aop.Prevent;
import com.yang.redis.entity.TestRequest;
import common.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 切面实现入参校验
 * @author yangyuyang
 * @date 2019-12-06
 */
@RestController
public class PreventController {
    /**
     * 测试防刷
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/testPrevent")
    @Prevent
    public Response testPrevent(TestRequest request) {
        return Response.success("调用成功");
    }


    /**
     * 测试防刷
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/testPreventIncludeMessage")
    @Prevent(message = "10秒内不允许重复调多次", value = "10")
    public Response testPreventIncludeMessage(TestRequest request) {
        return Response.success("调用成功");
    }
}
