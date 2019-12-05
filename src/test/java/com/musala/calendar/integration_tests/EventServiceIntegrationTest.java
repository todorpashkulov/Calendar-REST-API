package com.musala.calendar.integration_tests;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.musala.calendar.exeptions.ErrorDetails;
import com.musala.calendar.models.Event;
import com.musala.calendar.repositories.EventRepository;
import com.musala.calendar.repositories.EventTypeRepository;
import com.musala.calendar.repositories.UserRepository;
import com.musala.calendar.services.EventServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql(scripts = "classpath:sql_scripts/IntegrationTestFixture.sql")
@DisplayName("EventService Integration Test should")
class EventServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("return JSON array with all events through all layers")
    void whenCallingFindAllReturnEventList() throws Exception {
        mockMvc.perform(get("/api/event")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(7)));
    }

    @Test
    @DisplayName("return event as JSON through all layers")
    void whenCallingFindByIdReturnEvent() throws Exception {
        final Integer testEventId = 1;
        MvcResult mvcResult = mockMvc.perform(get("/api/event/{id}", testEventId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isOk())
                                     .andReturn();
        Event actualValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Event.class);
        Event expectedValue = eventRepository.findById(testEventId).get();
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("return HTTP:Status 404 when searched event isn't found in DB")
    void whenCantFindEventByIdThrowException() throws Exception {
        final Integer testEventId = 7;
        MvcResult mvcResult = mockMvc.perform(get("/api/event/{id}", testEventId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isNotFound())
                                     .andReturn();
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Entity Not Found",
                EventServiceImpl.getErrorMessage() + testEventId);
        String actualValue = mvcResult.getResponse().getContentAsString();
        String expectedValue = objectMapper.writeValueAsString(errorDetails);
        assertThat(actualValue).contains(expectedValue.substring(expectedValue.indexOf("\"status\"")));
    }

    @Test
    @DisplayName("return event as a JSON when it's saved in DB through all layers")
    void whenCallingCreateReturnPersistedEvent() throws Exception {
        Event event = new Event("EVENT_FOR_POST",
                eventTypeRepository.findById(1).get(),
                userRepository.findById(1).get());
        MvcResult mvcResult = mockMvc.perform(post("/api/event")
                .content(objectMapper.writeValueAsString(event))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isCreated())
                                     .andReturn();
        Event actualValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Event.class);
        Event expectedValue = eventRepository.findByName(event.getName()).get(0);
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("return updated event as JSON through all layers")
    void whenCallingUpdateReturnUpdatedEvent() throws Exception {
        Event event = eventRepository.findById(3).get();
        event.setName("EVENT3UPDATED");
        MvcResult mvcResult = mockMvc.perform(put("/api/event/{id}", event.getId())
                .content(objectMapper.writeValueAsString(event))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isOk())
                                     .andReturn();
        Event actualValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Event.class);
        Event expectedValue = eventRepository.findById(event.getId()).get();
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("return HTTP:Status 404 when event for update isn't found in DB")
    void whenCantFindEventToUpdateThrowException() throws Exception {
        Event event = new Event(7,
                "EVENT_FOR_PUT",
                eventTypeRepository.findById(1).get(),
                userRepository.findById(1).get());
        MvcResult mvcResult = mockMvc.perform(put("/api/event/{id}", event.getId())
                .content(objectMapper.writeValueAsString(event))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isNotFound())
                                     .andReturn();
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Entity Not Found",
                EventServiceImpl.getErrorMessage() + event.getId());
        String actualValue = mvcResult.getResponse().getContentAsString();
        String expectedValue = objectMapper.writeValueAsString(errorDetails);
        assertThat(actualValue).contains(expectedValue.substring(expectedValue.indexOf("\"status\"")));
    }

    @Test
    @DisplayName("delete event successfully through controller all layers")
    void whenCallingDeleteRemoveEventSuccessfully() throws Exception {
        Event testEvent = eventRepository.findById(3).get();
        List<Event> startEventList = (List<Event>) eventRepository.findAll();
        mockMvc.perform(delete("/api/event/{id}", testEvent.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
        List<Event> endEventList = (List<Event>) eventRepository.findAll();
        assertThat(startEventList).contains(testEvent);
        assertThat(endEventList).doesNotContain(testEvent);
    }

    @Test
    @DisplayName("return HTTP:Status 404 when event for deletion isn't found in DB")
    void whenCantFindEventToDeleteThrowException() throws Exception {
        final Integer testEventId = 7;
        MvcResult mvcResult = mockMvc.perform(delete("/api/event/{id}", testEventId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isNotFound())
                                     .andReturn();
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Entity Not Found",
                EventServiceImpl.getErrorMessage() + testEventId);
        String actualValue = mvcResult.getResponse().getContentAsString();
        String expectedValue = objectMapper.writeValueAsString(errorDetails);
        assertThat(actualValue).contains(expectedValue.substring(expectedValue.indexOf("\"status\"")));
    }
}