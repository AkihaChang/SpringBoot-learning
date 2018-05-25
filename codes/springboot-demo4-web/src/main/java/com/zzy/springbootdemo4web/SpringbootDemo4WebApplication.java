package com.zzy.springbootdemo4web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

@SpringBootApplication
public class SpringbootDemo4WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDemo4WebApplication.class, args);
    }

    @Bean
    public ViewResolver myViewResolver(){
        return myViewResolver();
    }

    private static class myViewResolver implements ViewResolver {

        @Override
        public View resolveViewName(String viewName, Locale locale) throws Exception {
            return null;
        }
    }
}
