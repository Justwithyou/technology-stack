package com.technology.stack.sprin.rocketmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * SpringBoot整合RocketMQ服务
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2021/1/4 10:53
 */
@SpringBootApplication
public class SpringRocketmqApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SpringRocketmqApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringRocketmqApplication.class);
    }
}
