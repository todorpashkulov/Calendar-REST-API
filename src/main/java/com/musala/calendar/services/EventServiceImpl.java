package com.musala.calendar.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musala.calendar.exeptions.NotFoundInDBException;
import com.musala.calendar.models.Event;
import com.musala.calendar.repositories.EventRepository;

@Service
public class EventServiceImpl implements EventService {
    private static final String ERROR_MESSAGE = "Can't find event with ID: ";

    @Autowired
    EventRepository eventRepository;

    public static String getErrorMessage() {
        return ERROR_MESSAGE;
    }

    @Override
    public List<Event> findAll() {
        return (List<Event>) eventRepository.findAll();
    }

    @Override
    public Event findById(Integer id) {
        return eventRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundInDBException(ERROR_MESSAGE + id));
    }

    @Override
    public Event create(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event update(Integer id, Event updatedEvent) {
        if (eventRepository.existsById(id)) {
            updatedEvent.setId(id);
            return eventRepository.save(updatedEvent);
        } else {
            throw new NotFoundInDBException(ERROR_MESSAGE + id);
        }
    }

    @Override
    public void delete(Integer id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
        } else {
            throw new NotFoundInDBException(ERROR_MESSAGE + id);
        }
    }
}
