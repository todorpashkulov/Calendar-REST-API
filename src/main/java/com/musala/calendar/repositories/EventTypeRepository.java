package com.musala.calendar.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.musala.calendar.models.EventType;

@Repository
public interface EventTypeRepository extends CrudRepository<EventType, Integer> {
}
