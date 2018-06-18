package com.zzy.springboot04webjsp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SuccessController {

    @GetMapping("/abc")
    public String hello(Model model) {
        model.addAttribute("msg","你好");

        return "success";
    }
}
