package com.technology.stack.spring.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * SpringBoot整合RabbitMQ服务
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/30 15:04
 */
@SpringBootApplication
public class SpringRabbitmqApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SpringRabbitmqApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringRabbitmqApplication.class);
    }
}
