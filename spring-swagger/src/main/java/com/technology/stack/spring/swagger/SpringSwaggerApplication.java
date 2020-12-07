package com.technology.stack.spring.swagger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * SpringBoot整合Swagger2.9.2+Swagger-bootstrap-ui1.9.6实现接口API输出
 * 其他版本：
 * Swagger2.8.0+Springfox-swagger-ui2.8.0
 * Swagger3.0.0+Springfox-swagger-ui3.0.0
 * knife4j(Swagger-bootstrap-ui的2.x版本)
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/4 17:26
 */
@EnableOpenApi
@SpringBootApplication
public class SpringSwaggerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSwaggerApplication.class, args);
    }
}
