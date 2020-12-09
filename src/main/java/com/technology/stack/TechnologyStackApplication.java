package com.technology.stack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 技术栈服务
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/2 14:46
 */
@SpringBootApplication
public class TechnologyStackApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TechnologyStackApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TechnologyStackApplication.class);
    }
}
