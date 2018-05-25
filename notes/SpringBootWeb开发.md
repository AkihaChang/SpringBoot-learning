# 一、Web开发

## 1、简介

使用Spring Boot；

**1、创建Spring Boot应用，选中我们需要的模块；**

**2、Spring Boot已经默认将这些场景配置好了，只需要在配置文件中指定少量配置就可以运行起来**

**3、自己编写业务代码**



自动配置原理？

这个场景Spring Boot帮我们配置了什么？能不能修改？能修改哪些配置？能不能扩展？......

```java
xxxAutoConfiguration：帮我们给容器中自动配置组件。
xxxProperties：配置类来封装配置文件的内容
```



## 2、Spring Boot对静态资源的映射规则

```java
@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			if (!this.resourceProperties.isAddMappings()) {
				logger.debug("Default resource handling disabled");
				return;
			}
			Duration cachePeriod = this.resourceProperties.getCache().getPeriod();
			CacheControl cacheControl = this.resourceProperties.getCache()
					.getCachecontrol().toHttpCacheControl();
			if (!registry.hasMappingForPattern("/webjars/**")) {
				customizeResourceHandlerRegistration(registry
						.addResourceHandler("/webjars/**")
						.addResourceLocations("classpath:/META-INF/resources/webjars/")
						.setCachePeriod(getSeconds(cachePeriod))
						.setCacheControl(cacheControl));
			}
			String staticPathPattern = this.mvcProperties.getStaticPathPattern();
			if (!registry.hasMappingForPattern(staticPathPattern)) {
				customizeResourceHandlerRegistration(
						registry.addResourceHandler(staticPathPattern)
								.addResourceLocations(getResourceLocations(
										this.resourceProperties.getStaticLocations()))
								.setCachePeriod(getSeconds(cachePeriod))
								.setCacheControl(cacheControl));
			}
		}
		
		//配置欢迎页的映射
		@Bean
		public WelcomePageHandlerMapping welcomePageHandlerMapping(
				ApplicationContext applicationContext) {
			return new WelcomePageHandlerMapping(
					new TemplateAvailabilityProviders(applicationContext),
					applicationContext, getWelcomePage(),
					this.mvcProperties.getStaticPathPattern());
		}
		
		//配置喜欢的图标
		@Configuration
		@ConditionalOnProperty(value = "spring.mvc.favicon.enabled", matchIfMissing = true)
		public static class FaviconConfiguration implements ResourceLoaderAware {

			private final ResourceProperties resourceProperties;

			private ResourceLoader resourceLoader;

			public FaviconConfiguration(ResourceProperties resourceProperties) {
				this.resourceProperties = resourceProperties;
			}

			@Override
			public void setResourceLoader(ResourceLoader resourceLoader) {
				this.resourceLoader = resourceLoader;
			}

			@Bean
			public SimpleUrlHandlerMapping faviconHandlerMapping() {
				SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
				mapping.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
                 //所有的 **/favicon.ico
				mapping.setUrlMap(Collections.singletonMap("**/favicon.ico",
						faviconRequestHandler()));
				return mapping;
			}

			@Bean
			public ResourceHttpRequestHandler faviconRequestHandler() {
				ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
				requestHandler.setLocations(resolveFaviconLocations());
				return requestHandler;
			}

			private List<Resource> resolveFaviconLocations() {
				String[] staticLocations = getResourceLocations(
						this.resourceProperties.getStaticLocations());
				List<Resource> locations = new ArrayList<>(staticLocations.length + 1);
				Arrays.stream(staticLocations).map(this.resourceLoader::getResource)
						.forEach(locations::add);
				locations.add(new ClassPathResource("/"));
				return Collections.unmodifiableList(locations);
			}

		}
```



1、所有"**/webjars/****"，都去"**classpath:/META-INF/resources/webjars/**"找资源；

​	webjars：以jar包的形式引入静态资源

​	http://www.webjars.org

