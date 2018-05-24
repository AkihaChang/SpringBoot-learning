# 一、配置

## 1、配置文件

Spring Boot使用一个全局配置文件,配置文件名是固定的

* appliation.properties
* application.yml

配置文件的作用：修改Spring Boot自动配置的默认值；Spring Boot在底层给我们配置好了；



## 2、YAML语法

**YAML**（YAML A Markup Language）

​	YAML A Markup Language：是一个标记语言

​	YAML isn't Markup Language：不是一个标记语言

标记语言：

​	以前的配置文件；大多数都是**xxx.xml**文件；

​	YAML：**以数据为中心**，比json、xml等更适合做配置文件；

​	YAML：配置例子

```yaml
server:
  port: 8081
```

### 1、基本语法

k：**(空格)**v：表示已对键值对**（空格必须有）**；

以空格的缩进来控制层级关系；只要是左对齐的一列数据，都是同一层级的

```yaml
server:
  port: 8081
  path: /hello
```



属性和值也是大小写敏感；

### 2、值得写法

#### 字面量：普通的值（数字、字符串、布尔）

​	k: v : 字面直接来写；

​	字符串默认不用加上单引号或者双引号；

​	**“”**：双引号；不会显示字符串里面的特殊字符；特殊字符会作为本身想表示的意思

​		例：name: "Tom \n Jerry"；输出：Tom 换行 Jerry

​	**‘’**：单引号；会显示特殊字符，特殊字符最终只是一个普通的字符串

​		例：name: 'Tom \n Jerry'；输出：Tom \n Jerry

#### 对象、Map（属性和值）（键值对）：

​	**k: v**：在下一行来写对象的属性和值得关系**（注意缩进）**

​		对象还是k: v的方式

```yaml
ship: 
 name: Missouri
 class: Battleship
```

行内写法：

```yaml
ship: {name: Missouri,class: Battleship}
```

#### 数组（List、Set）：

用**- 值**表示数组中的一个元素

```yaml
ships: 
 - Richelieu
 - Missouri
 - Enterprise
```

行内写法

```yaml
ships: [Richelieu,Missouri,Enterprise]
```



## 3、配置文件注入

### 1、使用yaml（yml）注入的方法

配置文件（yaml）：

```yaml
person:
    name: Tom
    age: 24
    birthday: 1995/1/1
    isMarried: false
    personMap: {k1: value1,k2: value2}
    personList:
     - AAA
     - BBB
    pet:
        name: 阿黄
        age: 10

```

JavaBean：

```java
/**
 * 将配置文件中的每一个属性的值，映射到这个组件中
 * @ConfigurationProperties：告诉Spring Boot将本类中的所有属性和配置文件中的相关属性进行绑定
 * prefix = "person"：将配置文件中哪个下面的所有属性进行一一映射
 *
 * 只有这个组件时容器中的组件，才能使用容器提供的@ConfigurationProperties功能
 *
 */
@Component
@ConfigurationProperties(prefix = "person")
public class Person {
    private String name;
    private int age;
    private Date birthday;
    private boolean isMarried;

    private Map<String,Object> personMap;
    private List<Object> personList;
    private Pet pet;

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", birthday=" + birthday +
                ", isMarried=" + isMarried +
                ", personMap=" + personMap +
                ", personList=" + personList +
                ", pet=" + pet +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean isMarried() {
        return isMarried;
    }

    public void setMarried(boolean married) {
        isMarried = married;
    }

    public Map<String, Object> getPersonMap() {
        return personMap;
    }

    public void setPersonMap(Map<String, Object> personMap) {
        this.personMap = personMap;
    }

    public List<Object> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Object> personList) {
        this.personList = personList;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}

```

在pom文件中导入配置文件处理器，以后编写配置就有提示了

```xml
<!-- 导入配置文件处理器，配置文件进行绑定时就会有提示 -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-configuration-processor</artifactId>
	<optional>true</optional>
</dependency>
```

### 2、使用properties的注入方法

另外：配置文件（.properties）：

>\#配置person的值
>
>\#idea的proeperties配置文件使用utf-8编码。此时解决乱码方法：在file->setting->editor->file encodings下，把transparent native-to-ascll conversion勾选上就行了
>
>person.name=Tom
>person.age= 20
>person.birthday=1995/1/1
>person.isMarried=false
>person.person-map.k1=value1
>person.person-map.k2=value2
>person.person-list=AA,BBB,CCCC
>person.pet.name=阿黄
>person.pet.age=10

