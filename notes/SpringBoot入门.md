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



### 4、Spring Boot HelloWorld

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



```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
    excludeFilters = {@Filter(
    type = FilterType.CUSTOM,
    classes = {TypeExcludeFilter.class}
), @Filter(
    type = FilterType.CUSTOM,
    classes = {AutoConfigurationExcludeFilter.class}
)}
)
public @interface EnableAutoConfiguration {
```

**@SpringBootApplication**：Spring Boot配置类

​	标注在某一个类上，表示这是一个Spring Boot的配置类；

​	**@Configuration**：配置类上来标注这个注解；

​		配置类 ------ 配置文件；配置类也是容器中的一个组件；**@Component**

**@EnableAutoConfiguration**：开启自动配置功能；

​	以前需要配置的东西，Spring Boot帮助我们自动配置；**@EnableAutoConfiguration**gaosuSpring Boot开启自动配置功能；这样自动配置才能生效；

```java
@AutoConfigurationPackage
@Import({AutoConfigurationImportSelector.class})
public @interface EnableAutoConfiguration {
```

​	**@AutoConfigurationPackage**：自动配置包

​		@Import({Registrar.class})：

​		Spring的底层注解@Import，给容器中导入一个组件；导入的组件由Registrar.class

​		**将主配置类（@SpringBootApplication标注的类）的所在包下面所有子包里面的所有组件扫描到Spring容器；**

​	**@Import(AutoConfigurationImportSelector.class)**；

​	给容器导入组件？

​	AutoConfigurationImportSelector：导入哪些组件的选择器；

​	将所有需要导入的组件以全类名的方式返回；这些组件就会被添加到容器中；

​	会给容器中导入非常多的自动配置类（xxxAutoConfiguration）；就是给容器中导入这个场景需要的所有组件，并配置好这些组件；

debug输出内容：

```java
configurations = {LinkedList@3008}  size = 109
 0 = "org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration"
 1 = "org.springframework.boot.autoconfigure.aop.AopAutoConfiguration"
 2 = "org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration"
 3 = "org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration"
 4 = "org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration"
 5 = "org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration"
 6 = "org.springframework.boot.autoconfigure.cloud.CloudAutoConfiguration"
 7 = "org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration"
 8 = "org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration"
 9 = "org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration"
 10 = "org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration"
```

有了自动配置类，免去我们手动编写配置注入功能组件等的工作；

```java
SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, classLoader)
```

Spring Boot在启动的时候从类路径下的**META-INF/spring.factories**中获取**EnableAutoConfiguration**指定的值，将这些值作为自动配置类导入到容器中，自动配置类就生效，帮我们进行自动配置工作；以前我们需要自己做的配置，自动配置类帮我们做了。

J2EE的整体整合和解决方案都在"spring-boot-autoconfigure-2.0.2.RELEASE.jar"；



##6、使用Spring Initializer快速创建Spring Boot项目

IDE都支持使用Spring的项目创建向导快速创建一个Spring Boot项目；

选择我们需要的模块；向导会联网创建Spring Boot项目；

默认生成的Spring Boot项目；

* 主程序已经生成好了，我们只需要编写自己的逻辑
* resources文件夹中目录结构：
  * static：保存所有的静态资源；如：js css images;
  * templates：保存所有的模板页面；（Spring Boot默认jar包嵌入式的Tomcat，默认不支持jsp页面）可以使用模板引擎（freemarker、thymeleaf）
  * application.properties：Spring Boot应用的配置文件；可以修改一些默认配置
