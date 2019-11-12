package com.musala.calendar.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.musala.calendar.models.EventType;
import com.musala.calendar.services.EventTypeService;

@RestController
@RequestMapping("/api")
public class EventTypeController {

    @Autowired
    EventTypeService eventTypeService;

    @GetMapping("/eventType")
    public List<EventType> getAll() {
        return eventTypeService.findAll();
    }

    @GetMapping("/eventType/{id}")
    public EventType getById(@PathVariable Integer id) {
        return eventTypeService.findById(id);
    }

    @PostMapping("/eventType")
    public ResponseEntity<EventType> create(@RequestBody EventType eventType) {
        return new ResponseEntity<EventType>(eventTypeService.create(eventType), HttpStatus.CREATED);
    }

    @PutMapping("/eventType/{id}")
    public EventType update(@PathVariable Integer id, @RequestBody EventType eventType) {
        return eventTypeService.update(id, eventType);
    }

    @DeleteMapping("/eventType/{id}")
    public void delete(@PathVariable Integer id) {
        eventTypeService.delete(id);
    }
}
