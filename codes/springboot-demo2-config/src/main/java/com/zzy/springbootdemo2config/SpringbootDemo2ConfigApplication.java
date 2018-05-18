package com.zzy.springbootdemo2config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
//@ImportResource("classpath:beans.xml")
public class SpringbootDemo2ConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDemo2ConfigApplication.class, args);
    }
}
