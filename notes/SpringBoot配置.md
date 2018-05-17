# 一、配置

## 1、配置文件

Spring Boot使用一个全局配置文件,配置文件名是固定的

* appliation.properties
* application.yml

配置文件的作用：修改Spring Boot自动配置的默认值；Spring Boot在底层给我们配置好了；

##2、YAML语法

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

###2、值得写法

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

####数组（List、Set）：

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



