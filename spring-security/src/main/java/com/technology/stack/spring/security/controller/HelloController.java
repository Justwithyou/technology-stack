package com.technology.stack.spring.security.controller;

import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.web.bind.annotation.*;

/**
 * REST API
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/21 18:59
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}
