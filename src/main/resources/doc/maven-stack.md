# maven技术栈知识点归纳

## maven是什么？

**maven：基于对象管理模型（POM-Project Object Model）的项目管理与构建工具，它可以定义项目结构、管理项目依赖，并使用统一的方式进行自动化构建**

## maven用来做什么？

**其核心功能是管理项目之间的依赖关系**

> 通俗来讲，通过pom.xml文件中的配置获取jar包并管理这些依赖的jar包，自动解决项目中的jar包重复与冲突问题，同时在pom.xml文件中定义与生命周期绑定的插件目标，执行项目构建

* maven项目目录结构

> - project
>   - src
>       - main
>           - java 源码目录
>           - resources 配置文件目录
>       - test
>           - java 测试源码目录
>           - resources 测试配置文件目录
>   - target 输出目录
>   - pom.xml 核心配置

* maven的主要功能点总结：

1. 生命周期管理，便捷的构件过程
2. 依赖管理，方便引入所需依赖的jar包
3. 仓库管理，提供统一管理所有jar的工具

## maven怎么用？

### maven的安装

* maven安装包下载：[http://maven.apache.org/docs/history.html]

**_windows和linux下安装文件不同_**

* windows安装

1. 解压maven-windows-version安装包到指定目录
2. 设置环境变量（计算机/我的电脑 -> 右键属性 -> 高级系统设置 -> 环境变量 -> 新建一个M2_HOME的环境变量并添加到path路径下）
3. 打开命令行界面，输入源maven -v验证是否安装成功

* linux安装

1. 把maven-linux-version安装包上传到服务器的指定目录
2. 解压安装包到指定目录（tar -zvxf maven-linux-version.tar.gz）
3. 设置maven全局变量（如果是root用户，则在/etc/profile文件中添加；如果是普通用户，则在.bash_profile文件中添加）
4. 在命令行输入maven -v验证是否安装成功

### maven的配置文件（setting.xml）

#### 概要

1. setting.xml文件的作用

* 用来设置maven参数的配置文件，是maven的全局配置文件，其中包含类似本地仓库、远程仓库和联网使用的代理信息等配置

2. setting.xml文件位置

* 一般在maven安装目录的conf子目录下，或者在用户目录的.m2目录下面

3. 配置的优先级

* 在maven安装目录conf子目录下setting.xml是真正的全局配置，而用户目录的.m2子目录下面的setting.xml只针对当前用户，如果两个目录下同时存在，则
用户目录下的setting.xml会覆盖maven安装目录的定义

#### setting.xml元素详解

1. 顶级元素预览

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                          https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <localRepository/>
  <interactiveMode/>
  <usePluginRegistry/>
  <offline/>
  <pluginGroups/>
  <servers/>
  <mirrors/>
  <proxies/>
  <profiles/>
  <activeProfiles/>
</settings>
```

1.1 localRepository

该配置表示构建系统本地仓库的路径，其默认值~/.m2/repository

1.2 interactiveMode

表示maven是否需要和用户交互以获得输入，如果需要和用户交互则设置为true，反之设置为false

1.3 usePluginRegistry

表示maven是否需要使用plugin-registry.xml文件来管理插件版本，如果需要则设置为true，反之设置为false

1.4 offline

表示在maven进行项目编译和部署等操作时是否允许maven联网来下载所需的信息，如果构建系统需要在离线模式下运行，则为true，反之则为false

1.5 pluginGroups

可以在pluginGroups元素下面定义一系列的pluginGroup元素，表示当通过plugin的前缀来解析plugin的时候到哪里取找，pluginGroup元素指定的是plugin的
groupId，默认情况下，maven会把org.apache.maven.plugins和org.codehaus.mojo添加到pluginGroups下

1.6 servers

一般仓库的下载和部署都是在pom.xml文件中的repositories和distributionManagement元素中定义的，然鹅，一般类似于用户名、密码等认证信息不应该在
pom.xml文件中配置，这些信息可以在setting.xml中配置

```xml
<!-- 配置服务端的一些设置，一些设置如安全证书不应该和pom.xml一起分发，这种类型的信息应该存在于构建服务器上的settings.xml文件中 -->
 <servers>
   <!-- 服务器元素包含配置服务器时需要的信息 -->
   <server>
     <!-- 这是server的id（注意不是用户登陆的id），该id与distributionManagement中repository元素的id相匹配 -->
     <id>server001</id>
     <!--鉴权用户名，鉴权用户名和鉴权密码表示服务器认证所需要的登录名和密码 -->
     <username>my_login</username>
     <!--鉴权密码，鉴权用户名和鉴权密码表示服务器认证所需要的登录名和密码，密码加密功能已被添加到2.1.0 +，详情请访问密码加密页面 -->
     <password>my_password</password>
     <!--鉴权时使用的私钥位置，和前两个元素类似，私钥位置和私钥密码指定了一个私钥的路径（默认是${user.home}/.ssh/id_dsa）以及如果需要的话，一个密语，将来passphrase和password元素可能会被提取到外部，但目前它们必须在settings.xml文件以纯文本的形式声明 -->
     <privateKey>${usr.home}/.ssh/id_dsa</privateKey>
     <!--鉴权时使用的私钥密码 -->
     <passphrase>some_passphrase</passphrase>
     <!--文件被创建时的权限，如果在部署的时候会创建一个仓库文件或者目录，这时候就可以使用权限（permission），这两个元素合法的值是一个三位数字，其对应了unix文件系统的权限，如664，或者775 -->
     <filePermissions>664</filePermissions>
     <!--目录被创建时的权限。 -->
     <directoryPermissions>775</directoryPermissions>
   </server>
 </servers>
```

1.7 mirrors

用于定义一系列的远程仓库的镜像
我们可以在pom中定义一个下载构件的时候所使用的远程仓库，但可能因为网络或者其他因素，对远程仓库的请求比较慢，这时候我们就可以创建镜像以缓解远程仓库的压力，
也就是说会把对远程仓库的请求转移到对其镜像地址的请求；每个远程仓库都有一个id，这样我们可以创建自己的mirror来关联到该仓库，那么以后需要从远程仓库下载构件的
工作会先从镜像仓库获取，如果获取不到再指向远程仓库；在我们定义的mirror中每个远程仓库都只能有一个mirror与它关联，也就是不能配置多个mirror的mirrorOf指向
同一个repositoryId

```xml
<mirrors>
  <!-- 给定仓库的下载镜像。 -->
  <mirror>
    <!-- 该镜像的唯一标识符。id用来区分不同的mirror元素。 -->
    <id>mirrorId</id>
    <!-- 镜像名称 -->
    <name>PlanetMirror Australia</name>
    <!-- 该镜像的URL。构建系统会优先考虑使用该URL，而非使用默认的服务器URL。 -->
    <url>http://downloads.planetmirror.com/pub/maven2</url>
    <!-- 被镜像的服务器的id。例如，如果我们要设置了一个Maven中央仓库（http://repo.maven.apache.org/maven2/）的镜像，就需要将该元素设置成central。这必须和中央仓库的id central完全一致。 -->
    <mirrorOf>repositoryId</mirrorOf>
  </mirror>
</mirrors>
```

1.8 proxies

用来配置不同的代理

```xml
<proxies>
  <!--代理元素包含配置代理时需要的信息 -->
  <proxy>
    <!--代理的唯一定义符，用来区分不同的代理元素。 -->
    <id>my-proxy</id>
    <!--该代理是否是激活的那个。true则激活代理。当我们声明了一组代理，而某个时候只需要激活一个代理的时候，该元素就可以派上用处。 -->
    <active>true</active>
    <!--代理的协议。 协议://主机名:端口，分隔成离散的元素以方便配置。 -->
    <protocol>http</protocol>
    <!--代理的主机名。协议://主机名:端口，分隔成离散的元素以方便配置。 -->
    <host>proxy.somewhere.com</host>
    <!--代理的端口。协议://主机名:端口，分隔成离散的元素以方便配置。 -->
    <port>8080</port>
    <!--代理的用户名，用户名和密码表示代理服务器认证的登录名和密码。 -->
    <username>proxy-user</username>
    <!--代理的密码，用户名和密码表示代理服务器认证的登录名和密码。 -->
    <password>some-password</password>
    <!--不该被代理的主机名列表。该列表的分隔符由代理服务器指定；例子中使用了竖线分隔符，使用逗号分隔也很常见。 -->
    <nonProxyHosts>*.google.com|bio.org</nonProxyHosts>
  </proxy>
</proxies>
```

1.9 profiles

根据环境参数调整构建配置的列表
setting.xml中的profile元素是pom.xml中profile元素的裁剪版本，只包含id/activation/repositories/pluginRepositories/properties元素，如果一个setting.xml
中的profile被激活，它的值会覆盖任何其他定义在pom.xml中带有相同id的profile，当所有约束条件都满足的时候就会激活这个profile

```xml
<profiles>
    <profile>
　　 <!-- profile的唯一标识 -->
        <id>test</id>     
        <!-- 自动触发profile的条件逻辑 -->
        <activation>
            <activeByDefault>false</activeByDefault>
            <jdk>1.6</jdk>
            <os>
                <name>Windows 7</name>
                <family>Windows</family>
                <arch>x86</arch>
                <version>5.1.2600</version>
            </os>
            <property>
                <name>mavenVersion</name>
                <value>2.0.3</value>
            </property>
            <file>
                <exists>${basedir}/file2.properties</exists>
                <missing>${basedir}/file1.properties</missing>
            </file>
        </activation>
        <!-- 扩展属性列表 -->
        <properties />
        <!-- 远程仓库列表 -->
        <repositories />
        <!-- 插件仓库列表 -->
        <pluginRepositories />
      ...
    </profile>
</profiles>
```

1.9.1 activation

自动触发profile的条件逻辑，profile中最重要的元素

jdk：表示当jdk的版本满足条件时激活，也可以使用范围来表示
os：表示当操作系统满足条件时激活
property：键值对的形式，表示当maven检测到这样一个键值对的时候就激活该profile
file：表示当文件存在或不存在的时候激活，exists表示存在，missing表示不存在
activeByDefault：当其值为true时表示如果没有其他的profile处于激活状态的时候，该profile自动激活
properties：用于定义属性键值对，当该profile被激活时，properties下面指定的属性都可以在pom.xml中使用，对应profile的扩展属性列表
repositories：用于定义远程仓库的，当该profile是激活状态的时候，这里面定义的远程仓库将作为当前pom的远程仓库，它是maven用来填充构建系统本地仓库所使用的一组远程仓库
```xml
<repositories>
  <!--包含需要连接到远程仓库的信息 -->
  <repository>
    <!--远程仓库唯一标识 -->
    <id>codehausSnapshots</id>
    <!--远程仓库名称 -->
    <name>Codehaus Snapshots</name>
    <!--如何处理远程仓库里发布版本的下载 -->
    <releases>
      <!--true或者false表示该仓库是否为下载某种类型构件（发布版，快照版）开启。 -->
      <enabled>false</enabled>
      <!--该元素指定更新发生的频率。Maven会比较本地POM和远程POM的时间戳。这里的选项是：always（一直），daily（默认，每日），interval：X（这里X是以分钟为单位的时间间隔），或者never（从不）。 -->
      <updatePolicy>always</updatePolicy>
      <!--当Maven验证构件校验文件失败时该怎么做-ignore（忽略），fail（失败），或者warn（警告）。 -->
      <checksumPolicy>warn</checksumPolicy>
    </releases>
    <!--如何处理远程仓库里快照版本的下载。有了releases和snapshots这两组配置，POM就可以在每个单独的仓库中，为每种类型的构件采取不同的策略。例如，可能有人会决定只为开发目的开启对快照版本下载的支持。参见repositories/repository/releases元素 -->
    <snapshots>
      <enabled />
      <updatePolicy />
      <checksumPolicy />
    </snapshots>
    <!--远程仓库URL，按protocol://hostname/path形式 -->
    <url>http://snapshots.maven.codehaus.org/maven2</url>
    <!--用于定位和排序构件的仓库布局类型-可以是default（默认）或者legacy（遗留）。Maven 2为其仓库提供了一个默认的布局；然而，Maven 1.x有一种不同的布局。我们可以使用该元素指定布局是default（默认）还是legacy（遗留）。 -->
    <layout>default</layout>
  </repository>
</repositories>
<!-- 
releases/snapshots是对构件的类型的限制
enabled表示这个仓库是否允许这种类型的构件
updatePolicy表示多久尝试更新一次，可选值有always/daily/interval:minutes/never
checksumPolicy：当maven在部署项目到仓库时会连同校验文件一起提交，checksumPolicy表示当这个校验文件缺失或者不正确时该如何处理，可选项有ignore/fail/warn
 -->
```
pluginRepositories：在maven中有两种类型的仓库，一种是存储构件的仓库，一种是存储plugin插件的仓库；pluginRepositories的定义和repositories的定义类似，它表示
maven可以在哪里找到所需要的插件，和repositories类似，repositories是管理jar包的仓库，pluginRepositories是管理插件的仓库，maven插件是一种特殊类型的构件
```xml
<pluginRepositories>
  <!-- 包含需要连接到远程插件仓库的信息.参见profiles/profile/repositories/repository元素的说明 -->
  <pluginRepository>
    <releases>
      <enabled />
      <updatePolicy />
      <checksumPolicy />
    </releases>
    <snapshots>
      <enabled />
      <updatePolicy />
      <checksumPolicy />
    </snapshots>
    <id />
    <name />
    <url />
    <layout />
  </pluginRepository>
</pluginRepositories>
```

activation示例：
```xml
<activation>
  <!--profile默认是否激活的标识 -->
  <activeByDefault>false</activeByDefault>
  <!--当匹配的jdk被检测到，profile被激活。例如，1.4激活JDK1.4，1.4.0_2，而!1.4激活所有版本不是以1.4开头的JDK。 -->
  <jdk>1.5</jdk>
  <!--当匹配的操作系统属性被检测到，profile被激活。os元素可以定义一些操作系统相关的属性。 -->
  <os>
    <!--激活profile的操作系统的名字 -->
    <name>Windows XP</name>
    <!--激活profile的操作系统所属家族(如 'windows') -->
    <family>Windows</family>
    <!--激活profile的操作系统体系结构 -->
    <arch>x86</arch>
    <!--激活profile的操作系统版本 -->
    <version>5.1.2600</version>
  </os>
  <!--如果Maven检测到某一个属性（其值可以在POM中通过${name}引用），其拥有对应的name = 值，Profile就会被激活。如果值字段是空的，那么存在属性名称字段就会激活profile，否则按区分大小写方式匹配属性值字段 -->
  <property>
    <!--激活profile的属性的名称 -->
    <name>mavenVersion</name>
    <!--激活profile的属性的值 -->
    <value>2.0.3</value>
  </property>
  <!--提供一个文件名，通过检测该文件的存在或不存在来激活profile。missing检查文件是否存在，如果不存在则激活profile。另一方面，exists则会检查文件是否存在，如果存在则激活profile。 -->
  <file>
    <!--如果指定的文件存在，则激活profile。 -->
    <exists>${basedir}/file2.properties</exists>
    <!--如果指定的文件不存在，则激活profile。 -->
    <missing>${basedir}/file1.properties</missing>
  </file>
</activation>
```

1.10 activeProfiles

手动激活profiles的列表，按照profile被定义的顺序定义activeProfile
该元素包含一组activeProfile元素，每个activeProfile都有一个profileId，只要定义，不论环境如何，其对应的profile都会被激活

### maven仓库的概念

* 仓库：统一存放jar包的工具

仓库分三类：本地仓库、私服（镜像仓库）、中央仓库

#### 本地仓库

maven会把所有工程依赖的jar包从远程下载到本机的一个目录下管理，默认仓库是在用户目录/.m2/repository下

> 通常会修改本地仓库的位置，每次项目引入依赖时都会先从本地仓库找，如果找不到再从中央仓库或者镜像仓库找

#### 私服

也称为镜像仓库

通常情况为公司设立，只供给公司内部使用，保证项目开发时，所有人的版本都能保持一致（搭建maven私服的开源软件：nexus）

也有对外开放的镜像仓库，如阿里云镜像：http://maven.aliyun.com/nexus/content/groups/public

#### 中央仓库

maven内置的远程共用仓库：http://repo1.maven.org/maven2，这个仓库是由maven自己维护，里面有大量的常用类库，并包含了世界上大部分流行的开源项目构建

### maven的生命周期

* maven的生命周期阶段是指项目的构建过程，它包含一系列有序的阶段，而一个阶段就是项目构建过程中的一个步骤，其中
包含项目的清理、初始化、编译、测试、打包、集成测试、验证、部署和站点生成

* maven的生命周期是抽象的，即生命周期不做任何实际的工作，实际任务都由插件来完成；插件目标可以绑定到生命周期阶段，
一个生命周期阶段可以绑定多个插件目标

* default lifecycle 默认生命周期 phase 阶段  plugins 插件 goal 目标

> 插件目标和生命周期阶段绑定，一个阶段可以绑定零个或多个目标，每个插件提供一个或多个目标

* 如果不添加插件，maven会默认指定一套和生命周期绑定的插件

#### maven标准生命周期阶段

* clean 

1) pre-clean：执行清理前需要完成的工作
2) clean：清理上一次构建时的文件
3) post-clean：执行清理后需要完成的工作

* default(or build)

1) validate：验证工程是否正确，所需要的资源是否可用
2) initialize：初始化构建环境
3) generate-sources
4) process-sources
5) generate-resources
6) process-resources
7) compile：编译项目源代码
8) process-classes
9) generate-test-sources
10) process-test-sources
11) generate-test-resources
12) process-test-resources
13) test-compile
14) process-test-classes
15) test：使用合适的单元测试框架来测试已编译的源代码
16) prepare-package
17) package：把已编译的代码打包成可以发布的格式，例如jar
18) pre-integration-test
19) integration-test：如有需要，处理包并发布到一个能继承测试的环境
20) post-integration-test
21) verify：运行所有检查，验证包是否有效且达到质量标准
22) install：把包安装到maven本地仓库，可以被其他工程作为依赖使用
23) deploy：在继承或者发布环境下执行，将最终版本的包拷贝到远程的repository，使得其他开发者和工程可以共享

* site 

1) pre-site：生成项目站点之前需要完成的工作
2) site：生成项目站点文档
3) post-site：生成项目站点之后需要完成的工作
4) site-deploy：将项目站点发布到服务器

### maven依赖管理

#### pom.xml文件

pom.xml文件主要描述了项目的maven坐标，依赖关系，开发者需要遵循的规则、组织和licenses，是项目级别的配置文件
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
   <!-- 模型版本 -->
   <modelVersion>4.0.0</modelVersion>
   <!-- The Basics 
   <groupId>...</groupId>：一般使用该项目的组织或者团体的域名来标识，例如：org.apache.maven.plugins -->
   <artifactId>...</artifactId>：代表唯一的工程名
   <version>...</version>：版本号
   <packaging>...</packaging>：标识打包类型，例如：jar/war/pom，默认为jar
   <dependencies>...</dependencies>：定义项目的依赖关系
   <parent>
        <groupId>...</groupId>：一般使用该项目的组织或者团体的域名来标识，例如：org.apache.maven.plugins
        <artifactId>...</artifactId>：代表唯一的工程名
        <version>...</version>：版本号
   </parent>
   <dependencyManagement>...</dependencyManagement>：依赖管理
   <modules>...</modules>：模块
   <properties>...</properties>：配置项
  
   <!-- Build Settings -->
   <build>...</build>
   <reporting>...</reporting>
  
   <!-- More Project Information -->
   <name>...</name>
   <description>...</description>
   <url>...</url>
   <inceptionYear>...</inceptionYear>
   <licenses>...</licenses>
   <organization>...</organization>
   <developers>...</developers>
   <contributors>...</contributors>
  
   <!-- Environment Settings -->
   <issueManagement>...</issueManagement>
   <ciManagement>...</ciManagement>
   <mailingLists>...</mailingLists>
   <scm>...</scm>
   <prerequisites>...</prerequisites>
   <repositories>...</repositories>
   <pluginRepositories>...</pluginRepositories>
   <distributionManagement>...</distributionManagement>
   <profiles>...</profiles>
</project>
```

* 在pom文件中groupId/artifactId/version是必不可少的标签，这三项是描述一个maven项目的基础信息，通常路径为：groupId:artifactId:version
也可以称为坐标，是一个依赖jar的唯一标识

* pom.xml中的一些默认配置项

> ${project.basedir}：表示根路径
>
> ${project.build.directory}：打包路径文件文件

* 常见配置属性

classifier：用来帮助定义构件输出的一些附属构件，附属构件与主构件对应
type：依赖类型，比如是jar包还是war包或者pom，默认是jar包
optional：是否可选，默认值为false（如果A依赖B，B依赖D，B对D的依赖的optional为true时，A不依赖D，必须显示引入）
exclusions：排除传递依赖，解决jar冲突问题
scope：作用域，依赖范围，在哪个范围内使用生效
> compile：默认值，表示编译、测试和运行都使用当前jar
>
> test：表示只在测试时当前jar生效
>
> runtime：表示测试和运行时使用当前jar，编译时不用该jar
>
> provided：表示编译时和测试时使用当前jar
>
> system：表示手动添加的jar包

##### 构件配置

```xml
<build>  
  
    <!-- 产生的构件的文件名，默认值是${artifactId}-${version}。 -->  
    <finalName>myProjectName</finalName>  
  
    <!-- 构建产生的所有文件存放的目录,默认为${basedir}/target，即项目根目录下的target -->  
    <directory>${basedir}/target</directory>  
  
    <!--当项目没有规定目标（Maven2叫做阶段（phase））时的默认值， -->  
    <!--必须跟命令行上的参数相同例如jar:jar，或者与某个阶段（phase）相同例如install、compile等 -->  
    <defaultGoal>install</defaultGoal>  
  
    <!--当filtering开关打开时，使用到的过滤器属性文件列表。 -->  
    <!--项目配置信息中诸如${spring.version}之类的占位符会被属性文件中的实际值替换掉 -->  
    <filters>  
        <filter>../filter.properties</filter>  
    </filters>  
  
    <!--项目相关的所有资源路径列表，例如和项目相关的配置文件、属性文件，这些资源被包含在最终的打包文件里。 -->  
    <resources>  
        <resource>  
  
            <!--描述了资源的目标路径。该路径相对target/classes目录（例如${project.build.outputDirectory}）。 -->  
            <!--举个例子，如果你想资源在特定的包里(org.apache.maven.messages)，你就必须该元素设置为org/apache/maven/messages。 -->  
            <!--然而，如果你只是想把资源放到源码目录结构里，就不需要该配置。 -->  
            <targetPath>resources</targetPath>  
  
            <!--是否使用参数值代替参数名。参数值取自properties元素或者文件里配置的属性，文件在filters元素里列出。 -->  
            <filtering>true</filtering>  
  
            <!--描述存放资源的目录，该路径相对POM路径 -->  
            <directory>src/main/resources</directory>  
  
            <!--包含的模式列表 -->  
            <includes>  
                <include>**/*.properties</include>  
                <include>**/*.xml</include>  
            </includes>  
  
            <!--排除的模式列表 如果<include>与<exclude>划定的范围存在冲突，以<exclude>为准 -->  
            <excludes>  
                <exclude>jdbc.properties</exclude>  
            </excludes>  
  
        </resource>  
    </resources>  
  
    <!--单元测试相关的所有资源路径，配制方法与resources类似 -->  
    <testResources>  
        <testResource>  
            <targetPath />  
            <filtering />  
            <directory />  
            <includes />  
            <excludes />  
        </testResource>  
    </testResources>  
  
    <!--项目源码目录，当构建项目的时候，构建系统会编译目录里的源码。该路径是相对于pom.xml的相对路径。 -->  
    <sourceDirectory>${basedir}\src\main\java</sourceDirectory>  
  
    <!--项目脚本源码目录，该目录和源码目录不同， 绝大多数情况下，该目录下的内容会被拷贝到输出目录(因为脚本是被解释的，而不是被编译的)。 -->  
    <scriptSourceDirectory>${basedir}\src\main\scripts  
    </scriptSourceDirectory>  
  
    <!--项目单元测试使用的源码目录，当测试项目的时候，构建系统会编译目录里的源码。该路径是相对于pom.xml的相对路径。 -->  
    <testSourceDirectory>${basedir}\src\test\java</testSourceDirectory>  
  
    <!--被编译过的应用程序class文件存放的目录。 -->  
    <outputDirectory>${basedir}\target\classes</outputDirectory>  
  
    <!--被编译过的测试class文件存放的目录。 -->  
    <testOutputDirectory>${basedir}\target\test-classes  
    </testOutputDirectory>  
  
    <!--项目的一系列构建扩展,它们是一系列build过程中要使用的产品，会包含在running build‘s classpath里面。 -->  
    <!--他们可以开启extensions，也可以通过提供条件来激活plugins。 -->  
    <!--简单来讲，extensions是在build过程被激活的产品 -->  
    <extensions>  
  
        <!--例如，通常情况下，程序开发完成后部署到线上Linux服务器，可能需要经历打包、 -->  
        <!--将包文件传到服务器、SSH连上服务器、敲命令启动程序等一系列繁琐的步骤。 -->  
        <!--实际上这些步骤都可以通过Maven的一个插件 wagon-maven-plugin 来自动完成 -->  
        <!--下面的扩展插件wagon-ssh用于通过SSH的方式连接远程服务器， -->  
        <!--类似的还有支持ftp方式的wagon-ftp插件 -->  
        <extension>  
            <groupId>org.apache.maven.wagon</groupId>  
            <artifactId>wagon-ssh</artifactId>  
            <version>2.8</version>  
        </extension>  
  
    </extensions>  
  
    <!--使用的插件列表 。 -->  
    <plugins>  
        <plugin>  
            <groupId>/</groupId>  
            <artifactId>maven-assembly-plugin</artifactId>  
            <version>2.5.5</version>  
  
            <!--在构建生命周期中执行一组目标的配置。每个目标可能有不同的配置。 -->  
            <executions>  
                <execution>  
  
                    <!--执行目标的标识符，用于标识构建过程中的目标，或者匹配继承过程中需要合并的执行目标 -->  
                    <id>assembly</id>  
  
                    <!--绑定了目标的构建生命周期阶段，如果省略，目标会被绑定到源数据里配置的默认阶段 -->  
                    <phase>package</phase>  
  
                    <!--配置的执行目标 -->  
                    <goals>  
                        <goal>single</goal>  
                    </goals>  
  
                    <!--配置是否被传播到子POM -->  
                    <inherited>false</inherited>  
  
                </execution>  
            </executions>  
  
            <!--作为DOM对象的配置,配置项因插件而异 -->  
            <configuration>  
                <finalName>${finalName}</finalName>  
                <appendAssemblyId>false</appendAssemblyId>  
                <descriptor>assembly.xml</descriptor>  
            </configuration>  
  
            <!--是否从该插件下载Maven扩展（例如打包和类型处理器）， -->  
            <!--由于性能原因，只有在真需要下载时，该元素才被设置成true。 -->  
            <extensions>false</extensions>  
  
            <!--项目引入插件所需要的额外依赖 -->  
            <dependencies>  
                <dependency>...</dependency>  
            </dependencies>  
  
            <!--任何配置是否被传播到子项目 -->  
            <inherited>true</inherited>  
  
        </plugin>  
    </plugins>  
  
    <!--主要定义插件的共同元素、扩展元素集合，类似于dependencyManagement， -->  
    <!--所有继承于此项目的子项目都能使用。该插件配置项直到被引用时才会被解析或绑定到生命周期。 -->  
    <!--给定插件的任何本地配置都会覆盖这里的配置 -->  
    <pluginManagement>  
        <plugins>...</plugins>  
    </pluginManagement>  
  
</build>  
```

##### 分发配置

```xml
<!--项目分发信息，在执行mvn deploy后表示要发布的位置。 -->  
<!--有了这些信息就可以把网站部署到远程服务器或者把构件部署到远程仓库。 -->  
<distributionManagement>  
  
    <!--部署项目产生的构件到远程仓库需要的信息 -->  
    <repository>  
  
        <!--是分配给快照一个唯一的版本号（由时间戳和构建流水号），还是每次都使用相同的版本号 -->  
        <!--参见repositories/repository元素 -->  
        <uniqueVersion>true</uniqueVersion>  
  
        <id> repo-id </id>  
        <name> repo-name</name>  
        <url>file://${basedir}/target/deploy </url>  
        <layout />  
  
    </repository>  
  
    <!--构件的快照部署到哪里,如果没有配置该元素，默认部署到repository元素配置的仓库 -->  
    <snapshotRepository>  
        <uniqueVersion />  
        <id />  
        <name />  
        <url />  
        <layout />  
    </snapshotRepository>  
  
    <!--部署项目的网站需要的信息 -->  
    <site>  
  
        <!--部署位置的唯一标识符，用来匹配站点和settings.xml文件里的配置 -->  
        <id> site-id </id>  
  
        <!--部署位置的名称 -->  
        <name> site-name</name>  
  
        <!--部署位置的URL，按protocol://hostname/path形式 -->  
        <url>scp://svn.baidu.com/banseon:/var/www/localhost/banseon-web </url>  
  
    </site>  
  
    <!--项目下载页面的URL。如果没有该元素，用户应该参考主页。 -->  
    <!--使用该元素的原因是：帮助定位那些不在仓库里的构件（由于license限制）。 -->  
    <downloadUrl />  
  
    <!--如果构件有了新的groupID和artifact ID（构件移到了新的位置），这里列出构件的重定位信息。 -->  
    <relocation>  
  
        <!--构件新的group ID -->  
        <groupId />  
  
        <!--构件新的artifact ID -->  
        <artifactId />  
  
        <!--构件新的版本号 -->  
        <version />  
  
        <!--显示给用户的，关于移动的额外信息，例如原因。 -->  
        <message />  
  
    </relocation>  
  
    <!--给出该构件在远程仓库的状态。不得在本地项目中设置该元素，因为这是工具自动更新的。 -->  
    <!--有效的值有：none（默认），converted（仓库管理员从Maven 1 POM转换过来）， -->  
    <!--partner（直接从伙伴Maven 2仓库同步过来），deployed（从Maven 2实例部署），verified（被核实时正确的和最终的）。 -->  
    <status />  
  
</distributionManagement>
```

##### 仓库配置

###### 构件仓库

```xml
<!--发现依赖和扩展的远程仓库列表。 -->  
<repositories>  
  
    <!--包含需要连接到远程仓库的信息 -->  
    <repository>  
  
        <!--如何处理远程仓库里发布版本的下载 -->  
        <releases>  
  
            <!--true或者false表示该仓库是否为下载某种类型构件（发布版，快照版）开启。 -->  
            <enabled />  
  
            <!--该元素指定更新发生的频率。Maven会比较本地POM和远程POM的时间戳。 -->  
            <!--这里的选项是：always（一直），daily（默认，每日）， -->  
            <!--interval：X（这里X是以分钟为单位的时间间隔），或者never（从不）。 -->  
            <updatePolicy />  
  
            <!--当Maven验证构件校验文件失败时该怎么做： -->  
            <!--ignore（忽略），fail（失败），或者warn（警告）。 -->  
            <checksumPolicy />  
  
        </releases>  
  
        <!--如何处理远程仓库里快照版本的下载。有了releases和snapshots这两组配置， -->  
        <!--POM就可以在每个单独的仓库中，为每种类型的构件采取不同的策略。 -->  
        <!--例如，可能有人会决定只为开发目的开启对快照版本下载的支持 -->  
        <snapshots>  
            <enabled />  
            <updatePolicy />  
            <checksumPolicy />  
        </snapshots>  
  
        <!--远程仓库唯一标识符。可以用来匹配在settings.xml文件里配置的远程仓库 -->  
        <id> repo-id </id>  
  
        <!--远程仓库名称 -->  
        <name> repo-name</name>  
  
        <!--远程仓库URL，按protocol://hostname/path形式 -->  
        <url>http://hostname:prot/repository/ </url>  
  
        <!--用于定位和排序构件的仓库布局类型-可以是default（默认）或者legacy（遗留）。 -->  
        <!--Maven 2为其仓库提供了一个默认的布局； -->  
        <!--然而，Maven1.x有一种不同的布局。 -->  
        <!--我们可以使用该元素指定布局是default（默认）还是legacy（遗留）。 -->  
        <layout> default</layout>  
  
    </repository>  
  
</repositories>    
```

###### 插件仓库

```xml
<!--发现插件的远程仓库列表，这些插件用于构建和报表 -->  
<pluginRepositories>  
    <!--包含需要连接到远程插件仓库的信息.参见repositories/repository元素 -->  
    <pluginRepository />  
  
</pluginRepositories>
```

##### profile配置

```xml
<!--在列的项目构建profile，如果被激活，会修改构建处理 -->  
<profiles>  
  
    <!--根据环境参数或命令行参数激活某个构建处理 -->  
    <profile>  
        <!--自动触发profile的条件逻辑。Activation是profile的开启钥匙。 -->  
        <activation>  
  
            <!--profile默认是否激活的标识 -->  
            <activeByDefault>false</activeByDefault>  
  
            <!--activation有一个内建的java版本检测，如果检测到jdk版本与期待的一样，profile被激活。 -->  
            <jdk>1.7</jdk>  
  
            <!--当匹配的操作系统属性被检测到，profile被激活。os元素可以定义一些操作系统相关的属性。 -->  
            <os>  
  
                <!--激活profile的操作系统的名字 -->  
                <name>Windows XP</name>  
  
                <!--激活profile的操作系统所属家族(如 'windows') -->  
                <family>Windows</family>  
  
                <!--激活profile的操作系统体系结构 -->  
                <arch>x86</arch>  
  
                <!--激活profile的操作系统版本 -->  
                <version>5.1.2600</version>  
  
            </os>  
  
            <!--如果Maven检测到某一个属性（其值可以在POM中通过${名称}引用），其拥有对应的名称和值，Profile就会被激活。 -->  
            <!-- 如果值字段是空的，那么存在属性名称字段就会激活profile，否则按区分大小写方式匹配属性值字段 -->  
            <property>  
  
                <!--激活profile的属性的名称 -->  
                <name>mavenVersion</name>  
  
                <!--激活profile的属性的值 -->  
                <value>2.0.3</value>  
  
            </property>  
  
            <!--提供一个文件名，通过检测该文件的存在或不存在来激活profile。missing检查文件是否存在，如果不存在则激活profile。 -->  
            <!--另一方面，exists则会检查文件是否存在，如果存在则激活profile。 -->  
            <file>  
  
                <!--如果指定的文件存在，则激活profile。 -->  
                <exists>/usr/local/hudson/hudson-home/jobs/maven-guide-zh-to-production/workspace/</exists>  
  
                <!--如果指定的文件不存在，则激活profile。 -->  
                <missing>/usr/local/hudson/hudson-home/jobs/maven-guide-zh-to-production/workspace/</missing>  
  
            </file>  
  
        </activation>  
        <id />  
        <build />  
        <modules />  
        <repositories />  
        <pluginRepositories />  
        <dependencies />  
        <reporting />  
        <dependencyManagement />  
        <distributionManagement />  
        <properties />  
    </profile> 
</profiles>
```

##### 报表配置

```xml
<!--描述使用报表插件产生报表的规范,特定的maven 插件能输出相应的定制和配置报表. -->  
<!--当用户执行“mvn site”，这些报表就会运行,在页面导航栏能看到所有报表的链接。 -->  
<reporting>    
    <!--true，则网站不包括默认的报表。这包括“项目信息”菜单中的报表。 -->  
    <excludeDefaults />    
    <!--所有产生的报表存放到哪里。默认值是${project.build.directory}/site。 -->  
    <outputDirectory />   
    <!--使用的报表插件和他们的配置。 -->  
    <plugins>    
        <plugin>  
            <groupId />  
            <artifactId />  
            <version />  
            <inherited />  
            <configuration>  
                <links>  
                    <link>http://java.sun.com/j2se/1.5.0/docs/api/</link>  
                </links>  
            </configuration>  
            <!--一组报表的多重规范，每个规范可能有不同的配置。 -->  
            <!--一个规范（报表集）对应一个执行目标 。例如，有1，2，3，4，5，6，7，8，9个报表。 -->  
            <!--1，2，5构成A报表集，对应一个执行目标。2，5，8构成B报表集，对应另一个执行目标 -->  
            <reportSets>    
                <!--表示报表的一个集合，以及产生该集合的配置 -->  
                <reportSet>    
                    <!--报表集合的唯一标识符，POM继承时用到 -->  
                    <id>sun-link</id>   
                    <!--产生报表集合时，被使用的报表的配置 -->  
                    <configuration />   
                    <!--配置是否被继承到子POMs -->  
                    <inherited />   
                    <!--这个集合里使用到哪些报表 -->  
                    <reports>  
                        <report>javadoc</report>  
                    </reports>    
                </reportSet>   
            </reportSets>  
        </plugin>  
    </plugins>  
</reporting>     
```

##### 环境配置

```xml
<!--项目的问题管理系统(Bugzilla, Jira, Scarab,或任何你喜欢的问题管理系统)的名称和URL，本例为 jira -->  
<issueManagement>    
    <!--问题管理系统（例如jira）的名字， -->  
    <system> jira </system>    
    <!--该项目使用的问题管理系统的URL -->  
    <url> http://jira.clf.com/</url>   
</issueManagement>  
```
```xml
<!--项目持续集成信息 -->  
<ciManagement>    
    <!--持续集成系统的名字，例如continuum -->  
    <system />    
    <!--该项目使用的持续集成系统的URL（如果持续集成系统有web接口的话）。 -->  
    <url />    
    <!--构建完成时，需要通知的开发者/用户的配置项。包括被通知者信息和通知条件（错误，失败，成功，警告） -->  
    <notifiers>   
        <!--配置一种方式，当构建中断时，以该方式通知用户/开发者 -->  
        <notifier>    
            <!--传送通知的途径 -->  
            <type />    
            <!--发生错误时是否通知 -->  
            <sendOnError />    
            <!--构建失败时是否通知 -->  
            <sendOnFailure />    
            <!--构建成功时是否通知 -->  
            <sendOnSuccess />    
            <!--发生警告时是否通知 -->  
            <sendOnWarning />    
            <!--不赞成使用。通知发送到哪里 -->  
            <address />    
            <!--扩展配置项 -->  
            <configuration />    
        </notifier>    
    </notifiers>    
</ciManagement>  
```

##### 项目信息配置

```xml
<project>
    <!--项目的名称, Maven产生的文档用 -->  
    <name>banseon-maven </name>        
    <!--项目主页的URL, Maven产生的文档用 -->  
    <url>http://www.clf.com/ </url>        
    <!--项目的详细描述, Maven 产生的文档用。 当这个元素能够用HTML格式描述时 -->  
    <!--（例如，CDATA中的文本会被解析器忽略，就可以包含HTML标签），不鼓励使用纯文本描述。 -->  
    <!-- 如果你需要修改产生的web站点的索引页面，你应该修改你自己的索引页文件，而不是调整这里的文档。 -->  
    <description>A maven project to study maven. </description>        
    <!--描述了这个项目构建环境中的前提条件。 -->  
    <prerequisites>       
        <!--构建该项目或使用该插件所需要的Maven的最低版本 -->  
        <maven />        
    </prerequisites>        
    <!--项目创建年份，4位数字。当产生版权信息时需要使用这个值。 -->  
    <inceptionYear />  
</project>
```
```xml  
<!--项目相关邮件列表信息 -->  
<mailingLists>    
    <!--该元素描述了项目相关的所有邮件列表。自动产生的网站引用这些信息。 -->  
    <mailingList>    
        <!--邮件的名称 -->  
        <name> Demo </name>   
        <!--发送邮件的地址或链接，如果是邮件地址，创建文档时，mailto: 链接会被自动创建 -->  
        <post> clf@126.com</post>    
        <!--订阅邮件的地址或链接，如果是邮件地址，创建文档时，mailto: 链接会被自动创建 -->  
        <subscribe> clf@126.com</subscribe>   
        <!--取消订阅邮件的地址或链接，如果是邮件地址，创建文档时，mailto: 链接会被自动创建 -->  
        <unsubscribe> clf@126.com</unsubscribe>    
        <!--你可以浏览邮件信息的URL -->  
        <archive> http:/hi.clf.com/</archive>    
    </mailingList>    
</mailingLists>  
```
```xml  
<!--项目开发者列表 -->  
<developers>    
    <!--某个项目开发者的信息 -->  
    <developer>   
        <!--SCM里项目开发者的唯一标识符 -->  
        <id> HELLO WORLD </id>    
        <!--项目开发者的全名 -->  
        <name> banseon </name>    
        <!--项目开发者的email -->  
        <email> banseon@126.com</email>    
        <!--项目开发者的主页的URL -->  
        <url />    
        <!--项目开发者在项目中扮演的角色，角色元素描述了各种角色 -->  
        <roles>  
            <role> Project Manager</role>  
            <role>Architect </role>  
        </roles>    
        <!--项目开发者所属组织 -->  
        <organization> demo</organization>    
        <!--项目开发者所属组织的URL -->  
        <organizationUrl>http://hi.clf.com/ </organizationUrl>    
        <!--项目开发者属性，如即时消息如何处理等 -->  
        <properties>  
            <dept> No </dept>  
        </properties>    
        <!--项目开发者所在时区， -11到12范围内的整数。 -->  
        <timezone> -5</timezone>    
    </developer>    
</developers>  
```
```xml
<!--项目的其他贡献者列表 -->  
<contributors>  
    <!--项目的其他贡献者。参见developers/developer元素 -->  
    <contributor>  
        <name />  
        <email />  
        <url />  
        <organization />  
        <organizationUrl />  
        <roles />  
        <timezone />  
        <properties />  
    </contributor>    
</contributors>  
```
```xml  
<!--该元素描述了项目所有License列表。应该只列出该项目的license列表，不要列出依赖项目的license列表。 -->  
<!--如果列出多个license，用户可以选择它们中的一个而不是接受所有license。 -->  
<licenses>   
    <!--描述了项目的license，用于生成项目的web站点的license页面，其他一些报表和validation也会用到该元素。 -->  
    <license>    
        <!--license用于法律上的名称 -->  
        <name> Apache 2 </name>    
        <!--官方的license正文页面的URL -->  
        <url>http://www.clf.com/LICENSE-2.0.txt </url>    
        <!--项目分发的主要方式： repo，可以从Maven库下载 manual， 用户必须手动下载和安装依赖 -->  
        <distribution> repo</distribution>    
        <!--关于license的补充信息 -->  
        <comments> Abusiness-friendly OSS license </comments>   
    </license>    
</licenses>  
```
```xml  
<!--SCM(Source Control Management)标签允许你配置你的代码库，供Maven web站点和其它插件使用。 -->  
<scm>    
    <!--SCM的URL,该URL描述了版本库和如何连接到版本库。欲知详情，请看SCMs提供的URL格式和列表。该连接只读。 -->  
    <connection>scm:svn:http://svn.baidu.com/banseon/maven/</connection>    
    <!--给开发者使用的，类似connection元素。即该连接不仅仅只读 -->  
    <developerConnection>scm:svn:http://svn.baidu.com/banseon/maven/  
    </developerConnection>    
    <!--当前代码的标签，在开发阶段默认为HEAD -->  
    <tag />    
    <!--指向项目的可浏览SCM库（例如ViewVC或者Fisheye）的URL。 -->  
    <url> http://svn.baidu.com/banseon</url>    
</scm>  
```
```xml 
<!--描述项目所属组织的各种属性。Maven产生的文档用 -->  
<organization>    
    <!--组织的全名 -->  
    <name> demo </name>    
    <!--组织主页的URL -->  
    <url> http://www.clf.com/</url>    
</organization>   
```

#### 依赖管理

* 在pom引入依赖只需添加dependency标签，在里面指定坐标即可

* maven可以解析jar包依赖-传递依赖

> 当引入指定jar包时，引入的jar包内部同时还依赖其他jar包，且依赖jar也依赖其他jar，依赖关系不断传递，直至没有依赖

* 依赖包冲突

> 按照maven依赖原则，工程内可能会引入同一jar包的不同版本

* 包冲突解决：

> maven解析pom.xml文件时，同一个jar包只会保留一个，这种方式可以有效避免因引入两个jar包导致的工程不稳定性

> maven默认处理策略：
>
> 最短路径优先：maven面对两个相同的jar时，会默认选择最短路径的jar包
>
> 最先声明优先：如果路径一样，则选择最先声明的jar包

* 依赖移除

> 如果不想产生冲突，可以在声明时通过exclusions标签把重复的依赖移除掉

* 检测包冲突工具

> mvn dependency:help
>
> mvn dependency:analyze
>
> mvn dependency:tree
>
> mvn dependency:tree -Dverbose

### maven常用打包方式总结

#### spring-boot-maven-plugin插件打包

* pom.xml文件配置

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <!-- 主类全路径 -->
                <mainClass>org.apache.maven.plugin.Application</mainClass>
                <executable>true</executable>
            </configuration>
        </plugin>
    </plugins>
</build>
```

#### maven-assembly-plugin插件打包

* pom.xml文件配置

```xml
<build>
    <plugins>
        <!-- 编译配置 -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
                <encoding>utf-8</encoding>
            </configuration>
        </plugin>
        <!-- 指定启动类，将依赖打成外部jar包 -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <version>3.1.2</version>
            <configuration>
                <archive>
                    <!-- 生成的jar中，不要包含pom.xml和pom.properties这两个文件 -->
                    <addMavenDescriptor>false</addMavenDescriptor>
                    <manifest>
                        <!-- 是否要把第三方jar放到manifest的classpath中 -->
                        <addClasspath>true</addClasspath>
                        <!-- classpathPrefix，manifest前加上的前缀路径 -->
                        <classpathPrefix>lib/</classpathPrefix>
                        <!-- 项目启动类 -->
                        <mainClass>org.apache.maven.plugin.Application</mainClass>
                    </manifest>
                </archive>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-assembly-plugin</artifactId>
            <version>3.1.1</version>
            <configuration>
                <descriptors>
                    <!-- assembly描述文件 -->
                    <descriptor>src/main/assembly/assembly.xml</descriptor>
                </descriptors>
            </configuration>
            <executions>
                <execution>
                    <id>make-assembly</id>
                    <!-- 生命周期阶段 -->
                    <phase>package</phase>
                    <goals>
                        <!-- 绑定插件目标 -->
                        <goal>single</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

#### maven-antrun-plugin插件打包

* pom.xml文件配置

```xml
<build>
    <plugins>
        <plugin>
            <artifactId>maven-antrun-plugin</artifactId>
            <executions>
                <execution>
                    <!-- 生命周期阶段 -->
                    <phase>package</phase>
                    <goals>
                        <!-- 绑定插件目标 -->
                        <goal>run</goal>
                    </goals>
                    <!-- 此处可以添加一些自定义打包设置，指定包含某些文件夹、文件，排除某些文件夹、文件等 -->
                    <configuration>
                        <tasks>
                            <echo>复制war到release目录</echo>
                            <copy todir="${project.deploy.directory}" overwrite="true">
                                <fileset dir="${basedir}/target">
                                    <include name="*.war" />
                                </fileset>
                            </copy>
                            <echo>复制发布文档到release目录的doc文件夹下</echo>
                            <mkdir dir="${project.deploy.directory}/conf/doc"/>
                            <copy todir="${project.deploy.directory}/conf/doc" overwrite="true">
                                <fileset dir="${basedir}/src/main/resources/doc">
                                    <include name="*" />
                                </fileset>
                            </copy>
                            <echo>复制sql脚本到release目录的sql文件夹下</echo>
                            <mkdir dir="${project.deploy.directory}/conf/sql/project-version-sql"/>
                            <copy todir="${project.deploy.directory}/conf/sql/project-version-sql" overwrite="true">
                                <fileset dir="${basedir}/src/main/resources/sql/project-version-sql">
                                    <include name="*.sql" />
                                </fileset>
                            </copy>
                        </tasks>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```





