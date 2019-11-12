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

import com.musala.calendar.models.EventType;
import com.musala.calendar.services.EventTypeService;

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

@WebMvcTest(controllers = EventTypeController.class)
@DisplayName("EventTypeController should")
class EventTypeControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private EventTypeService eventTypeService;

    @Test
    @DisplayName("return JSON array with all eventTypes through controller layer")
    void whenCallingFindAllReturnEventTypeList() throws Exception {
        when(eventTypeService.findAll()).thenReturn(Arrays.asList(new EventType("eventType1"),
                new EventType("eventType2"),
                new EventType("eventType3")));
        mvc.perform(get("/api/eventType").accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(3)))
           .andExpect(jsonPath("$[0].name", is("eventType1")))
           .andExpect(jsonPath("$[1].name", is("eventType2")))
           .andExpect(jsonPath("$[2].name", is("eventType3")));
        verify(eventTypeService, times(1)).findAll();
    }

    @Test
    @DisplayName("return eventType as JSON through controller layer")
    void whenCallingFindByIdReturnEventType() throws Exception {
        EventType eventType = new EventType(1, "eventType1");
        when(eventTypeService.findById(1)).thenReturn(eventType);
        mvc.perform(get("/api/eventType/{id}", 1).accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(1)))
           .andExpect(jsonPath("$.name", is("eventType1")));
        verify(eventTypeService, times(1)).findById(1);
    }

    @Test
    @DisplayName("return eventType as JSON when it's saved in DB through controller layer")
    void whenCallingCreateReturnPersistedEventType() throws Exception {
        EventType eventType = new EventType(1, "eventType1");
        when(eventTypeService.create(eventType)).thenReturn(eventType);
        mvc.perform(post("/api/eventType")
                .content(objectMapper.writeValueAsString(eventType))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.id", is(1)))
           .andExpect(jsonPath("$.name", is("eventType1")));
        verify(eventTypeService, times(1)).create(eventType);
    }

    @Test
    @DisplayName("return updated eventType as JSON through controller layer")
    void whenCallingUpdateReturnUpdatedEventType() throws Exception {
        EventType eventType = new EventType(2, "event2");
        when(eventTypeService.update(2, eventType)).thenReturn(eventType);
        mvc.perform(put("/api/eventType/{id}", 2)
                .content(objectMapper.writeValueAsString(eventType))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(2)))
           .andExpect(jsonPath("$.name", is("event2")));
        verify(eventTypeService, times(1)).update(2, eventType);
    }

    @Test
    @DisplayName("delete eventType successfully through controller layer")
    void whenCallingDeleteRemoveEventTypeSuccessfully() throws Exception {
        mvc.perform(delete("/api/eventType/1"))
           .andExpect(status().is(200));
        verify(eventTypeService, times(1)).delete(1);
    }
}