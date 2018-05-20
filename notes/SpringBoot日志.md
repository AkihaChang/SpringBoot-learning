# 一、日志

## 1、日志框架

市面上常见的日志框架：

JUL、JCL、Jboss-logging、logback、log4j、log4j2、slf4j

| 日志门面（日志抽象层）                                       | 日志实现                                       |
| ------------------------------------------------------------ | ---------------------------------------------- |
| ~~JCL（Jackarta Commons Logging）~~    SLF4j（Simple Logging Facade for Java） ~~jboss-logging~~ | Log4j JUL(java.util.logging)    Log4j2 Logback |

左边选一个门面（抽象层）、右边来选一个实现；

日志门面：SLF4j；

日志实现：Logback；



Spring Boot：底层是Spring框架，Spring框架默认是用JCL；而**Spring Boot选用SLF4j和logback**；



## 2、SLF4j使用

### 1、如何在系统中使用SLF4j

以后开发的时候，日志记录方法的调用，不应该直接调用日志的实现类，而是调用日志抽象层里面的方法；

给系统里面导入SLF4j的jar和logback的实现jar

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorld {
  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(HelloWorld.class);
    logger.info("Hello World");
  }
}
```

图示：

![SLF4j的各项使用方法](https://github.com/AkihaChang/SpringBoot-learning/raw/master/notes/images/concrete-bindings.png)

每一个日志的实现框架都有自己的配置文件。使用SLF4j后，**配置文件还是做成日志实现框架的配置文件**；

### 2、遗留问题

问题1（SLF4j）：Spring（commons-logging）、Hibernate（jboss-logging）、Mybatis、xxx，统一日志记录，即使是别的框架和我一起统一使用SLF4j输出？

![](https://github.com/AkihaChang/SpringBoot-learning/raw/master/notes/images/legacy.png)

**如何让系统中所有的日志都统一到SLF4j？**

1、将系统中其他日志框架先排除出去；

2、用中间包来替换原有的日志框架；

3、我们再来导入SLF4j其他的实现



## 3、Spring Boot日志关系

```xml
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Spring Boot使用它来做日志功能；

```xml
<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-logging</artifactId>
      <version>2.0.2.RELEASE</version>
      <scope>compile</scope>
</dependency>
```

