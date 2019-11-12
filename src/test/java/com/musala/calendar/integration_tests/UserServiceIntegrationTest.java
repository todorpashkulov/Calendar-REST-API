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
import com.musala.calendar.models.User;
import com.musala.calendar.repositories.UserRepository;
import com.musala.calendar.services.UserServiceImpl;

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
@DisplayName("UserService Integration Test should")
class UserServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("return JSON array with all users through all layers")
    void whenCallingFindAllReturnUserList() throws Exception {
        mockMvc.perform(get("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    @DisplayName("return user as JSON through all layers")
    void whenCallingFindByIdReturnUser() throws Exception {
        final Integer testUserId = 1;
        MvcResult mvcResult = mockMvc.perform(get("/api/user/{id}", testUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isOk())
                                     .andReturn();
        User actualValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
        User expectedValue = userRepository.findById(testUserId).get();
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("return HTTP:Status 404 when searched user isn't found in DB")
    void whenCantFindUserByIdThrowException() throws Exception {
        final Integer testUserId = 4;
        MvcResult mvcResult = mockMvc.perform(get("/api/user/{id}", testUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isNotFound())
                                     .andReturn();
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Entity Not Found",
                UserServiceImpl.getErrorMessage() + testUserId);
        String actualValue = mvcResult.getResponse().getContentAsString();
        String expectedValue = objectMapper.writeValueAsString(errorDetails);
        assertThat(actualValue).contains(expectedValue.substring(expectedValue.indexOf("\"status\"")));
    }

    @Test
    @DisplayName("return user as a JSON when it's saved in DB through all layers")
    void whenCallingCreateReturnPersistedUser() throws Exception {
        User user = new User("USER_FOR_POST", "USEROV4", "USER4@email.com", "USER4PASSWORD");
        MvcResult mvcResult = mockMvc.perform(post("/api/user")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isCreated())
                                     .andReturn();
        User actualValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
        User expectedValue = userRepository.findByFirstName(user.getFirstName()).get(0);
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("return updated user as JSON through all layers")
    void whenCallingUpdateReturnUpdatedUser() throws Exception {
        User user = userRepository.findById(3).get();
        user.setFirstName("USER3UPDATED");
        MvcResult mvcResult = mockMvc.perform(put("/api/user/{id}", user.getId())
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isOk())
                                     .andReturn();
        User actualValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
        User expectedValue = userRepository.findById(user.getId()).get();
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("return HTTP:Status 404 when user for update isn't found in DB")
    void whenCantFindUserToUpdateThrowException() throws Exception {
        User user = new User(4, "EVENT_FOR_PUT");
        MvcResult mvcResult = mockMvc.perform(put("/api/user/{id}", user.getId())
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isNotFound())
                                     .andReturn();
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Entity Not Found",
                UserServiceImpl.getErrorMessage() + user.getId());
        String actualValue = mvcResult.getResponse().getContentAsString();
        String expectedValue = objectMapper.writeValueAsString(errorDetails);
        assertThat(actualValue).contains(expectedValue.substring(expectedValue.indexOf("\"status\"")));
    }

    @Test
    @DisplayName("delete user successfully through controller all layers")
    void whenCallingDeleteRemoveUserSuccessfully() throws Exception {
        User user = userRepository.findById(5).get();
        List<User> startEventTypeList = (List<User>) userRepository.findAll();
        mockMvc.perform(delete("/api/user/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
        List<User> endEventTypeList = (List<User>) userRepository.findAll();
        assertThat(startEventTypeList).contains(user);
        assertThat(endEventTypeList).doesNotContain(user);
    }

    @Test
    @DisplayName("return HTTP:Status 404 when user for deletion isn't found in DB")
    void whenCantFindUserToDeleteThrowException() throws Exception {
        final Integer testEventTypeId = 4;
        MvcResult mvcResult = mockMvc.perform(delete("/api/user/{id}", testEventTypeId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isNotFound())
                                     .andReturn();
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Entity Not Found",
                UserServiceImpl.getErrorMessage() + testEventTypeId);
        String actualValue = mvcResult.getResponse().getContentAsString();
        String expectedValue = objectMapper.writeValueAsString(errorDetails);
        assertThat(actualValue).contains(expectedValue.substring(expectedValue.indexOf("\"status\"")));
    }
}