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