### 3、@Value获取值和@ConfigurationProperties获取值的比较

|                           | @ConfigurationProperties | @Value     |
| ------------------------- | ------------------------ | ---------- |
| 功能                      | 批量注入配置文件中的属性 | 一个个指定 |
| 松散绑定（松散语法）      | 支持                     | 不支持     |
| SpEL9（Spring表达式语言） | 不支持                   | 支持       |
| JSR303数据校验            | 支持                     | 不支持     |
| 复杂类型封装              | 支持                     | 不支持     |

配置文件不管是yaml还是properties他们都能获取到值；

如果说，我们只是在某个业务逻辑中需要获取某项值，使用@Value

如果说，我们专门编写了一个javaBean来和配置文件映射，我们就直接使用@ConfigurationProperties

4、配置文件注入值数据校验

```java
/**
 * 将配置文件中的每一个属性的值，映射到这个组件中
 * @ConfigurationProperties：告诉Spring Boot将本类中的所有属性和配置文件中的相关属性进行绑定
 * prefix = "person"：将配置文件中哪个下面的所有属性进行一一映射
 *
 * 只有这个组件时容器中的组件，才能使用容器提供的@ConfigurationProperties功能
 *
 */
@Component
@ConfigurationProperties(prefix = "person")
@Validated
public class Person {

    /**
     * <bean class="Person">
     *     <property name="name" value="字面量/${key}从环境变量、配置文件中获取值/#{SpEL}"
     * </bean>
     *
     */


    //邮箱格式
    //@Email
    //@Value("$person.name")
    private String name;
    //@Value("#{11*2}")
    private int age;
    private Date birthday;
    //@Value("true")
    private boolean isMarried;

    //@Value("${person.maps}")
    private Map<String,Object> personMap;
    private List<Object> personList;
    private Pet pet;

```

### 4、@PropertySource&@ImportResource

**@PropertySource**：加载指定的配置文件；

```java
/**
 * 将配置文件中的每一个属性的值，映射到这个组件中
 * @ConfigurationProperties：告诉Spring Boot将本类中的所有属性和配置文件中的相关属性进行绑定
 * prefix = "person"：将配置文件中哪个下面的所有属性进行一一映射
 *
 * 只有这个组件时容器中的组件，才能使用容器提供的@ConfigurationProperties功能
 * @ConfigurationProperties：默认从全局配置文件中获取值
 */
@PropertySource(value = {"classpath:person.properties"})
@Component
@ConfigurationProperties(prefix = "person")
@Validated
public class Person {

    /**
     * <bean class="Person">
     *     <property name="name" value="字面量/${key}从环境变量、配置文件中获取值/#{SpEL}"
     * </bean>
     *
     */


    //邮箱格式
    //@Email
    //@Value("$person.name")
    private String name;
    //@Value("#{11*2}")
    private int age;
    private Date birthday;
    //@Value("true")
    private boolean isMarried;
```

**@ImportResource**：导入Spring的配置文件，让配置文件里面的内容生效；

Spring Boot里面没有Spring的配置文件，我恩自己编写的配置文件，也不能自动识别；

想让Spring的配置文件生效，加载进来；@ImportResource标注在一个配置类上

```java
@ImportResource("classpath:beans.xml")
//导入配置文件让其生效
```

不来编写Spring的配置文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="helloService" class="com.zzy.springbootdemo2config.service.HelloService"></bean>
</beans>
```

Spring Boot推荐给容器中添加组建的方式：推荐使用全注解的方式

1、配置类======Spring配置文件

2、使用@Bean给容易中添加组件

```java
/**
 * @Configuration：指明当前类是一个配置类；就是来替代之前的Spring配置文件
 *
 * 在配置文件中，使用"<bean></bean>"标签添加组件
 */
@Configuration
public class MyAppConfig {

