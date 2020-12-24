# SpringBoot基础

SpringBoot官网：[https://spring.io/projects/spring-boot]

## 概念

EJB --> Spring --> SpringBoot --> SpringCloud(SpringCloudAlibaba) --> ServiceMesh

EJB开发过于繁重，出现了Spring框架，随着Spring大火，变得覆盖面广而全，也开始变得复杂，需要过多的XML配置和第三方配置，于是SpringBoot出现，简单容易
上手，且不再需要繁杂的XML配置，约定大于配置，可易于集成其他框架，易插拔，内嵌Web容器等，随着应用越来越大，单一服务变得不易维护，由此SpringCloud微服务
出现，把服务按照业务流程分割成不容的模块化服务，独立开发部署，并支持多种语言的融合，服务网格？

**SpringBoot在Spring的基础之上，通过简化Spring应用初识搭建以及开发过程，默认约定部分配置项，不再需要繁杂的XML配置，整合诸多可插拔的
组件（框架），内嵌容器工具，方便开发人员快速搭建应用系统的框架**

### SpringBoot的主要优点：

1. 搭建项目快，入门更简单
2. 开箱即用，提供各种默认配置来简化项目配置
3. 内嵌式容器简化Web项目
4. 无过多冗余代码生成和XML配置的要求
5. 方便监控，使用Spring Boot Actuator组件提供了应用的系统监控，可以查看应用配置的详细信息

## 配置文件

* SpringBoot针对常用的开发场景提供了一系列的默认配置来减少Spring原有的复杂且少有改动的模板化配置内容

### 配置基础

SpringBoot2.x配置参考指南：[https://www.cnblogs.com/King2019Blog/p/12902496.html]

SpringBoot默认的配置目录：src/main/resources
SpringBoot默认的配置文件为src/main/resources/application.properties（**bootstrap**.properties/**bootstrap**.yml优先级更高，使用较少）

YAML文件格式：大纲缩进
YAML文件目前无法通过@PropertySources注解来加载配置

#### 自定义参数

可以在配置文件中定义需要的自定义属性，然后使用@Value注解加载配置参数

@Value注解加载配置采纳数时可以使用两种方式：

- placeHolder方式，格式为：${...}
- SpEL表达式，格式为：#{...}，大括号内为SpEL表达式

* 参数引用

在application.properties中的各个参数之间，也可以直接通过placeHolder方式引用：${...}

* 使用随机数

在某些特殊情况下，配置文件中的参数我们希望每次加载时都不一致，这时可以通过：${random}配置产生随机的int值、long值或者String字符串

${random}的几种配置方式：

_这种方式可以用于设置应用端口等场景，需控制出现端口冲突的情况_

```
# 随机字符串
org.spring.boot.technology.stack.value=${random.value}
# 随机int
org.sprig.boot.technology.stack.number=${random.int}
# 随机long
org.sprig.boot.technology.stack.big-number=${random.long}
# 10以内的随机数
org.sprig.boot.technology.stack.int-in-ten=${random.int(10)}
# 10-20的随机数
org.sprig.boot.technology.stack.int-between-ten-and-twenty=${random.int[10,20]}
```

* 命令行参数

通过命令行方式启动SpringBoot应用时，连续的两个减号'--'就是对application.properties中的属性进行赋值

* 多环境配置

在SpringBoot中多环境配置文件名需要满足application-{profile}.properties/application-{profile}.yml的格式，其中{profile}对应不同的环境标识，
具体加载哪个配置文件，则可以在application.properties/application.yml中通过spring.profiles.active属性值指定，其值对应不同环境配置文件中的
{profile}标识

多环境配置文件的常用配置思路：
application.properties/application.yml中配置通用内容，并设置spring.profiles.active属性的值
application-{profile}.properties/application-{profile}.yml中配置各个环境不同的内容
可通过命令行激活不同环境的配置

也可通过spring.config.locations指定外部配置文件

#### 加载顺序

1. 内部配置文件的加载

> 1. jar包同级的config目录下的配置文件
>
> 2. jar包同级的配置文件
>
> 3. jar包内部classpath的config目录
>
> 4. jar包内部classpath中的配置文件

2. 外部配置文件的加载

> 1. 命令行中的传入参数
>
> 2. SPRING_APPLICATION_JSON中的属性，SPRING_APPLICATION_JSON是以JSON格式配置在系统环境变量中的内容
>
> 3. java:comp/env中的JNDI属性
>
> 4. java的系统属性，可以通过System.getProperties()获得的内容
>
> 5. 操作系统的环境变量
>
> 6. 通过random.*配置的随机属性
>
> 7. 位于当前jar包之外，针对不同{profile}环境的配置文件
>
> 8. 位于当前jar包之内，针对不同{profile}环境的配置文件
>
> 9. 位于当前jar包之外的application.properties配置文件或者YAML配置文件
>
> 10. 位于当前jar包之内的application.properties配置文件或者YAML配置文件
>
> 11. 在@Configuration注解的类中，通过@PropertySource注解定义的属性
>
> 12. 应用默认属性，使用SpringApplication.setDefaultProperties定义的内容

#### 2.x的新特性

##### 配置文件绑定

* 简单类型

SpringBoot2.x配置属性加载的时候，除了和原有版本一样移除特殊字符外，还会把配置以全部小写的方式进行匹配和加载（推荐以全小写配置'-'分隔符的方式配置）

* List类型

在properties中使用[]来定位列表类型：
> properties: 
> spring.example.url[0]=http://www.baidu.com
> spring.example.url[1]=http://spring.io
> yml: 
> spring: 
>   example: 
>       url: 
>         - http://www.baidu.com
>         - http://spring.io

同时也支持以逗号分隔的配置方式：
> properties: 
> spring.example.url=http://www.baidu.com,http://spring.io
> yml: 
> spring: 
>   example: 
>       url: http://www.baidu.com,http://spring.io

注意：SpringBoot2.x中对于List类型的配置必须是连续的，否则会抛出异常UnboundConfigurationPropertiesException

* Map类型

> properties: 
> spring.example.map.name=spring
> spring.example.map.value=spring
> yml: 
> spring: 
>   example: 
>       name: spring
>       value: spring

##### 环境属性绑定

* 简单类型

在环境变量中通过小写转换为大写，'.'转换成'_'来配置文件中的内容，如SPRING_JPA_DATABASE_PLATFORM=mysql和spring.jpa.database.platform=mysql等价

* List类型

由于在环境变量中[]无法使用，所以用'_'代替，任何由下划线包围的数字都会被认为是[]的数组形式

##### 系统属性绑定

* 简单类型

系统属性和文件配置中的类似，都以移除特殊字符并转化小写后实现绑定
> -Dspring.jpa.database-platform=mysql

* List类型

系统属性的绑定和文件配置中的类似，通过[]来标示
> -D"spring.example.url[0]=http://www.baidu.com"
> -D"spring.example.url[1]=http://spring.io

##### 属性的读取

在Spring应用程序的environment中读取属性的时候，每个属性的唯一名称符合以下规则：

1. 通过.分离各个元素
2. 最后一个.将前缀和属性名称分开
3. 必须是字母和数字
4. 必须是小写字母
5. 用连字符-分割单词
6. 唯一允许的其他字符是[]，用于List的索引
7. 不能以数字开头

注意：@Value注解获取配置值时也要遵循统一的规则

##### 全新的绑定API

在SpringBoot2.x中新增了绑定API可以更方便的获取配置信息

* 简单类型
 
配置属性：
> spring.example.name=spring
> spring.example.value=spring

定义配置类：
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
@Data
@ConfigurationProperties
public class ExampleProperties {
    
    private String name;
    private String value;
}

// 通过Binder获取配置信息
@SpringBootApplication
public class Application {
    
    public static void main(String[] args){
        ApplicationContext context = SpringApplication.run(Application.class, args);
        Binder binder = Binder.get(context.getEnvironment());
        
        // 绑定简单属性
        ExampleProperties ep = binder.bind("spring.example", Bindable.of(ExampleProperties.class)).get();
        System.out.println(ep);

        // 绑定List配置，代码示示例
        List<String> post = binder.bind("spring.list.post", Bindable.listOf(String.class)).get();
        System.out.println(post);
        
        List<PostInfo> posts = binder.bind("spring.list.posts", Bindable.listOf(PostInfo.class)).get();
        System.out.println(posts);

    }
}
```

## SpringBoot启动过程原理分析

* SpringBoot入口类的要求是顶层包下面第一个含有main方法的类，并使用@SpringBootApplication来启用SpringBoot特性，使用
SpringApplication.run方法启动项目

* SpringApplication.run方法

> 第一个参数 Class<?> primarySource：加载的主要资源类
>
> 第二个参数 args：传递给应用的参数
>
> 先使用主要资源类实例化一个SpringApplication对象，再调用这个对象的run方法

### 实例化SpringApplication对象

* SpringApplication构造方法

```
public SpringApplication(Class<?>... primarySources) {
    this((ResourceLoader)null, primarySources);
}

public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {

    this.sources = new LinkedHashSet();
    this.bannerMode = Mode.CONSOLE;
    this.logStartupInfo = true;
    this.addCommandLineProperties = true;
    this.addConversionService = true;
    this.headless = true;
    this.registerShutdownHook = true;
    this.additionalProfiles = new HashSet();
    this.isCustomEnvironment = false;
    this.lazyInitialization = false;
    // 1. 资源初始化，资源加载器为Null
    this.resourceLoader = resourceLoader;
    // 2. 断言主要资源类不能为Null，否则报错
    Assert.notNull(primarySources, "PrimarySources must not be null");
    // 3. 初始化主要资源类集合并去重
    this.primarySources = new LinkedHashSet(Arrays.asList(primarySources));
    // 4. 推断当前的Web应用类型，根据classpath路径下的类判断当前应用类型：NONE(非Web应用)、SERVLET(servlet web应用)、REACTIVE(reactive web应用)
    this.webApplicationType = WebApplicationType.deduceFromClasspath();
    // 5. 设置应用上下文初始化器
    // 获取'META-INF/spring.factories'文件中的上下文初始化器去重并实例化，然后列表排序返回
    this.setInitializers(this.getSpringFactoriesInstances(ApplicationContextInitializer.class));
    // 6. 设置监听器，获取'META-INF/spring.factories'文件中的监听器并实例化（观察者模式）
    this.setListeners(this.getSpringFactoriesInstances(ApplicationListener.class));
    // 7. 推断主入口应用类，构造RuntimeException获取异常栈链，匹配异常栈链中方法名为main的元素，通过反射Class.forName获取该元素的类对象信息
    this.mainApplicationClass = this.deduceMainApplicationClass();
}

WebApplicationType.deduceFromClasspath
static WebApplicationType deduceFromClasspath() {
    if (ClassUtils.isPresent("org.springframework.web.reactive.DispatcherHandler", (ClassLoader)null) && !ClassUtils.isPresent("org.springframework.web.servlet.DispatcherServlet", (ClassLoader)null) && !ClassUtils.isPresent("org.glassfish.jersey.servlet.ServletContainer", (ClassLoader)null)) {
        return REACTIVE;
    } else {
        String[] var0 = SERVLET_INDICATOR_CLASSES;
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            String className = var0[var2];
            if (!ClassUtils.isPresent(className, (ClassLoader)null)) {
                return NONE;
            }
        }
        return SERVLET;
    }
}


```

### run方法

* run方法源码

```
public ConfigurableApplicationContext run(String... args) {
    // 1. 创建并启动计时监控类
    // 记录当前任务名称以及应用启动时间
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    // 2. 声明应用上下文和异常报告集合
    ConfigurableApplicationContext context = null;
    Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList();

    // 3. 设置系统属性'java.atw.headless'属性的值，默认为true
    // java.awt.headless：运行一个headless服务器，进行简单的图像处理
    this.configureHeadlessProperty();

    // 4. 创建Spring应用运行监听器，并触发应用启动事件
    // 获取'META-INF/spring.factories'文件中配置的监听器列表然后实例化，再启动
    SpringApplicationRunListeners listeners = this.getRunListeners(args);
    listeners.starting();
    
    Collection exceptionReporters;
    try {
        // 5. 初始化默认应用参数类
        ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);

        // 6. 根据运行监听器和应用默认参数配置应用环境
        // 获取应用环境配置，配置应用环境，运行监听器发布环境准备就绪事件，绑定应用环境
        ConfigurableEnvironment environment = this.prepareEnvironment(listeners, applicationArguments);
        this.configureIgnoreBeanInfo(environment);

        // 7. 创建Banner打印类
        Banner printedBanner = this.printBanner(environment);

        // 8. 创建应用上下文
        // 根据不同的应用类型初始化不同的应用上下文
        context = this.createApplicationContext();

        // 9. 准备异常报告器
        // 获取'META-INF/spring.factories'文件中配置的异常类名称并实例化
        exceptionReporters = this.getSpringFactoriesInstances(SpringBootExceptionReporter.class, new Class[]{ConfigurableApplicationContext.class}, context);
        
        // 10. 准备应用上下文
        // 绑定环境到应用上下文，配置上下文的Bean生成器以及资源加载器，应用上下文所有初始化器，触发运行监听器的上下文准备就绪事件
        // 启动日志记录，注册两个特殊的单例Bean对象，加载所有资源，触发运行监听器的上下文加载事件
        this.prepareContext(context, environment, listeners, applicationArguments, printedBanner);

        // 11. 刷新应用上下文
        this.refreshContext(context);

        // 12. 应用上下文刷新之后根据上下文和应用默认参数进行后置处理
        this.afterRefresh(context, applicationArguments);

        // 13. 停止计时监控类，并统计一些任务执行信息
        stopWatch.stop();

        // 14. 输出日志记录执行主类名、时间信息
        if (this.logStartupInfo) {
            (new StartupInfoLogger(this.mainApplicationClass)).logStarted(this.getApplicationLog(), stopWatch);
        }
        
        // 15. 发布应用上下文启动完成事件，触发所有运行监听器的开始事件
        listeners.started(context);

        // 16. 执行所有runner运行器，执行所有ApplicationRunner和CommandLineRunner两种运行器
        this.callRunners(context, applicationArguments);
    } catch (Throwable var10) {
        this.handleRunFailure(context, var10, exceptionReporters, listeners);
        throw new IllegalStateException(var10);
    }

    try {
        // 17. 发布应用上下文就绪事件
        listeners.running(context);

        // 18. 返回应用上下文
        return context;
    } catch (Throwable var9) {
        this.handleRunFailure(context, var9, exceptionReporters, (SpringApplicationRunListeners)null);
        throw new IllegalStateException(var9);
    }
}
```

## 自动装配原理

**重要注解：**

> @SpringBootApplication
> @EnableAutoConfiguration
> @AutoConfigurationPackage
> @Import
> 以上注解涉及Bean的发现以及加载
> 以下注解涉及Java中的Bean配置、自动配置依赖条件、Bean配置参数获取等
> @Configuration
> @Bean
> @Condition···(条件注解)
> @ConfigurationProperties
> @EnableConfigurationProperties

* Java中的Bean配置

通过@Configuration注解标识一个类为配置类（能生产让Spring IOC容器管理的Bean实例的工厂），而@Bean注解表示当前被注解的方法将返回一个对象，该对象应该被注册到容器中

* 自动配置条件依赖

SpringBoot使用大量的条件注解来完成自动配置，常见的条件注解有：@ConditionalOnBean、@ConditionalOnClass、@ConditionalOnExpression、@ConditionalOnMissingBean
、@ConditionalOnMissingClass、@ConditionalOnNotWebApplication等

* Bean参数获取

在创建Bean时可能需要获取配置文件中的参数，此时需要用到@ConfigurationProperties和@EnableConfigurationProperties两个注解，@ConfigurationProperties注解获取配置
中的属性值转换成Bean对象，而@EnableConfigurationProperties注解则把转成的Bean对象注入到容器中

* Bean的发现

入口类上的注解@SpringBootApplication由以下三个注解构成：@SpringBootConfiguration、@EnableAutoConfiguration、@ComponentScan

@SpringBootConfiguration实际还是@Configuration，标识被注解的类为配置类

@ComponentScan注解的作用是自动扫描并加载符合条件的组件，默认扫描和入口类同级的类以及同级包下的类（所以一般SpringBoot项目的结构为所有的包和入口类同一层级）

@EnableAutoConfiguration注解是发现和收集注册依赖包中的Bean的核心注解
@EnableAutoConfiguration由两个主要的注解组成：@AutoConfigurationPackage、@Import
@AutoConfigurationPackage内部也是由@Import组成，负责加载入口类所在包下的所有组件并注册到Spring容器
```
AutoConfigurationPackages.Registrar
static class Registrar implements ImportBeanDefinitionRegistrar, DeterminableImports {
    Registrar() {
    }

    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        AutoConfigurationPackages.register(registry, (new AutoConfigurationPackages.PackageImport(metadata)).getPackageName());
    }

    public Set<Object> determineImports(AnnotationMetadata metadata) {
        return Collections.singleton(new AutoConfigurationPackages.PackageImport(metadata));
    }
}
```

@EnableAutoConfiguration.@Import
```
AutoConfigurationImportSelector.selectImports
public String[] selectImports(AnnotationMetadata annotationMetadata) {
    if (!this.isEnabled(annotationMetadata)) {
        return NO_IMPORTS;
    } else {
        AutoConfigurationMetadata autoConfigurationMetadata = AutoConfigurationMetadataLoader.loadMetadata(this.beanClassLoader);
        AutoConfigurationImportSelector.AutoConfigurationEntry autoConfigurationEntry = this.getAutoConfigurationEntry(autoConfigurationMetadata, annotationMetadata);
        return StringUtils.toStringArray(autoConfigurationEntry.getConfigurations());
    }
}

AutoConfigurationImportSelector.getAutoConfigurationEntry
protected AutoConfigurationImportSelector.AutoConfigurationEntry getAutoConfigurationEntry(AutoConfigurationMetadata autoConfigurationMetadata, AnnotationMetadata annotationMetadata) {
    if (!this.isEnabled(annotationMetadata)) {
        return EMPTY_ENTRY;
    } else {
        AnnotationAttributes attributes = this.getAttributes(annotationMetadata);
        List<String> configurations = this.getCandidateConfigurations(annotationMetadata, attributes);
        configurations = this.removeDuplicates(configurations);
        Set<String> exclusions = this.getExclusions(annotationMetadata, attributes);
        this.checkExcludedClasses(configurations, exclusions);
        configurations.removeAll(exclusions);
        configurations = this.filter(configurations, autoConfigurationMetadata);
        this.fireAutoConfigurationImportEvents(configurations, exclusions);
        return new AutoConfigurationImportSelector.AutoConfigurationEntry(configurations, exclusions);
    }
}

AutoConfigurationImportSelector.getCandidateConfigurations
protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
    List<String> configurations = SpringFactoriesLoader.loadFactoryNames(this.getSpringFactoriesLoaderFactoryClass(), this.getBeanClassLoader());
    Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you are using a custom packaging, make sure that file is correct.");
    return configurations;
}

SpringFactoriesLoader.loadFactoryNames
public static List<String> loadFactoryNames(Class<?> factoryType, @Nullable ClassLoader classLoader) {
    String factoryTypeName = factoryType.getName();
    return (List)loadSpringFactories(classLoader).getOrDefault(factoryTypeName, Collections.emptyList());
}

SpringFactoriesLoader.loadSpringFactories
private static Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) {
    MultiValueMap<String, String> result = (MultiValueMap)cache.get(classLoader);
    if (result != null) {
        return result;
    } else {
        try {
            Enumeration<URL> urls = classLoader != null ? classLoader.getResources("META-INF/spring.factories") : ClassLoader.getSystemResources("META-INF/spring.factories");
            LinkedMultiValueMap result = new LinkedMultiValueMap();

            while(urls.hasMoreElements()) {
                URL url = (URL)urls.nextElement();
                UrlResource resource = new UrlResource(url);
                Properties properties = PropertiesLoaderUtils.loadProperties(resource);
                Iterator var6 = properties.entrySet().iterator();

                while(var6.hasNext()) {
                    Entry<?, ?> entry = (Entry)var6.next();
                    String factoryTypeName = ((String)entry.getKey()).trim();
                    String[] var9 = StringUtils.commaDelimitedListToStringArray((String)entry.getValue());
                    int var10 = var9.length;

                    for(int var11 = 0; var11 < var10; ++var11) {
                        String factoryImplementationName = var9[var11];
                        result.add(factoryTypeName, factoryImplementationName.trim());
                    }
                }
            }

            cache.put(classLoader, result);
            return result;
        } catch (IOException var13) {
            throw new IllegalArgumentException("Unable to load factories from location [META-INF/spring.factories]", var13);
        }
    }
}
```
通过AutoConfigurationImportSelector.selectImports方法获取到自动配置的Bean数组，然后再根据条件注解确认是否需要加载到Spring容器中；使用
SpringFactoriesLoader.loadSpringFactories加载每个jar包下的"META-INF/spring.factories"文件中的自动配置项，spring.factories文件中有一个
···.EnableAutoConfiguration的key值定义了一组需要自动配置的Bean

* Bean加载的方法总结

1. 使用@Configuration与@Bean注解（第三方Bean定义）
2. 使用@RestController/@Service/@Repository/@Component标注类，@ComponentScan注解扫描（顶层包下自动扫描）
3. 使用@Import注解导入（自动配

## 资源初始化方式

通常使用三种方式：@PostConstruct注解、实现InitializingBean接口、配置init-method属性绑定方法
执行顺序：构造方法 -> @PostConstruct -> @InitializingBean.afterPropertiesSet -> init-method绑定的方法
效率：@InitializingBean.afterPropertiesSet通过接口调用方式，效率较高，@PostConstruct和init-method绑定的方法都是通过反射实现

#### @PostConstruct注解

@PostConstruct注解

#### InitializingBean接口

重写afterPropertiesSet方法

#### init-method配置属性绑定

init-method

## Spring Boot Starter

起步依赖，可以看做一种可插拔的插件，只需要引入即可自动扫描到要加载的信息并启用对应配置

* 自定义Starter

把独立于业务功能之外的模块抽离出来封装复用（动态数据源、登录模块、日志切面），官方建议的命名规则：module-spring-boot-starter（官方的Starter命令格式为：
spring-boot-starter-module），区别自定义和官方的不同命名格式

@ConfigurationProperties获取配置，@EnableConfigurationProperties引入被@ConfigurationProperties注解的类
@Condition···类型条件注解控制是否自动配置
@Configuration注解在类上，配合@Bean注解在方法上声明自定义的Bean配置

spring-boot-configuration-processor此依赖包可用于在配置文件新增自定义配置有对应的提示信息













