package com.zzy.springbootdemo4web.controller;

import com.zzy.springbootdemo4web.dao.DepartmentDao;
import com.zzy.springbootdemo4web.dao.EmployeeDao;
import com.zzy.springbootdemo4web.entities.Department;
import com.zzy.springbootdemo4web.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collection;

@Controller
public class EmployeeController {
    @Autowired
    EmployeeDao employeeDao;

    @Autowired
    DepartmentDao departmentDao;

    //查询所有员工返回列表页面
    @GetMapping("/emps")
    public String list(Model model) {
        Collection<Employee> employees = employeeDao.getAll();

        //放在请求域中
        model.addAttribute("emps",employees);
        //thymeleaf默认就会拼串
        //classpath:/templates/xxx.html
        return "emps/list";
    }

    //来到员工添加页面
    @GetMapping("/emp")
    public String toAddPage(Model model) {
        //来到添加页面，查出所有的部门，在页面显示
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("depts",departments);

        return "emps/add";
    }

    //员工添加
    //SpringMVC自动将请求参数和入参对象的属性进行一一绑定；要求了请求参数的名字和javaBean入参的对象里面的属性名是一样的
    @PostMapping("/emp")
    public String addEmp(Employee employee) {
        //来到员工列表页面
        System.out.println("保存的员工信息："+employee);
        //保存员工
        employeeDao.save(employee);

        //redirect：表示重定向到一个地址  “/”代表当前项目路径
        //forward：表示转发到一个地址
        return "redirect:/emps";
    }

}
