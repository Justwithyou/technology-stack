# 工作流

工作流：业务过程的部分或整体在计算机应用环境下的自动化（主要解决的是：使在多个参与者之间按照某种预定义的规则传递文档、信息或者任务
的过程自动进行，从而实现某个预期的业务目标，或者促使此目标的实现）

工作流管理系统：一个软件系统，它完成工作量的定义和管理，并按照在系统中预先定义的工作流逻辑进行工作流实例的进行

常见的工作流框架：Activity/JBPM/OSWorkflow/Workflow

工作流框架底层需要数据库支持，Activity需要23张表（底层使用mybatis操作数据库），JBPM需要18张表（底层使用hibernate操作数据库）

## Activity

Activity是在Alfresco发布的开源业务流程管理（BPM-Business-Process-Management）框架，覆盖了业务流程管理、工作流、服务协作等领域的一个开源的、灵活的、易扩展的可执行流程语言框架

Activity默认使用H2数据库，可替换
```xml
<dependencies>
    <!-- 
        如果使用以下方式引入，则需要指定仓库
        <repositories>
        	    <repository>
        	      <id>alfresco</id>
        	      <name>Activiti Releases</name>
        	      <url>https://artifacts.alfresco.com/nexus/content/repositories/activiti-releases/</url>
        	      <releases>
        	        <enabled>true</enabled>
        	      </releases>
        	    </repository>
        </repositories>
     -->
    <dependency>
        <groupId>org.activiti</groupId>
        <artifactId>activiti-engine</artifactId>
        <!-- 如果版本号不包含字母则需要注意是否要更换仓库地址 -->
        <version>7.1.175</version>
    </dependency>
    <dependency>
        <groupId>org.activiti</groupId>
        <artifactId>activiti-spring</artifactId>    
        <version>7.1.175</version>
    </dependency>
    <!--
        中央仓库的版本：
        <dependency>
            <groupId>org.activiti</groupId>
            <artifactId>activiti-engine</artifactId>
            <version>7.1.0.M5</version>
        </dependency>
        <dependency>
            <groupId>org.activiti</groupId>
            <artifactId>activiti-spring</artifactId>
            <version>7.1.0.M5</version>
        </dependency>
    -->
    <dependency>
          <groupId>com.h2database</groupId>
          <artifactId>h2</artifactId>
    </dependency>
    <!-- Activiti与SpringBoot整合 -->
    <!-- https://mvnrepository.com/artifact/org.activiti/activiti-spring-boot-starter -->
    <dependency>
        <groupId>org.activiti</groupId>
        <artifactId>activiti-spring-boot-starter</artifactId>
        <version>7.1.0.M5</version>
    </dependency>
</dependencies>
```