package com.technology.stack.spring.swagger.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * swagger-bootstrap-ui controller
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/4 17:38
 */
@Api("spring-swagger-ui")
@RestController
public class SwaggerController {

    @ApiOperation("hello interface")
    @GetMapping("/hello/{username}")
    public String hello(@PathVariable("username") String username) {
        return "hello world";
    }
}
