package com.musala.calendar.controllers;

import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.musala.calendar.models.Event;
import com.musala.calendar.services.EventService;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EventController.class)
@DisplayName("EventController should")
class EventControllerTest {
    @Autowired

    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private EventService eventService;

    @Test
    @DisplayName("return JSON array with all events through controller layer")
    void whenCallingFindAllReturnEventList() throws Exception {
        when(eventService.findAll()).thenReturn(Arrays.asList(new Event("event1"),
                new Event("event2"),
                new Event("event3")));
        mvc.perform(get("/api/event").accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(3)))
           .andExpect(jsonPath("$[0].name", is("event1")))
           .andExpect(jsonPath("$[1].name", is("event2")))
           .andExpect(jsonPath("$[2].name", is("event3")));
        verify(eventService, times(1)).findAll();
    }

    @Test
    @DisplayName("return event as JSON through controller layer")
    void whenCallingFindByIdReturnEvent() throws Exception {
        Event event = new Event(1, "event1");
        when(eventService.findById(1)).thenReturn(event);
        mvc.perform(get("/api/event/{id}", 1).accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(1)))
           .andExpect(jsonPath("$.name", is("event1")));
        verify(eventService, times(1)).findById(1);
    }

    @Test
    @DisplayName("return event as JSON when it's saved in DB through controller layer")
    void whenCallingCreateReturnPersistedEvent() throws Exception {
        Event event = new Event(1, "event1");
        when(eventService.create(event)).thenReturn(event);
        mvc.perform(post("/api/event")
                .content(objectMapper.writeValueAsString(event))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.id", is(1)))
           .andExpect(jsonPath("$.name", is("event1")));
        verify(eventService, times(1)).create(event);
    }

    @Test
    @DisplayName("return updated event as JSON through controller layer")
    void whenCallingUpdateReturnUpdatedEvent() throws Exception {
        Event event = new Event(2, "event2");
        when(eventService.update(2, event)).thenReturn(event);
        mvc.perform(put("/api/event/{id}", 2)
                .content(objectMapper.writeValueAsString(event))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(2)))
           .andExpect(jsonPath("$.name", is("event2")));
        verify(eventService, times(1)).update(2, event);
    }

    @Test
    @DisplayName("delete event successfully through controller layer")
    void whenCallingDeleteRemoveEventSuccessfully() throws Exception {
        mvc.perform(delete("/api/event/1"))
           .andExpect(status().is(200));
        verify(eventService, times(1)).delete(1);
    }
}