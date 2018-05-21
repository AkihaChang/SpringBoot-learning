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

### 1、如何在系统中使用SLF4j（https://www.slf4j.org）

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

底层依赖关系：

![slf4j](https://github.com/AkihaChang/SpringBoot-learning/raw/master/notes/images/slf4j.png)

总结：

​	1、Spring Boot底层也是使用slf4j+logback的方式进行日志记录；

​	2、Spring Boot也把其他的日志都踢换成了slf4j；

​	3、中间替换包？

```java
protected Logger getSLF4JLogger(LogRecord record) {
        String name = record.getLoggerName();
        if (name == null) {
            name = UNKNOWN_LOGGER_NAME;
        }
        return LoggerFactory.getLogger(name);
    }
```

​	4、如果我们要引入其他框架，一定要把这个框架默认的日志依赖移除掉！

​	例如：Spring框架用的是commons-logging



**Spring Boot能自动适配所有的日志，而底层使用slf4j+logback的方式记录日志，引入其他框架的时候，只需要把整个框架依赖的日志框架排除掉。**



## 4、日志的使用

### 1、默认配置

Spring Boot默认帮我们配置好了日志

```java
//记录器
    Logger logger = LoggerFactory.getLogger(getClass());
    @Test
    public void contextLoads() {

        //日志级别：
        //由低到高：  trace<debug<info<warn<error
        //可以调整输出的日志级别；日志就只会在这个级别以及以后的高级别生效
        logger.trace("这是trace日志...");
        logger.debug("这是debug日志...");
        //Spring Boot默认给我们是info级别的，没有指定级别的就用Spring Boot默认规定的级别；root级别
        logger.info("这是info日志...");
        logger.warn("这是warn日志...");
        logger.error("这是error日志...");
    }
```

```xml
<!--
	日志输出格式：
		%d 表示日期时间，
		%thread 表示线程名，
		%-5level：级别从左显示5个字符宽度
		%logger{50} 表示logger名字最长50个字符，否则按照句点分割。
		%msg：日止消息，
		%n 是换行符
-->
%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n
```

Spring Boot修改日志的默认配置

```prop
logging.level.com.zzy=trace


# 不指定路径在当前项目下生成springboot.log日志
# 可以指定完整的路径
#logging.file=E:/springboot.log

# 在当前磁盘的根路径下创建spring文件夹里面的log文件夹；使用spring.log作为默认文件
logging.path=/spring/log

# 在控制台输出的日志格式
logging.pattern.console=%d{yyyy-MM-dd} [%thread] %-5level %logger{50} - %msg%n
# 指定文件中日志输出的格式
logging.pattern.file=%d{yyyy-MM-dd} [%thread] %-5level %logger{50} - %msg%n
```

| logging.file | logging.path | Example  | Description                      |
| ------------ | ------------ | -------- | -------------------------------- |
| (none)       | (none)       |          | 只在控制台输出                   |
| 指定文件名   | (none)       | my.log   | 输出日志到my.log                 |
| (none)       | 指定目录     | /var/log | 输出到指定目录的spring.log文件中 |

### 2、指定配置

给类路径下放上每个日志框架自己的配置文件即可；Spring Boot就不适用他默认配置的了