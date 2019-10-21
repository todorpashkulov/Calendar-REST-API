package com.musala.calendar.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.musala.calendar.models.Event;
import com.musala.calendar.services.EventService;

@RestController
@RequestMapping("/api")
public class EventController {

    @Autowired
    EventService eventService;

    @GetMapping("/event")
    public List<Event> getAll() {
        return eventService.findAll();
    }

    @GetMapping("/event/{id}")
    public Event getById(@PathVariable Integer id) {
        return eventService.findById(id);
    }

    @PostMapping("/event")
    public Event create(@RequestBody Event event) {
        return eventService.createEvent(event);
    }
}
