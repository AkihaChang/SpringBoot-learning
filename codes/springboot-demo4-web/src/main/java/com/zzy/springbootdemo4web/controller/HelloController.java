package com.zzy.springbootdemo4web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HelloController {

    @ResponseBody
    @RequestMapping("/hello")
    private String hello(){
        return "hello world!";
    }

    //查出一些数据用于页面显示
    @RequestMapping("/success")
    private String success(Map<String, Object> map){
        //classpath:templates/success.html;

        map.put("hello", "<h1>你好</h1>");
        map.put("users", Arrays.asList("密苏里","黎塞留","企业"));
        return "success";
    }
}
