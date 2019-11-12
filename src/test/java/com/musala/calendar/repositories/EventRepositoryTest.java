package com.musala.calendar.repositories;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import com.musala.calendar.models.Event;

import static org.assertj.core.api.Assertions.assertThat;


@SuppressWarnings("OptionalGetWithoutIsPresent")
@DataJpaTest
@Sql(scripts = "classpath:sql_scripts/createEventsFixture.sql")
@DisplayName("EventRepository should")
class EventRepositoryTest {

    @Autowired
    EventRepository eventRepository;

    @Test
    @DisplayName("return all events in DB as ArrayList")
    void whenCallingFindAllReturnEventList() {
        List<Event> eventList = (List<Event>) eventRepository.findAll();
        assertThat(eventList).hasSize(6);

    }

    @Test
    @DisplayName("return the correct event found by id")
    void whenCallingFindByIdReturnEvent() {
        Event event = eventRepository.findById(3).get();
        assertThat(event.getId()).isEqualTo(3);
        assertThat(event.getName()).isEqualTo("EVENT3");
    }

    @Test
    @DisplayName("return new event when saving and persist new event")
    void whenCallingSaveReturnPersistedEvent() {
        Event event = new Event(7, "EVENT7");
        eventRepository.save(event);
        Event savedEvent = eventRepository.findById(7).get();
        assertThat(savedEvent).isEqualTo(event);
    }

    @Test
    @DisplayName("delete event successfully")
    void whenCallingDeleteRemoveEventSuccessfully() {
        List<Event> startEventList = (List<Event>) eventRepository.findAll();
        eventRepository.deleteById(3);
        List<Event> endEventList = (List<Event>) eventRepository.findAll();
        assertThat(startEventList).contains(new Event(3, "EVENT3"));
        assertThat(endEventList).doesNotContain(new Event(3, "EVENT3"));
    }
}
