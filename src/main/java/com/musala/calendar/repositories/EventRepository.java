package com.musala.calendar.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.musala.calendar.models.Event;

@Repository
public interface EventRepository extends CrudRepository<Event, Integer> {
}
