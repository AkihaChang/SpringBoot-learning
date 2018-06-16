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

### 1、Spring MVC auto-configuration

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

### 2、扩展SpringMVC

```xml
    <mvc:view-controller path="/hello" view-name="success"/>
    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/hello"/>
            <bean></bean>
        </mvc:interceptor>
    </mvc:interceptors>
```

**编写一个配置类（@Configuration），是WebMvcConfigurerAdapter类型；不能标注@EnableWebMvc**

既保留了自动配置，也能用我们的扩展配置；

```java
//使用WebMvcConfigurerAdapter可以扩展SpringMVC的功能
@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //super.addViewControllers(registry);
        //浏览器发送"/zzy"请求，同样来到success
        registry.addViewController("/zzy").setViewName("/success");
    }
}
```

原理：

​	1、WebMvcAutoConfiguration是SpringMVC的自动配置类

​	2、在做其他自动配置时会导入：**@Import(EnableWebMvcConfiguration.class)**

```java
@Configuration
public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport {
    private final WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();

    public DelegatingWebMvcConfiguration() {
    }
	
    //从容器中获取所有的WebMvcConfiguerer
    @Autowired(
        required = false
    )
    public void setConfigurers(List<WebMvcConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.configurers.addWebMvcConfigurers(configurers);
            
            //一个参考实现；将所有的WebMvcConfigurer相关配置都来一起调用
           	 @Override
		   	//public void addViewControllers(ViewControllerRegistry registry) {
			//	for (WebMvcConfigurer delegate : this.delegates) {
			//	delegate.addViewControllers(registry);
			//	}
			//}
        }
    }
```

​	3、容器中所有的WebMvcConfigurer都会一起起作用；

​	4、我们的配置类也会被调用；

​	效果：SpringMVC的自动配置和我们的扩展配置都会起作用

### 3、全面接管SpringMVC

Spring Boot对SpringMVC的自动配置不需要了，所有都是我们自己配；所有的SpringMVC的自动配置都失效了

**我们需要在配置类中添加@EnableWebMvc即可；**

```java
//使用WebMvcConfigurerAdapter可以扩展SpringMVC的功能
@EnableWebMvc
@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //super.addViewControllers(registry);
        //浏览器发送"/zzy"请求，同样来到success
        registry.addViewController("/zzy").setViewName("/success");
    }
}
```

原理：

为什么@EnableWebMvc自动配置就失效了；

1、EnableWebMvc的核心

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DelegatingWebMvcConfiguration.class)
public @interface EnableWebMvc {
}

```

2、

```java
@Configuration
public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport {
```

3、

```java
@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class })
//容器中没有这个组建的时候，这个自动配置类才生效
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
@AutoConfigureAfter({ DispatcherServletAutoConfiguration.class,
		ValidationAutoConfiguration.class })
public class WebMvcAutoConfiguration {
```

4、@EnableWebMvc将WebMvcConfigurationSupport组件导入进来；

5、导入的WebMvcConfigurationSupport只是SpringMVC最基本的功能；



## 5、如何修改Spring Boot的默认配置

模式：

​	1、Spring Boot在自动配置很多组件的时候，先看容器中有没有自己配制的（@Bean、@Component）如果有，就用用户配置的，如果没有，才自动配置；如果有些组件可以有多个（ViewResolver）将用户配置和自己默认的组合起来；

​	2、在Spring Boot中，会有非常多的xxxConfigurer帮助我们进行扩展配置



## 6、RestfulCRUD

### 1、默认访问首页

```java
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //super.addViewControllers(registry);
        //浏览器发送"/zzy"请求，同样来到success
        registry.addViewController("/zzy").setViewName("/success");
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/index.html").setViewName("login");
    }

}
```

### 2、国际化

**1、编写国际化配置文件；**

2、使用ResourceBundleMessageSource管理国际化资源文件

3、在页面使用fmt:message去除国际化内容



步骤：

1、编写国际化配置文件，抽取页面需要显示的国际化消息

![i18n-properties](https://github.com/AkihaChang/SpringBoot-learning/raw/master/notes/images/i18n-properties.png)

2、Spring Boot制度牌哪个配置好了管理国际化资源文件的组件

```java
@Conditional(ResourceBundleCondition.class)
@EnableConfigurationProperties
public class MessageSourceAutoConfiguration {
    
