package com.technology.stack.spring.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * SpringSecurity安全配置类
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/21 19:48
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 验证配置类设置用户密码，不实现加密
        // return NoOpPasswordEncoder.getInstance();
        // 参数为密码迭代次数
        return new BCryptPasswordEncoder(10);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("security")
                .password("security")
                .roles("admin");
    }

    @Override
    public void configure(WebSecurity web) {
        // 匹配忽略拦截的URL地址
        web.ignoring().antMatchers("/js/**", "/css/**","/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                // 配置登录页面地址
                .loginPage("/login.html")
                // 配置登录接口地址
                .loginProcessingUrl("/login.html")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/index")
                .successForwardUrl("/index")
                .permitAll()
                .and()
                .csrf().disable();
        http.authorizeRequests()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                .logoutSuccessUrl("/index")
                .deleteCookies()
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .permitAll()
                .and();
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("admin")
                .antMatchers("/user/**").hasRole("user")
                .anyRequest().authenticated()
                .and();
    }
}
