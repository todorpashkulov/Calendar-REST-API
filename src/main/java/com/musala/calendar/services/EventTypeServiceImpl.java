package com.musala.calendar.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musala.calendar.exeptions.NotFoundInDBException;
import com.musala.calendar.models.EventType;
import com.musala.calendar.repositories.EventTypeRepository;

@Service
public class EventTypeServiceImpl implements EventTypeService {
    private static final String NO_EVENT_TYPE_FOUND_MESSAGE = "Can't find eventType with ID: ";

    @Autowired
    EventTypeRepository eventTypeRepository;

    @Override
    public List<EventType> findAll() {
        return (List<EventType>) eventTypeRepository.findAll();
    }

    @Override
    public EventType findById(Integer id) {
        return eventTypeRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundInDBException(NO_EVENT_TYPE_FOUND_MESSAGE + id));
    }

    @Override
    public EventType create(EventType eventType) {
        return eventTypeRepository.save(eventType);
    }

    @Override
    public EventType update(Integer id, EventType updatedEventType) {
        updatedEventType.setId(id);
        return eventTypeRepository.save(updatedEventType);
    }

    @Override
    public void delete(Integer id) {
        if (eventTypeRepository.existsById(id)) {
            eventTypeRepository.deleteById(id);
        } else {
            throw new NotFoundInDBException(NO_EVENT_TYPE_FOUND_MESSAGE + id);
        }
    }
}
