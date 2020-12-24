package com.technology.stack.spring.jdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * TODO
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/7 11:20
 */
@EnableOpenApi
@SpringBootApplication
public class SpringJdbcApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SpringJdbcApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringJdbcApplication.class);
    }
}
