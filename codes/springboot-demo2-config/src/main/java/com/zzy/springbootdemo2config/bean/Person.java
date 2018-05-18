package com.zzy.springbootdemo2config.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    //@Value("${person.maps}")
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
