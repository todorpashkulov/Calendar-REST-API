package com.musala.calendar.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.musala.calendar.models.User;
import com.musala.calendar.services.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/user")
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/user/{id}")
    public User findById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @PostMapping("/user")
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping("/user/{id}")
    public User update(@PathVariable Integer id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @DeleteMapping("/user/{id}")
    public void delete(@PathVariable Integer id) {
        userService.delete(id);
    }
}
