# 一、Srping Boot入门

##1、Spring Boot简介

> 简化 Spring应用开发的一个框架；
>
> 整个Spring技术站的一个大整合；
>
> J2EE开发的一站式解决方案；

##2、微服务

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

###2、Idea设置

```
File | Settings | Build, Execution, Deployment | Build Tools | Maven
Maven home directory、User Settings file、Local repository
分别进行个性化配置
```

### ##4、Spring Boot HelloWorld

功能：浏览器发送hello请求，服务器接受请求并处理，相应Hello World字符串

###1、创建一个maven工程（jar）

###2、导入Spring Boot相关依赖

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

###3、编写一个主程序：用于启动Spring Boot应用

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

###4、编写相关的Controller、Service

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

###5、运行主程序测试

````html
http://localhost:8080/hello
````

###6、简化部署工作

```xml
<!-- 这个插件，可以将应用打包为一个可执行的jar包 -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

将这个应用打成jar包，直接运用`java -jar` 的命令运行。

##5、Hello World研究

### 1、pom文件

#### 1、父项目

```xml
<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.2.RELEASE</version>
</parent>

<!-- 它的父项目是 -->
<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>2.0.2.RELEASE</version>
        <relativePath>../../spring-boot-dependencies</relativePath>
</parent>
<!-- 它真正用来管理Spring Boot应用里的所有依赖版本 -->
```

Spring Boot的版本仲裁中心；

以后我们导入依赖默认是不需要写版本（没有在dependencies里面管理的依赖自然需要声明版本号）；

#### 2、启动器

```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
</dependencies>
```

**spring-boot-starter**-**web**

​	spring-boot-starter：spring-boot场景启动器；帮我们导入了web模块正常运行所依赖的组件；

Spring Boot将所有的功能场景都抽取出来，做成一个个的starters（启动器），只需要在项目里面引入这些starter，相关场景的所有依赖都会导入进来。要用什么功能。酒倒入什么场景的启动器

### 2、主程序类，主入口类

```java
/**
 * @SpringBootApplication 来标注一个主程序类，说明这是一个Spring Boot应用
 */
@SpringBootApplication
public class HelloWorldMainApplication {
    public static void main(String[] args) {

        //启动Spring Boot应用
        SpringApplication.run(HelloWorldMainApplication.class,args);
    }
}

```

**@SpringBootApplication**：Spring Boot应用标注在某个类上，说明这个类是Spring Boot的主配置类，Spring Boot就应该运行这个类的main方法来启动Spring Boot应用。