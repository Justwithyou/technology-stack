package com.technology.stack.controller;

import org.springframework.web.bind.annotation.*;

/**
 * Hello控制器
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/2 15:22
 */
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String index() {
        return "Hello World";
    }
}