	@Bean
	@ConfigurationProperties(prefix = "spring.messages")
	public MessageSourceProperties messageSourceProperties() {
		return new MessageSourceProperties();
	}

	@Bean
	public MessageSource messageSource() {
		MessageSourceProperties properties = messageSourceProperties();
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		if (StringUtils.hasText(properties.getBasename())) {
            //设置国际化文件的基础名（去掉语言国家代码）
			messageSource.setBasenames(StringUtils.commaDelimitedListToStringArray(
					StringUtils.trimAllWhitespace(properties.getBasename())));
		}
		if (properties.getEncoding() != null) {
			messageSource.setDefaultEncoding(properties.getEncoding().name());
		}
		messageSource.setFallbackToSystemLocale(properties.isFallbackToSystemLocale());
		Duration cacheDuration = properties.getCacheDuration();
		if (cacheDuration != null) {
			messageSource.setCacheMillis(cacheDuration.toMillis());
		}
		messageSource.setAlwaysUseMessageFormat(properties.isAlwaysUseMessageFormat());
		messageSource.setUseCodeAsDefaultMessage(properties.isUseCodeAsDefaultMessage());
		return messageSource;
	}
```

3、去页面获取国际化的值

```html
<!DOCTYPE html>
<!-- saved from url=(0051)https://getbootstrap.com/docs/4.1/examples/sign-in/ -->
<html lang="en" xmlns:th="http://www.thymeleaf.org"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="https://getbootstrap.com/favicon.ico">

    <title>登录</title>

    <!-- Bootstrap core CSS -->
    <link href="asserts/css/bootstrap.min.css" th:href="@{/webjars/bootstrap/4.1.0/css/bootstrap.min.css}" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="asserts/css/signin.css" th:href="@{/asserts/css/signin.css}" rel="stylesheet">
  </head>

