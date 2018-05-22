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

