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

![SLF4j的各项使用方法](https://upload-images.jianshu.io/upload_images/7099290-4b28476ead1d7b1a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



