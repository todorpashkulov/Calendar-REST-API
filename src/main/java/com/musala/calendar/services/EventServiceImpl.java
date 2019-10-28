package com.musala.calendar.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musala.calendar.exeptions.NotFoundInDBException;
import com.musala.calendar.models.Event;
import com.musala.calendar.repositories.EventRepository;

@Service
public class EventServiceImpl implements EventService {
    private static final String NO_EVENT_FOUND_MESSAGE = "Can't find event with ID: ";

    @Autowired
    EventRepository eventRepository;

    @Override
    public List<Event> findAll() {
        return (List<Event>) eventRepository.findAll();
    }

    @Override
    public Event findById(Integer id) {
        return eventRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundInDBException(NO_EVENT_FOUND_MESSAGE + id));
    }

    @Override
    public Event create(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event update(Integer id, Event updatedEvent) {
        updatedEvent.setId(id);
        return eventRepository.save(updatedEvent);
    }

    @Override
    public void delete(Integer id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
        } else {
            throw new NotFoundInDBException(NO_EVENT_FOUND_MESSAGE + id);
        }
    }
}
