package com.zzy.springbootdemo2config;

import com.zzy.springbootdemo2config.bean.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Spring Boot单元测试
 * 可以在测试期间很方便的类似编码一样注入容器的功能
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootDemo2ConfigApplicationTests {

    @Autowired
    Person person;

    @Test
    public void contextLoads() {
        System.out.println(person);
    }

}
