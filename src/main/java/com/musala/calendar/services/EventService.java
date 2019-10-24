package com.musala.calendar.services;

import java.util.List;

import com.musala.calendar.models.Event;

public interface EventService {

    List<Event> findAll();

    Event createEvent(Event event);

    Event findById(Integer id);

}
