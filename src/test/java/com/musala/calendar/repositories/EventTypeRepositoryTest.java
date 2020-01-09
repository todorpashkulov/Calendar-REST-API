package com.musala.calendar.repositories;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import com.musala.calendar.models.EventType;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@DataJpaTest
@Sql(scripts = "classpath:sql_scripts/createEventTypesFixture.sql")
@DisplayName("EventTypeRepository should")
class EventTypeRepositoryTest {

    @Autowired
    EventTypeRepository eventTypeRepository;

    @Test
    @DisplayName("return all eventTypes in DB as ArrayList")
    void whenCallingFindAllReturnEventTypeList() {
        List<EventType> eventList = (List<EventType>) eventTypeRepository.findAll();
        assertThat(eventList).hasSize(6);
    }

    @Test
    @DisplayName("return the correct eventType found by id")
    void whenCallingFindByIdReturnEventType() {
        EventType eventType = eventTypeRepository.findById(3).get();
        assertThat(eventType.getId()).isEqualTo(3);
        assertThat(eventType.getName()).isEqualTo("EVENT_TYPE3");
    }

    @Test
    @DisplayName("return new eventType when saving and persist new eventType")
    void whenCallingSaveReturnPersistedEventType() {
        EventType eventType = new EventType(7, "EVENT_TYPE7");
        eventTypeRepository.save(eventType);
        EventType savedEventType = eventTypeRepository.findById(7).get();
        assertThat(savedEventType).isEqualTo(eventType);
    }

    @Test
    @DisplayName("delete eventType successfully")
    void whenCallingDeleteRemoveEventTypeSuccessfully() {
        List<EventType> startEventTypeList = (List<EventType>) eventTypeRepository.findAll();
        eventTypeRepository.deleteById(3);
        List<EventType> endEventTypeList = (List<EventType>) eventTypeRepository.findAll();
        assertThat(startEventTypeList).contains(new EventType(3, "EVENT_TYPE3"));
        assertThat(endEventTypeList).doesNotContain(new EventType(3, "EVENT_TYPE3"));
    }
}