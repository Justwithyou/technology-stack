# SpringBoot整合权限管理

Apache Shiro框架

Spring Security框架

权限管理核心：认证（登录认证）、授权（鉴权）

## SpringSecurity

常见认证方式： 

1. HTTP BASIC Authentication headers：基于IETF RFC标准
2. HTTP Digest authentication headers：基于IETF RFC 标准
3. HTTP X.509 client certificate exchange：基于IETF RFC 标准
4. LDAP：跨平台身份验证
5. Form-based authentication：基于表单的身份验证
6. Run-as authentication：用户用户临时以某一个身份登录
7. OpenID authentication：去中心化认证

比较少见的认证方式：

1. Jasig Central Authentication Service：单点登录
2. Automatic “remember-me” authentication：记住我登录
3. Anonymous authentication：匿名登录

授权：

支持基于URL的请求授权、支持方法访问授权和对象访问授权

> CSRF：跨站请求伪造
>
> XSS：跨站脚本攻击

### 基本使用方式

#### 登录流程

UsernamePasswordAuthenticationFilter --> AbstractAuthenticationProcessingFilter
UsernamePasswordAuthenticationToken --> AbstractAuthenticationToken
WebAuthenticationDetails(remoteAddress, sessionId)

#### 引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

#### 项目启动时，默认登录密码会打印在日志中：Using generated security password: 89378db9-1e73-4359-a40e-ee1504056f63
   这是Spring Security为默认用户user生成的临时密码，是一个UUID字符串（可查看源码UserDetailsServiceAutoConfiguration类）
   直接访问接口，会自动跳转到登录页面，可使用默认用户user以及在日志中打印的临时密码登录
   
#### 用户配置

默认的临时密码会在每次重启项目之后变化，整理两种简单的用户、密码配置方式

1） 配置文件

可以直接在配置文件application.yml文件中配置默认的用户和密码，根据源码中自动配置时读取配置文件的前缀spring.security自定义用户和密码覆盖
默认配置即可，在SecurityProperties配置类中，会使用自定义配置覆盖内部类User中的用户和密码，启动时不再打印通过UUID生成的密码

```yaml
spring: 
    security: 
      user: 
        name: security
        password: security
```

2） 配置类

加密方案：散列算法（哈希算法），通常加密还会使用加盐值的方式

常用的散列函数：MD5消息摘要算法、安全散列算法（SHA）

Spring Security提供多种密码加密方案，官方推荐使用BCryptPasswordEncoder，BCryptPasswordEncoder使用BCrypt强哈希函数，开发者可以在使用
时提供strength和SecureRandom实例，strength越大，密钥迭代次数越多，密钥迭代次数为2^strength，strength取值在4~31之间，默认为10

Spring Security中的BCryptPasswordEncoder自带加盐，BCryptPasswordEncoder是PasswordEncoder接口的实现类

* PasswordEncoder接口类

```
/** 接口中定义三个方法 */
public interface PasswordEncoder {
    // 用来对明文密码进行加密，返回密文
    String encode(CharSequence var1);
    // 密码校对方法
    boolean matches(CharSequence var1, String var2);
    // 是否需要再次加密
    default boolean upgradeEncoding(String encodedPassword) {
        return false;
    }
}
```

* 配置

自定义SecurityConfig
```
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("security")
                .password("security").roles("admin");
    }
}
```

#### 自定义登录页面

默认登录页面地址：/login，如果为指定登录接口的地址，则默认设置为和登录页面地址一致

相关源码类：FormLoginConfigurer.init/AbstractAuthenticationFilterConfigurer(构造方法,updateAuthenticationDefaults方法) 

登录参数：默认为username和password，在FormLoginConfigurer的构造方法中已经指定，通过UsernamePasswordAuthenticationFilter 设置
参数名称，当请求从浏览器来到服务端之后，会从HttpServletRequest中取出用户登录名称和密码，这两个参数也可以通过配置自定义
```
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
            .permitAll()
            .and()
            .csrf().disable();
}
```
  
#### 登录回调

##### 登录成功回调

* defaultSuccessUrl：存在两个重载方法

1. 一个重载方法只有一个参数，指定登录成功跳转地址URL，分两种情况，如果是直接在浏览器地址栏输入登录地址，则登录成功后跳转到指定的URL，如果是在
浏览器中输入其他地址，因未登录跳转到登录页面，此时登录成功之后会直接跳到原输入的地址

2. 一个重载方法有两个参数，第二个参数默认为false，也就和第一种一样，如果手动设置第二个参数为true，则不管如何跳转到的登录页面，登录成功之后都
直接跳转到指定的URL

* successForwardUrl：不管怎么跳转到的登录页面，登录成功之后一律跳转到指定的URL

##### 登录失败回调

* failureForwardUrl：登录失败之后发生服务端跳转

* failureUrl：登录失败之后发生重定向

#### 注销登录

默认注销登录接口为/logout，可自定义配置：
```
http.authorizeRequests()
        .and()
        .logout()
        // 注销页面Url
        .logoutUrl("/logout")
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
        // 退出成功跳转Url
        .logoutSuccessUrl("/index")
        // 删除Cookie
        .deleteCookies()
        // 清除认证信息
        .clearAuthentication(true)
        // 使HttpSession失效
        .invalidateHttpSession(true)
        .permitAll()
        .and();    
```


