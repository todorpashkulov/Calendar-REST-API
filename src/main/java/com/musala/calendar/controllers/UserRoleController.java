package com.musala.calendar.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.musala.calendar.models.UserRole;
import com.musala.calendar.services.UserRoleService;

@RestController
@RequestMapping("/api")
public class UserRoleController {

    @Autowired
    UserRoleService userRoleService;

    @GetMapping("/userRole")
    public List<UserRole> findAll() {
        return userRoleService.findAll();
    }

    @GetMapping("/userRole/{id}")
    public UserRole findById(@PathVariable Integer id) {
        return userRoleService.findById(id);
    }

    @PostMapping("/userRole")
    public ResponseEntity<UserRole> create(@RequestBody UserRole userRole) {
        return new ResponseEntity<>(userRoleService.create(userRole), HttpStatus.CREATED);
    }

    @PutMapping("/userRole/{id}")
    public UserRole update(@PathVariable Integer id, @RequestBody UserRole userRole) {
        return userRoleService.update(id, userRole);
    }

    @DeleteMapping("/userRole/{id}")
    public void delete(@PathVariable Integer id) {
        userRoleService.delete(id);
    }
}