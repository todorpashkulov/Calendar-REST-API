package com.musala.calendar.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musala.calendar.models.Event;
import com.musala.calendar.repositories.EventRepository;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventRepository eventRepository;

    @Override
    public Event createEvent(Event event) {
        return null;
    }

    @Override
    public List<Event> findAll() {
        return (List<Event>) eventRepository.findAll();
    }

    @Override
    public Event findById(Integer id) {
        if (eventRepository.findById(id).isPresent()) {
            return eventRepository.findById(id).get();
        } else {
            throw new NullPointerException();
        }
    }
}
