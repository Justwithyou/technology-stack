package com.technology.stack.spring.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * SpringBoot整合SpringSecurity
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/21 18:57
 */
@SpringBootApplication
public class SpringSecurityApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringSecurityApplication.class);
    }
}