  <body class="text-center">
    <form class="form-signin" action="dashboard.html">
      <img class="mb-4" src="asserts/img/bootstrap-solid.svg" th:src="@{/asserts/css/bootstrap-solid.svg}" alt="" width="72" height="72">
      <h1 class="h3 mb-3 font-weight-normal" th:text="#{login.tip}">Please sign in</h1>
      <label for="inputEmail" class="sr-only" th:text="#{login.username}">Username</label>
      <input type="email" id="inputEmail" class="form-control" placeholder="Email address" th:placeholder="#{login.username}" required="" autofocus="">
      <label for="inputPassword" class="sr-only" th:text="#{login.password}">Password</label>
      <input type="password" id="inputPassword" class="form-control" placeholder="Password" th:placeholder="#{login.password}" required="">
      <div class="checkbox mb-3">
        <label>
          <input type="checkbox" value="remember-me" /> [[#{login.remember}]]
        </label>
      </div>
      <button class="btn btn-lg btn-primary btn-block" type="submit" th:text="#{login.btn}">Sign in</button>
      <p class="mt-5 mb-3 text-muted">© 2017-2018</p>
      <a class="btn btn-sm">中文</a>
      <a class="btn btn-sm">English</a>
    </form> 

</body>
</html>
```

效果：根据浏览器语言的信息切换国际化

注：修改系统默认编码格式

![default-file-settings-encoding](https://github.com/AkihaChang/SpringBoot-learning/raw/master/notes/images/default-file-settings-encoding.png)



原理：

​	国际化Locale（区域信息对象）；LocaleResolver（获取区域信息对象）；

```java
		//默认的就是根据请求头带来的区域信息获取Locale进行国际化
		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnProperty(prefix = "spring.mvc", name = "locale")
		public LocaleResolver localeResolver() {
			if (this.mvcProperties
					.getLocaleResolver() == WebMvcProperties.LocaleResolver.FIXED) {
				return new FixedLocaleResolver(this.mvcProperties.getLocale());
			}
			AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
			localeResolver.setDefaultLocale(this.mvcProperties.getLocale());
			return localeResolver;
		}

```

4、点击链接切换国际化

```java
package com.zzy.springbootdemo4web.component;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class MyLocaleResolver implements LocaleResolver {


    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String l = request.getParameter("l");
        Locale locale = Locale.getDefault();
        if(!StringUtils.isEmpty(l)){
            String[] split = l.split("_");
            locale = new Locale(split[0],split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}

/**
*  MyMvcConfig中添加如下代码
*
*/
    @Bean
    public LocaleResolver localeResolver() {
        return new MyLocaleResolver();
    }

```

### 3、登录

开发期间模板引擎页面修改一户，要实时生效

1、禁用模板引擎的缓存

```properties
# 禁用缓存
spring.thymeleaf.cache=false
```

2、页面修改完成以后ctrl+f9：重新编译

登录错误消息的显示

```html
<!--判断-->
<p style="color: red" th:text="${msg}" th:if="${not #strings.isEmpty(msg)}"></p>
```

### 4、拦截器进行登录检查

```java
**
 * 登录检查，
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {
    //目标方法执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("loginUser");
        if(user!=null) {
            return true;
        }else {
            request.setAttribute("msg","没有权限请先登录");
            request.getRequestDispatcher("/index.html").forward(request,response);
            return false;
        }
    }
}

 //注册拦截器（在MyMvcConfig中添加）
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //静态资源不会被拦截，Spring Boot已经最好了静态资源映射
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/index.html","/","/user/login");
    }
```

### 5、CRUD员工列表

实验要求：

1、RestfulCRUD：CRUD满足Rest风格；

URI：/资源名称/资源标识    HTTP请求方式区分对资源CRUD操作

|      | 普通CRUD（uri区分操作）  | RestfulCRUD        |
| ---- | ------------------------ | ------------------ |
| 查询 | getUser                  | user---GET         |
| 添加 | addUser                  | user---POST        |
| 修改 | updateUser?id=xxx&xxx=xx | user/{id}---PUT    |
| 删除 | deleteUser?id=1          | user/{id}---DELETE |

2、实验的请求架构：

|                                      | 请求URI | 请求方式 |
| ------------------------------------ | ------- | -------- |
| 查询所有用户                         | users   | GET      |
| 查询某个用户（来到修改页面）         | user/1  | GET      |
| 来到添加页面                         | user    | GET      |
| 添加用户                             | user    | POST     |
| 来到修改页面（查出用户进行信息回显） | user/1  | GET      |
| 修改用户                             | user    | PUT      |
| 删除用户                             | user/1  | DELETE   |

3、员工列表：

#### thymeleaf公共元素抽取

```html
//1、抽取公共片段
<div th:fragment="copy">
&copy; 2011 The Good Thymes Virtual Grocery
</div>

//2、引入公共片段
<div th:insert="~{footer :: copy}"></div>
//~{templatename::selector}：模板名：：选择器
//~{templatename::fragmentname}：模板名：：片段名

//3、默认效果：
//insert的功能片段在div标签中
//如果使用th:insert等属性进行引入，可以不用写~{}:
//行内写法可以加上：[[~{}]]；[(~{})]；
```

3种引入功能片段的th属性：

**th:insert**：将公共片段整个插入到声明引入的元素中

**th:replace**：将声明引入的元素替换为公共片段

**th:include**：将被引入的片段的内容包含进这个标签中

```html
<footer th:fragment="copy">
	&copy; 2011 The Good Thymes Virtual Grocery
</footer>

<!-- 引入方式：-->
<div th:insert="footer :: copy"></div>
<div th:replace="footer :: copy"></div>
<div th:include="footer :: copy"></div>

<!-- 效果：-->
<div>
    <footer>
    &copy; 2011 The Good Thymes Virtual Grocery
    </footer>
</div>

<footer>
	&copy; 2011 The Good Thymes Virtual Grocery
</footer>

<div>
	&copy; 2011 The Good Thymes Virtual Grocery
</div>
```



### 6、CRUD-员工添加

```html
<form>
        <div class="form-group">
          <label>lastName</label>
          <input type="text" class="form-control" placeholder="黎塞留">
        </div>
        <div class="form-group">
          <label>Email</label>
          <input type="text" class="form-control" placeholder="xxx@163.com">
        </div>
        <div class="form-group">
          <label>Gender</label><br/>
          <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="gender" value="1">
            <label class="form-check-label">男</label>
          </div>
          <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="gender" value="0">
            <label class="form-check-label">女</label>
          </div>
        </div>
        <div class="form-group">
          <label for="department">department</label>
          <select class="form-control" id="department">
            <option>1</option>
            <option>2</option>
            <option>3</option>
            <option>4</option>
            <option>5</option>
          </select>
        </div>
        <div class="form-group">
          <label>Birth</label>
          <input type="text" class="form-control" placeholder="密苏里">
        </div>
        <button type="submit" class="btn btn-primary">添加</button>
      </form>
```

注：提交的数据格式不对：生日：日期；

2017-12-12；2017/12/12（默认）；2017.12.12；

日期的格式化；SrpingMVC将页面提交的值需要转换为指定的类型；

2017-12-12---Date；类型转换，格式化

### 7、CRUD员工修改

与添加共用页面

```html
<main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4"><div class="chartjs-size-monitor" style="position: absolute; left: 0px; top: 0px; right: 0px; bottom: 0px; overflow: hidden; pointer-events: none; visibility: hidden; z-index: -1;"><div class="chartjs-size-monitor-expand" style="position:absolute;left:0;top:0;right:0;bottom:0;overflow:hidden;pointer-events:none;visibility:hidden;z-index:-1;"><div style="position:absolute;width:1000000px;height:1000000px;left:0;top:0"></div></div><div class="chartjs-size-monitor-shrink" style="position:absolute;left:0;top:0;right:0;bottom:0;overflow:hidden;pointer-events:none;visibility:hidden;z-index:-1;"><div style="position:absolute;width:200%;height:200%;left:0; top:0"></div></div></div>
      <!-- 需要区分是修改还是添加 -->
      <form th:action="@{/emp}" method="post">
        <!-- 发送put请求修改员工数据 -->
        <!--
        1、SpringMVC中配置HiddenHttpMethodFilter；（Spring Boot自动配置好的）
        2、页面创建一个post表单
        3、创建一个input项，name="_method"；值就是我们指定的请求方式
        -->
        <input type="hidden" name="_method" value="put" th:if="${emp!=null}"/>
        <!--<input type="hidden" name="_method" value="put" th:if="${emp!=null}" th:value="${emp.id}"/>-->
        <div class="form-group">
          <label>id</label>
          <input type="text" name="id" class="form-control" placeholder="id" th:value="${emp!=null}?${emp.id}">
        </div>
        <div class="form-group">
          <label>lastName</label>
          <input type="text" name="lastName" class="form-control" placeholder="黎塞留" th:value="${emp!=null}?${emp.lastName}">
        </div>
        <div class="form-group">
          <label>Email</label>
          <input type="email" name="email" class="form-control" placeholder="xxx@163.com" th:value="${emp!=null}?${emp.email}">
        </div>
        <div class="form-group">
          <label>Gender</label><br/>
          <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="gender" value="1" th:checked="${emp!=null}?${emp.gender==1}">
            <label class="form-check-label">男</label>
          </div>
          <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="gender" value="0" th:checked="${emp!=null}?${emp.gender==0}">
            <label class="form-check-label">女</label>
          </div>
        </div>
        <div class="form-group">
          <!-- 提交的是部门id -->
          <label>department</label>
          <select class="form-control" name="department.id">
            <option th:selected="${emp!=null}?${dept.id==emp.department.id}" th:each="dept:${depts}" th:value="${dept.id}" th:text="${dept.departmentName}"></option>
          </select>
        </div>
        <div class="form-group">
          <label>Birth</label>
          <input type="text" name="birth" class="form-control" placeholder="密苏里" th:value="${emp!=null}?${#dates.format(emp.birth, 'yyyy-MM-dd')}">
        </div>
        <button type="submit" class="btn btn-primary" th:text="${emp!=null}?'修改':'添加'"></button>
      </form>
    </main>
```

### 8、员工删除

```html
<td>
              <a class="btn btn-sm btn-primary" th:href="@{/emp/}+${emp.id}">编辑</a>
              <button th:attr="del_uri=@{/emp/}+${emp.id}" class="btn btn-sm btn-danger deleteBtn">删除</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </main>
    <form id="deleteEmpForm" method="post">
      <input type="hidden" name="_method" value="delete"/>
    </form>

<!-- 利用js提交 -->
<!-- 按钮点击事件 -->
<script>
  $(".deleteBtn").click(function () {
      //删除当前员工
      console.log($(this).attr("del_uri"));
      $("#deleteEmpForm").attr("action",$(this).attr("del_uri")).submit();
      return false;
  })
</script>
```



## 7、错误处理机制

### 1、默认的错误处理机制

默认效果：

​	1、浏览器，返回一个默认的错误页面

![error](https://github.com/AkihaChang/SpringBoot-learning/raw/master/notes/images/error.png)

浏览器发送请求的请求头：

![rowser-header](https://github.com/AkihaChang/SpringBoot-learning/raw/master/notes/images/browser-header.png)

​	2、如果其他客户端，默认响应一个json数据

![error-mobile](https://github.com/AkihaChang/SpringBoot-learning/raw/master/notes/images/error-mobile.png)

![mobile-header](https://github.com/AkihaChang/SpringBoot-learning/raw/master/notes/images/mobile-header.png)

原理：

​	可以参照ErrorMvcAutoConfiguration；错误处理的自动配置；

​	给容器中添加了以下组件：

​	1、DefaultErrorAttributes：

```java
//帮我们在页面共享信息
	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest,
			boolean includeStackTrace) {
		Map<String, Object> errorAttributes = new LinkedHashMap<>();
		errorAttributes.put("timestamp", new Date());
		addStatus(errorAttributes, webRequest);
		addErrorDetails(errorAttributes, webRequest, includeStackTrace);
		addPath(errorAttributes, webRequest);
		return errorAttributes;
	}
```



​	2、BaicErrorController：处理默认/error请求

```java
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class BasicErrorController extends AbstractErrorController {
    
    @RequestMapping(produces = "text/html")  //产生html类型的数据；浏览器发送的请求来到这个方法处理
	public ModelAndView errorHtml(HttpServletRequest request,
			HttpServletResponse response) {
		HttpStatus status = getStatus(request);
		Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(
				request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
		response.setStatus(status.value());
        //去哪个页面作为错误页面；包含页面地址和页面内容
		ModelAndView modelAndView = resolveErrorView(request, response, status, model);
		return (modelAndView != null ? modelAndView : new ModelAndView("error", model));
	}

	@RequestMapping
	@ResponseBody  //产生json数据，其他客户端来到这个方法处理
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		Map<String, Object> body = getErrorAttributes(request,
				isIncludeStackTrace(request, MediaType.ALL));
		HttpStatus status = getStatus(request);
		return new ResponseEntity<>(body, status);
	}
```

​	3、ErrorPageCustomizer：

```java
	/**
	 * Path of the error controller.
	 */
	@Value("${error.path:/error}")
	private String path = "/error";  //系统出现错误以后，来到error请求，进行处理；(web.xml注册错误页面规则)；就会来到/error请求
```

​	4、DefaultErrorViewResolver：

```java
	@Override
	public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status,
			Map<String, Object> model) {
		ModelAndView modelAndView = resolve(String.valueOf(status), model);
		if (modelAndView == null && SERIES_VIEWS.containsKey(status.series())) {
			modelAndView = resolve(SERIES_VIEWS.get(status.series()), model);
		}
		return modelAndView;
	}

	private ModelAndView resolve(String viewName, Map<String, Object> model) {
        //默认Spring Boot可以去找到一个页面？  error/404
		String errorViewName = "error/" + viewName;
        //模板引擎可以解析这个页面地址就用模板引擎解析
		TemplateAvailabilityProvider provider = this.templateAvailabilityProviders
				.getProvider(errorViewName, this.applicationContext);
		if (provider != null) {
            //模板引擎可用的情况下返回到errorViewName指定的视图地址
			return new ModelAndView(errorViewName, model);
		}
        //模板引擎不可用，就在静态资源文件夹下找errorViewName对应的页面 error/404.html
		return resolveResource(errorViewName, model);
	}
```

​	步骤：

​		一旦系统出现4xx或者5xx之类的错误；	ErrorPageCustomizer就会生效（定制错误的响应规则）；就会来到/error请求；就会被**BaicErrorController**处理；

​		1、响应页面；去哪个页面是由**DefaultErrorViewResolver**解析得到的

```java
protected ModelAndView resolveErrorView(HttpServletRequest request,
			HttpServletResponse response, HttpStatus status, Map<String, Object> model) {
    	//所有的ErrorViewResolver得到ModelAndView
		for (ErrorViewResolver resolver : this.errorViewResolvers) {
			ModelAndView modelAndView = resolver.resolveErrorView(request, status, model);
			if (modelAndView != null) {
				return modelAndView;
			}
		}
		return null;
	}
```



### 2、如何定制错误响应：

#### 1、如何定制错误的页面；

​		1、有模板引擎的情况下；error/状态码；（建错误页面命名为 错误状态码.html 放在模板引擎文件夹里面的error文件夹下），发生此状态码的错误，就会来到对应的页面

​		我们可以使用4xx和5xx作为错误页面的文件名来匹配这种类型的所有为错误（精确优先：有限寻找精确的状态码.html）

​		页面能获取的信息：

​		timestamp：时间戳

​		status：状态码

​		error：错误的提示

​		exception：异常

​		message：异常消息

​		errors：JSR303数据校验的错误都在这里

​		2、没有模板引擎的情况下（模板引擎找不到这个错误页面），静态资源文件夹下找；

​		3、以上都没有错误页面，就是默认来到Spring Boot默认的错误提示页面；

#### 2、如何定制错误的json数据；

​	1、自定义异常处理&返回定制json数据；

```java
@ControllerAdvice
public class MyExceptionHandler {
	//1、浏览器客户端返回的都是json
    @ResponseBody
    @ExceptionHandler(UserNotExistException.class)
    public Map<String,Object> handleExcpetion(Exception e) {

        Map<String,Object> map = new HashMap<>();
        map.put("code","user.notexist");
        map.put("message",e.getMessage());
        return map;
    }
}
//没有自适应效果
```

​	2、转发到/error进行自适应效果处理

```java
    @ExceptionHandler(UserNotExistException.class)
    public String handleException(Exception e, HttpServletRequest request) {
        Map<String,Object> map = new HashMap<>();

        //传入自己的错误状态码  4xx 5xx，否则就不会进入定制错误页面的解析流程
        /**
         * Integer statusCode = (Integer) request
         * 				.getAttribute("javax.servlet.error.status_code");
         */
        request.setAttribute("javax.servlet.error.status_code",500);
        map.put("code","user.notexist");
        map.put("message",e.getMessage());
        //转发到/error
        return "forward:/error";
    }
```

​	3、将我们的定制数据携带出去；

出现错误以后，回来到/error请求，会被BasicErrorController处理，相应出去可以获取的数据是由getErrorAttributes得到的（是AbstractErrorCotroller（ErrorController）规定的方法）；

​	1、完全编写一个ErrorController的实现类（或者编写AbstractErrorCotroller的子类），放在容器中；

​	2、页面上能用的数据，或者是json返回能用的数据都是通过errorAttributes.getErrorAttributes()得到；

​		容器中DefaultErrorAttributes.getErrorAttributes()默认进行数据处理的；

自定义ErrorAttributes

```java
//给容器中加入我们自己定义的ErrorAttributes
@Component
public class MyErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> map = super.getErrorAttributes(webRequest, includeStackTrace);
        map.put("person","zzy");
        return map;
    }
}
```

最终效果：响应是自适应的，可以通过定制ErrorAttributes改变需要返回的内容。



## 8、配置嵌入式Servlet容器

Spring Boot默认是用的是嵌入式的Servlet容器（Tomcat）；

