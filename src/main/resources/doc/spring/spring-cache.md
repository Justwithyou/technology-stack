# SpringBoot整合Cache进程内缓存

**具体代码实现阅读spring-jdbc模块的代码**

1. 引入依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-cache</artifactId>
    </dependency>
</dependencies>
```

2. 在启动类上添加注解@EnableCaching

添加该注解之后会自动化配置合适的缓存管理器：Generic/JCache/EhCache2.x/Hazelcast/Infinispan/Couchbase/Redis/Caffeine/Simple

除了自动位置，也可以在配置文件中使用spring.cache.type强制指定

3. 配置注解

@CacheConfig：主要用于配置类中会用到的一些共用的缓存配置
@Cacheable：配置在方法上，方法的返回会被缓存，之后再调起方法，则会先从缓存中获取
@CachePut：配置在方法上，能根据参数定义条件进行缓存，多用于数据新增和修改上
@CacheEvict：配置在方法上，多用于删除，用来从缓存中移除相应数据

4. 缓存管理类CacheManager

底层默认为ConCurrentHashMap集合

## Ehcache

SpringBoot整合EhCache进程内缓存

1. 引入依赖

```xml
<!-- EhCache -->
<dependency>
    <groupId>net.sf.ehcache</groupId>
    <artifactId>ehcache</artifactId>
</dependency>
```

2. 创建ehcache.xml文件

## Redis

SpringCache + Redis
RedisCacheManager