    //将方法的返回值添加到容器中；容器中这个组件的默认id就是方法名
    @Bean
    public HelloService helloService(){
        System.out.println("配置类@Bean给容器中添加组件了！");
        return new HelloService();
    }
}
```



## 4、配置文件占位符

#### 1、随机数

```java
random.value、{random.Int}、${randon.long}
random.int(10)、{random.int[123456]}
```

#### 2、占位符获取之前的值，如果没有，可以使用:指定默认的值

```prop
person.name=Tom${random.uuid}
person.age= ${random.int}
person.birthday=1995/1/1
person.isMarried=false
person.person-map.k1=value1
person.person-map.k2=value2
person.person-list=AA,BBB,CCCC
person.pet.name=${person.hello:hello}阿黄
person.pet.age=10
```



## 5、Profile

### 1、多Profile文件

我们在主配置文件编写的时候，文件名可以使是 application-{profile}.properties/yml

默认使用applicatio.properties的配置；

### 2、yml支持多文档块方式

```yaml
server:
  port: 8081
spring:
  profiles:
    active: dev
---
server:
  port: 8082
spring:
  profiles: dev
---
server:
  port: 8083
spring:
  profiles: prod
---
```

### 3、激活指定profile

​	1、在配置文件中指定 **spring.profiles.active=dev**

​	2、命令行：

​		项目打包后，cmd输入：**java -jar springboot-demo2-config --spring.profiles.active=dev**

同样可以在测试的时候，配置传入的命令行参数（Run-->Edit Configuration-->Program arguments:--spring.profiles.active=prod）

​	3、虚拟机参数：

​		Run-->Edit Configuration-->Program arguments:-Dspring.profiles.active=dev



## 6、配置文件加载位置

Spring Boot启动会扫描以下位置的application.properties或者application.yml文件作为Spring Boot的默认文件

**-file:./config/**

**-file:./**

**-classpath:/config/**

**-classpath:/**

* 优先级由高到低，高优先级地配置会覆盖低优先级的配置；

* Spring Boot会从这四个位置全部加载主配置文件；**互补配置**；

* 我们还可以通过spring。config。location来改变默认的文件配置

  项目打包好以后，我们可以使用命令行参数的形式，启动项目的时候来指定配置文件的新位置，让加载的这些配置文件共同起作用形成互补配置；

  

## 7、外部配置文件的加载顺序
**Spring Boot也可以从以下位置加载配置；优先级从高到低；高优先级的配置会覆盖低优先级的配置，所有的配置会形成互补配置**

1、命令行参数

java -jar springboot-demo2-config2-0.0.1-SNAPSHOT.jar --server.port=8087 --server.context=path=/abc

多个配置用空格分开

2、来自java:comp/env的NDI属性

3、来自Java系统属性（System.getProperties()）

4、操作系统环境变量

5、RandomValuePropertySource配置的random.*属性值

**由jar包外向jar包内寻找；**

**优先加载带profile**

6、jar包外部的application-{profile}或application.yml（带spring.profile）配置文件

7、jar包内部的applicaiton-{profile}.properties或application.yml（带spring.profile）配置文件

**再来加载不带profile**

8、jar包外部的applicaiton.properties或application.yml（不带spring.profile）配置文件

9、jar包内部的applicaiton.properties或application.yml（不带spring.profile）配置文件

10、@Confighuration注解类上的@PropertySource

11、通过SpringApplication.setDefaultProperties指定的默认属性

以上列举比较常用的配置方法，所有支持的及配置加载来源请参考[官方文档](https://docs.spring.io/spring-boot/docs/2.0.2.RELEASE/reference/htmlsingle/#boot-features-external-config)。



## 8、自动配置的原理

配置文件到底能写什么？怎么写？自动配置原理；

[配置文件能配置的属性参照](https://docs.spring.io/spring-boot/docs/2.0.2.RELEASE/reference/htmlsingle/#appendix)

### 1、自动配置原理：

1、Srping Boot启动的时候加载主配置类，开启了自动配置功能**@EnableAutoConfiguration**

2、@EnableAutoConfiguration作用：

 - 利用EnableAutoConfigurationImportSelector给容器中导入一些组件；

 - 可以查看selectImports()方法的内容；

 - List<String> configurations = getCandidateConfigurations(annotationMetadata,      attributes);  //获取候选的配置

   - ```java
     SpringFactoriesLoader.loadFactoryNames();
     //扫描所有jar包类路径下的 META-INF/spring.factories
     //把扫描到的这些文件的内容包装成properties对象
     //从properties中获取到EnableAutoConfiguration.class类（类名）对应的值，把他们添加在容器中
     ```



​	将类路径下“**META-INF/spring.factories**”里面配置的所有**EnableAutoConfiguration**的值加入到容器中；

```properties
# Auto Configure
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,\
org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration,\
org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration,\
org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration,\
org.springframework.boot.autoconfigure.cloud.CloudAutoConfiguration,\
org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration,\
org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration,\
org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration,\
org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration,\
org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.cassandra.CassandraRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.couchbase.CouchbaseDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.couchbase.CouchbaseReactiveDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.couchbase.CouchbaseReactiveRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.couchbase.CouchbaseRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration,\
org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.ldap.LdapDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.ldap.LdapRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.mongo.MongoReactiveRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.neo4j.Neo4jRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.solr.SolrRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,\
org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration,\
org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration,\
org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration,\
org.springframework.boot.autoconfigure.elasticsearch.jest.JestAutoConfiguration,\
org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration,\
org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration,\
org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration,\
org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration,\
org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration,\
org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration,\
org.springframework.boot.autoconfigure.hazelcast.HazelcastJpaDependencyAutoConfiguration,\
org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration,\
org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration,\
org.springframework.boot.autoconfigure.influx.InfluxDbAutoConfiguration,\
org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration,\
org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration,\
org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,\
org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration,\
org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration,\
org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration,\
org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration,\
org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration,\
org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration,\
org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration,\
org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration,\
org.springframework.boot.autoconfigure.jsonb.JsonbAutoConfiguration,\
org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration,\
org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapAutoConfiguration,\
org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration,\
org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration,\
org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration,\
org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration,\
org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration,\
org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration,\
org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration,\
org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration,\
org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,\
org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration,\
org.springframework.boot.autoconfigure.reactor.core.ReactorCoreAutoConfiguration,\
org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,\
org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration,\
org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration,\
org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration,\
org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration,\
org.springframework.boot.autoconfigure.sendgrid.SendGridAutoConfiguration,\
org.springframework.boot.autoconfigure.session.SessionAutoConfiguration,\
org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientAutoConfiguration,\
org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration,\
org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration,\
org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration,\
org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration,\
org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration,\
org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration,\
org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration,\
org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration,\
org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration,\
org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration,\
org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration,\
org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration,\
org.springframework.boot.autoconfigure.websocket.reactive.WebSocketReactiveAutoConfiguration,\
org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration,\
org.springframework.boot.autoconfigure.websocket.servlet.WebSocketMessagingAutoConfiguration,\
org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration
```

每一个这样的 xxxAutoConfiguration类都是容器中的一个组件，都加入到容器中；用他们来做自动配置

3、每一个自动配置类进行自动配置功能

4、以**HttpEncodingAutoConfiguration（Http编码自动配置）**为例，解释自动配置原理

```java
@Configuration  //表示这是一个配置类，以前编写的配置文件一样，也可以给容器中添加组件
@EnableConfigurationProperties(HttpEncodingProperties.class)  //启用指定类的ConfigurationProperties功能；将配置文件中对应的值和HttpEncodingProperties绑定起来；并把HttpEncodingProperties加入到ioc容器中

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)  //Spring底层@Conditional注解，根据不同的条件，如果满足指定的条件，整个配置类里面的配置就会生效；（判断当前应用是否为web应用，如果是，当前配置类生效）

