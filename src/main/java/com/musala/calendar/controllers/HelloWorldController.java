package com.musala.calendar.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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
