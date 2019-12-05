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
import com.musala.calendar.models.EventType;
import com.musala.calendar.repositories.EventTypeRepository;
import com.musala.calendar.services.EventTypeServiceImpl;

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
@DisplayName("EventTypeService Integration Test should")
class EventTypeServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Test
    @DisplayName("return JSON array with all eventTypes through all layers")
    void whenCallingFindAllReturnEventTypeList() throws Exception {
        mockMvc.perform(get("/api/eventType")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    @DisplayName("return eventType as JSON through all layers")
    void whenCallingFindByIdReturnEventType() throws Exception {
        final Integer testEventTypeId = 1;
        MvcResult mvcResult = mockMvc.perform(get("/api/eventType/{id}", testEventTypeId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isOk())
                                     .andReturn();
        EventType actualValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                EventType.class);
        EventType expectedValue = eventTypeRepository.findById(testEventTypeId).get();
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("return HTTP:Status 404 when searched eventType isn't found in DB")
    void whenCantFindEventTypeByIdThrowException() throws Exception {
        final Integer testEventId = 4;
        MvcResult mvcResult = mockMvc.perform(get("/api/eventType/{id}", testEventId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isNotFound())
                                     .andReturn();
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Entity Not Found",
                EventTypeServiceImpl.getErrorMessage() + testEventId);
        String actualValue = mvcResult.getResponse().getContentAsString();
        String expectedValue = objectMapper.writeValueAsString(errorDetails);
        assertThat(actualValue).contains(expectedValue.substring(expectedValue.indexOf("\"status\"")));
    }

    @Test
    @DisplayName("return eventType as a JSON when it's saved in DB through all layers")
    void whenCallingCreateReturnPersistedEventType() throws Exception {
        EventType eventType = new EventType("EVENT_TYPE_FOR_POST");
        MvcResult mvcResult = mockMvc.perform(post("/api/eventType")
                .content(objectMapper.writeValueAsString(eventType))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isCreated())
                                     .andReturn();
        EventType actualValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                EventType.class);
        EventType expectedValue = eventTypeRepository.findByName(eventType.getName()).get(0);
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("return updated eventType as JSON through all layers")
    void whenCallingUpdateReturnUpdatedEventType() throws Exception {
        EventType eventType = eventTypeRepository.findById(3).get();
        eventType.setName("EVENT_TYPE3UPDATED");
        MvcResult mvcResult = mockMvc.perform(put("/api/eventType/{id}", eventType.getId())
                .content(objectMapper.writeValueAsString(eventType))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isOk())
                                     .andReturn();
        EventType actualValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                EventType.class);
        EventType expectedValue = eventTypeRepository.findById(eventType.getId()).get();
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("return HTTP:Status 404 when eventType for update isn't found in DB")
    void whenCantFindEventTypeToUpdateThrowException() throws Exception {
        EventType eventType = new EventType(4, "EVENT_FOR_PUT");
        MvcResult mvcResult = mockMvc.perform(put("/api/eventType/{id}", eventType.getId())
                .content(objectMapper.writeValueAsString(eventType))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isNotFound())
                                     .andReturn();
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Entity Not Found",
                EventTypeServiceImpl.getErrorMessage() + eventType.getId());
        String actualValue = mvcResult.getResponse().getContentAsString();
        String expectedValue = objectMapper.writeValueAsString(errorDetails);
        assertThat(actualValue).contains(expectedValue.substring(expectedValue.indexOf("\"status\"")));
    }

    @Test
    @DisplayName("delete eventType successfully through controller all layers")
    void whenCallingDeleteRemoveEventTypeSuccessfully() throws Exception {
        EventType eventType = eventTypeRepository.findById(5).get();
        List<EventType> startEventTypeList = (List<EventType>) eventTypeRepository.findAll();
        mockMvc.perform(delete("/api/eventType/{id}", eventType.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
        List<EventType> endEventTypeList = (List<EventType>) eventTypeRepository.findAll();
        assertThat(startEventTypeList).contains(eventType);
        assertThat(endEventTypeList).doesNotContain(eventType);
    }

    @Test
    @DisplayName("return HTTP:Status 404 when eventType for deletion isn't found in DB")
    void whenCantFindEventTypeToDeleteThrowException() throws Exception {
        final Integer testEventTypeId = 4;
        MvcResult mvcResult = mockMvc.perform(delete("/api/eventType/{id}", testEventTypeId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isNotFound())
                                     .andReturn();
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Entity Not Found",
                EventTypeServiceImpl.getErrorMessage() + testEventTypeId);
        String actualValue = mvcResult.getResponse().getContentAsString();
        String expectedValue = objectMapper.writeValueAsString(errorDetails);
        assertThat(actualValue).contains(expectedValue.substring(expectedValue.indexOf("\"status\"")));
    }
}