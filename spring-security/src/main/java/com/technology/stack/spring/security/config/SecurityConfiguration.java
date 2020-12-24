package com.technology.stack.spring.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SpringSecurity安全配置类
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/23 10:46
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * 密码加密，使用BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    // 使用自定义的UserDetailsServiceImpl
}