![jquery文件目录](https://github.com/AkihaChang/SpringBoot-learning/raw/master/notes/images/jquery.png)

​	页面访问路径：localhost:8080/webjars/jquery/3.3.1-1/jquery.js

```xml
<!-- 引入jQuery 在访问的时候只需要写webjars下面资源的名称即可 -->
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>jquery</artifactId>
            <version>3.3.1-1</version>
        </dependency>
```



2、"/**"访问当前项目的任何资源（静态资源的文件夹）

```java
"classpath:/META-INF/resources/"
"classpath:/resources/",
"classpath:/static/"
"classpath:/public/"
```

localhost:8080/abc：去静态资源文件夹里面找abc

3、欢迎页；静态资源文件夹下的所有index.html页面；被"/**"映射；

​	localhost:8080/ 找index页面

4、所有的**/favicon.ico 都是在静态资源文件下找



## 3、模板引擎

JSP、Velocity、Freemarker、Thymeleaf；

![模板引擎](https://github.com/AkihaChang/SpringBoot-learning/raw/master/notes/images/template-engine.png)

Srping Boot推荐的Thymeleaf模板引擎；

语法更简单，功能更强大；



### 1、引入thymeleaf

```xml
<!-- 引入thymeleaf-->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

<!-- 切换thymeleaf版本 （Spring Boot 1.5x版本需要，2.0以上版本默认thymeleaf 3.0+）-->
<!-- 布局功能的支持程序 thymeleaf3主程序 layout2以上版本 -->
<!-- thymeleaf2 layout1 -->
<properties>
	<thymeleaf.version>3.0.9 RELEASE</thymeleaf.version>
	<thyme-layout-dialect.version>2.2.2</thyme-layout-dialect.version>
</properties>
```

### 2、Thymeleaf使用&语法

```java
@ConfigurationProperties(prefix = "spring.thymeleaf")
public class ThymeleafProperties {

	private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

	public static final String DEFAULT_PREFIX = "classpath:/templates/";

	public static final String DEFAULT_SUFFIX = ".html";
    //只要我们把Html页面放在"classpath:/templates/"，thymeleaf就能自动渲染
```

只要我们吧Htm页面放在classpath:/templates/，thymeleaf就能自动渲染；

使用：

1、饶茹thymeleaf的名称空间

```xml
<html lang="en" xmlns:th="http://www.thymeleaf.org"></html>
```

2、使用thymeleaf语法

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>成功</title>
</head>
<body>
    <h1>成功！</h1>
    <!-- th:text：将div练得文本内容设置为-->
    <div th:text="${hello}"></div>

</body>
</html><!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>成功</title>
</head>
<body>
    <h1>成功！</h1>
    <!-- th:text：将div练得文本内容设置为-->
    <div th:text="${hello}"></div>

</body>
</html>
```

### 3、语法规则

1、th:text改变当前元素里面的文本内容；

​	th:任意html属性，来替换原生属性的值

![thymeleaf th语法](https://github.com/AkihaChang/SpringBoot-learning/raw/master/notes/images/thymeleaf-th.png)

2、表达式：

```properties
Simple expressions:  //表达式语法
    Variable Expressions: ${...}  //获取变量值；OGNL
    	//1、获取对象的属性、调用方法；
    	//2、使用内置的基本对象：
    		#ctx : the context object.
			#vars: the context variables.
			#locale : the context locale.
			#request : (only in Web Contexts) the HttpServletRequest object.
             #response : (only in Web Contexts) the HttpServletResponse object.
             #session : (only in Web Contexts) the HttpSession object.
             #servletContext : (only in Web Contexts) the ServletContext object.

             ${session.foo}
         //3、内置的一些工具对象
         	#execInfo : information about the template being processed.
            #messages : methods for obtaining externalized messages inside variables expressions, in the same way as they
            would be obtained using #{…} syntax.
            #uris : methods for escaping parts of URLs/URIs
            #conversions : methods for executing the configured conversion service (if any).
            #dates : methods for java.util.Date objects: formatting, component extraction, etc.
            #calendars : analogous to #dates , but for java.util.Calendar objects.
            #numbers : methods for formatting numeric objects.
            #strings : methods for String objects: contains, startsWith, prepending/appending, etc.
            #objects : methods for objects in general.
            #bools : methods for boolean evaluation.
            #arrays : methods for arrays.
            #lists : methods for lists.
            #sets : methods for sets.
            #maps : methods for maps.
            #aggregates : methods for creating aggregates on arrays or collections.
            #ids : methods for dealing with id attributes that might be repeated (for example, as a result of an iteration).
         
             Selection Variable Expressions: *{...}  //选择表达式：和${}在功能上一样
             	//补充：配合 th:object="${session.user}"：
             		 <div th:object="${session.user}">
						<p>Name: <span th:text="*{firstName}">Sebastian</span>.</p>
						<p>Surname: <span th:text="*{lastName}">Pepper</span>.</p>
						<p>Nationality: <span th:text="*{nationality}">Saturn</span>.</p>
             		 </div>
             Message Expressions: #{...}  //获取国际化内容
             Link URL Expressions: @{...}  //定义URL
             	@{/order/process(execId=${execId},execType='FAST')}
             Fragment Expressions: ~{...}  //片段引用表达式
             	<div th:insert="~{commons :: main}">...</div>
Literals
Text literals: 'one text' , 'Another one!' ,…
Number literals: 0 , 34 , 3.0 , 12.3 ,…
Boolean literals: true , false
Null literal: null
Literal tokens: one , sometext , main ,…
Text operations:  //文本操作
	String concatenation: +
	Literal substitutions: |The name is ${name}|
Arithmetic operations:  //数学运算
	Binary operators: + , - , * , / , %
	Minus sign (unary operator): -
Boolean operations:  //布尔运算
	Binary operators: and , or
	Boolean negation (unary operator): ! , not
Comparisons and equality:  //比较运算
	Comparators: > , < , >= , <= ( gt , lt , ge , le )
	Equality operators: == , != ( eq , ne )
Conditional operators:  //条件运算（三元运算符）
If-then: (if) ? (then)
If-then-else: (if) ? (then) : (else)
Default: (value) ?: (defaultvalue)
Special tokens:  //特殊操作
Page 17 of 104No-Operation:
```



## 4、SpringMVC自动配置

https://docs.spring.io/spring-boot/docs/2.0.2.RELEASE/reference/htmlsingle/#boot-features-developing-web-application

Spring Boot自动配置好了SpringMVC

以下是Spring Boot对SpringMVC的默认配置：

- Inclusion of `ContentNegotiatingViewResolver` and `BeanNameViewResolver` beans.

  - 自动配置了ViewResolver（视图解析器：根据方法的返回值得到视图对象（View），试图对象决定如何渲染（转发？重定向？））
  - `ContentNegotiatingViewResolver` ：组合所有的视图解析器；
  - **如何定制：我们可以自己给容器中添加一个视图解析器；自动的将其组合进来；**

- Support for serving static resources, including support for WebJars (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.0.2.RELEASE/reference/htmlsingle/#boot-features-spring-mvc-static-content))).  //静态资源文件夹路径和WebJars

- Automatic registration of `Converter`, `GenericConverter`, and `Formatter` beans.

  - Converter：转换器；public String hello(User user)：类型转换使用Converter

  - Formatter：格式化器；2018-5-25格式化为Date；

    ```java
    @Bean
    @ConditionalOnProperty(Prefix = "spring.mvc", name = "date-format")  //在文件中配置日期格式化的规则
    public Formatter<Date> dateFormatter() {
        return new DateFormatter(this.mvcProperties.getDateFormat());  //日期格式化组件
    }
    ```

    **自己添加的格式化器转换器我们只要放在容器中即可。**

- Support for `HttpMessageConverters` (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.0.2.RELEASE/reference/htmlsingle/#boot-features-spring-mvc-message-converters)).

  - `HttpMessageConverters`：SpringMVC用来转换Http请求和相应的；例如把User以json方式返回；

  - `HttpMessageConverters`是从容器中确定的；获取所有的HttpMessageConverter；

    **自己给容器中添加HttpMessageConverter，只需要将自己的组件注册在容器中（@Bean，@Component）**

- Automatic registration of `MessageCodesResolver` (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.0.2.RELEASE/reference/htmlsingle/#boot-features-spring-message-codes)).  //定义错误代码生成规则

- Static `index.html` support.  //静态首页访问

- Custom `Favicon` support (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.0.2.RELEASE/reference/htmlsingle/#boot-features-spring-mvc-favicon)).  //favion.ico

- Automatic use of a `ConfigurableWebBindingInitializer` bean (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.0.2.RELEASE/reference/htmlsingle/#boot-features-spring-mvc-web-binding-initializer)).  //我们可**以配置一个`ConfigurableWebBindingInitializer`来替换默认的；（添加到容器）**

  ```java
  初始化WebDataBinder；
  请求数据---JavaBean；
  ```

  **org.springframework.boot.autoconfigure.web：web的所有自动配置场景**

If you want to keep Spring Boot MVC features and you want to add additional [MVC configuration](https://docs.spring.io/spring/docs/5.0.6.RELEASE/spring-framework-reference/web.html#mvc) (interceptors, formatters, view controllers, and other features), you can add your own `@Configuration` class of type `WebMvcConfigurer` but **without** `@EnableWebMvc`. If you wish to provide custom instances of `RequestMappingHandlerMapping`, `RequestMappingHandlerAdapter`, or`ExceptionHandlerExceptionResolver`, you can declare a `WebMvcRegistrationsAdapter` instance to provide such components.

If you want to take complete control of Spring MVC, you can add your own `@Configuration` annotated with `@EnableWebMvc`.



## 5、如何修改Spring Boot的默认配置

模式：

​	1、Spring Boot在自动配置很多组件的时候，先看容器中有没有自己配制的（@Bean、@Component）如果有，就用用户配置的，如果没有，才自动配置；如果有些组件可以有多个（ViewResolver）将用户配置和自己默认的组合起来；

​	2、