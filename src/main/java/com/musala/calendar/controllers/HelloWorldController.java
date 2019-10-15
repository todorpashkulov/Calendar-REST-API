package com.musala.calendar.controllers;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello/")
@Api(value = "HelloWorld Resource")
public class HelloWorldController {

    @GetMapping(value = "get")
    public String hello() {
        return "Hello";
    }

    @PostMapping(value = "post")
    public String postHello() {
        return hello();
    }

}