@ConditionalOnClass(CharacterEncodingFilter.class)  //当前项目有没有这个类（CharacterEncodingFilter：SprinngMVC中进行乱码解决的过滤器）

@ConditionalOnProperty(prefix = "spring.http.encoding", value = "enabled", matchIfMissing = true)  //判断配置文件中是否存在某个配置（spring.http.encoding.enabled）；如果不存在判断也是成立的
//及时我们配置文件中不配置spring.http.encoding.enabled=true，也是默认生效的；

public class HttpEncodingAutoConfiguration {
    private final HttpEncodingProperties properties;  //它已经和Spring Boot的配置文件映射了
	
    //只有一个有参构造器的情况下，参数的值就会从容器中拿
	public HttpEncodingAutoConfiguration(HttpEncodingProperties properties) {
		this.properties = properties;
	}

	@Bean  //给容器中添加一个组件，这个组件的某些值需要从properties中获取
	@ConditionalOnMissingBean  //判断容器中没有这个组件？
  	public CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
		filter.setEncoding(this.properties.getCharset().name());
		filter.setForceRequestEncoding(this.properties.shouldForce(Type.REQUEST));
		filter.setForceResponseEncoding(this.properties.shouldForce(Type.RESPONSE));
		return filter;
	}
```

根据当前不同的条件判断，决定这个配置类是否生效？

一旦这个配置类生效；这个配置类就会给容器中添加各种组件；这些组件的属性是从对应的properties类中获取的，这些类里面的每一个属性又是和配置文件绑定的；

5、所有在配置文件中能配置的属性都是在xxxProperties类中封装着；配置文件能配置什么就可以参照某个功能对应的这个属性类

```java
@ConfigurationProperties(prefix = "spring.http.encoding")  //从配置文件正获取指定的值和bean的属性进行绑定
public class HttpEncodingProperties {

