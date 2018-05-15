# 一、Srping Boot入门

## 1、Spring Boot简介

> 简化 Spring应用开发的一个框架；
>
> 整个Spring技术站的一个大整合；
>
> J2EE开发的一站式解决方案；

## 2、微服务

2014，martin fowler提出

微服务：架构风格（服务微化）

一个应用应该是一组小型服务；可以通过HTTP的方式进行互通；



每一个功能元素最终都是一个可独立替换和可独立生级的软件单元；



详细参照微服务文档：http://blog.cuicc.com/blog/2015/07/22/microservices

##3、环境配置

环境约束：

-jdk1.8：java version"18_152"

-maven3.x：Apache Maven 3.5.0

-Intellij IDEA2017：Intellij IDEA 2.18.1.3 x64

-Spring Boot2.0.2.RELEASE：2.0.2

###1、maven设置；

给maven的settings.xml配置文件的profiles标签添加

```
<profile>
		<id>jdk-1.8</id>
		<activation>
			<activeByDefault>true</activeByDefault>
			<jdk>1.8</jdk>
		</activation>
		<properties>
			<maven.compiler.source>1.8</maven.compiler.source>
			<maven.compiler.target>1.8</maven.compiler.target>
			<maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
		</properties>
</profile>
```

### 2、Idea设置

![idea maven settings](E:\GitHub-Repositories\SpringBoot-learning\notes\images\idea maven settings)

### 4、Spring Boot HelloWorld

功能：浏览器发送hello请求，服务器接受请求并处理，相应Hello World字符串

####1、创建一个maven工程（jar）

####2、导入Spring Boot相关依赖

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.2.RELEASE</version>
</parent>
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

####3、编写一个主程序：用于启动Spring Boot应用

```java
/**
 * @SpringBootApplication 来标注一个主程序类，说明这是一个Spring Boot应用
 */
@SpringBootApplication
public class HelloWorldMainApplication {
    public static void main(String[] args) {

        //启动Spring Boot应用
        SpringApplication.run(HelloWorldMainApplication.class,args)；
    }
}
```

####4、编写相关的Controller、Service

```java
@Controller
public class HelloController {
	
    @ResponseBody
    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}
```