   public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
```



**精髓：**

​	**1、Spring Boot启动汇价在大量的自动配置类**

​	**2、我们查看自己所需要的功能有没有Spring Boot默认写好的自动配置类**

​	**3、我们再来看这些自动配置类中到底配置了哪些组件（只要我们要用的组件有我们就不需要再来进行配置了）**

​	**4、给容器中自动配置类添加组件的时候，会从properties类中获取某些属性我们就可以在配置文件中指定这些属性的值**



xxxAutoConfiguration：自动配置类；

给容器中添加组件

xxxProperties：封装配置文件中相关属性；

### 2、细节

#### 1、@Conditional派生注解（Spring注解版原生的@Conitional作用）

作用：必须是@Conditional指定的条件成立，才给容器中添加组件，配置类里面的所有内容才生效；

| @Conditional扩展注解            | 作用（判断是否满足当前指定条件）                 |
| ------------------------------- | ------------------------------------------------ |
| @ConditionalOnJava              | 系统的java版本是否符合要求                       |
| @ConditionalOnBean              | 容器中存在指定Bean                               |
| @ConditionalOnMissingBean       | 容器中不存在指定Bean                             |
| @ConditionalOnExpression        | 满足SpEL表达式指定                               |
| @ConditionalOnClass             | 系统中有指定的类                                 |
| @ConditionalOnMissingClass      | 系统中没有指定的类                               |
| @ConditionalOnSingleCandidate   | 容器中只有一个指定的Bean，或者这个Bean是首选Bean |
| @ConditionalOnProperty          | 系统总指定的属性是否有指定的值                   |
| @ConditionalOnResource          | 类路径下是否存在指定资源                         |
| @ConditionalOnWebApplication    | 当前是web环境                                    |
| @ConditionalOnNotWebApplication | 当前不是web环境                                  |
| @ConditionalOnJndi              | JNDI存在指定项                                   |

**注：自动配置类必须在一定条件下才能生效**；

我们怎么知道哪些自动配置类生效？

我们可以通过在properties里面启用**debug=true**属性，来让控制台打印自动配置报告，这样我们就可以很方便的知道哪些自动配置类生效

```java
============================
CONDITIONS EVALUATION REPORT
============================


Positive matches:  //自动配置类启用的
-----------------

   CodecsAutoConfiguration matched:
      - @ConditionalOnClass found required class 'org.springframework.http.codec.CodecConfigurer'; @ConditionalOnMissingClass did not find unwanted class (OnClassCondition)

   CodecsAutoConfiguration.JacksonCodecConfiguration matched:
      - @ConditionalOnClass found required class 'com.fasterxml.jackson.databind.ObjectMapper'; @ConditionalOnMissingClass did not find unwanted class (OnClassCondition)

...
          
Negative matches:  //没有启用与没有匹配成功的自动配置类
-----------------

   ActiveMQAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'javax.jms.ConnectionFactory', 'org.apache.activemq.ActiveMQConnectionFactory' (OnClassCondition)

   AopAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.aspectj.lang.annotation.Aspect', 'org.aspectj.lang.reflect.Advice', 'org.aspectj.weaver.AnnotatedElement' (OnClassCondition)
...

```







