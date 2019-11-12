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

import com.musala.calendar.models.User;
import com.musala.calendar.services.UserService;

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

@WebMvcTest(controllers = UserController.class)
@DisplayName("UserController should")
class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Test
    @DisplayName("return JSON array with all users through controller layer")
    void whenCallingFindAllReturnUserList() throws Exception {
        when(userService.findAll()).thenReturn(Arrays.asList(new User("user1"),
                new User("user2"),
                new User("user3")));
        mvc.perform(get("/api/user").accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(3)))
           .andExpect(jsonPath("$[0].firstName", is("user1")))
           .andExpect(jsonPath("$[1].firstName", is("user2")))
           .andExpect(jsonPath("$[2].firstName", is("user3")));
        verify(userService, times(1)).findAll();
    }

    @Test
    @DisplayName("return user as JSON through controller layer")
    void whenCallingFindByIdReturnUser() throws Exception {
        User user = new User(1, "user1");
        when(userService.findById(1)).thenReturn(user);
        mvc.perform(get("/api/user/{id}", 1).accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(1)))
           .andExpect(jsonPath("$.firstName", is("user1")));
        verify(userService, times(1)).findById(1);
    }

    @Test
    @DisplayName("return user as JSON when it's saved in DB through controller layer")
    void whenCallingCreateReturnPersistedUser() throws Exception {
        User user = new User(1, "user1");
        when(userService.create(user)).thenReturn(user);
        mvc.perform(post("/api/user")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.id", is(1)))
           .andExpect(jsonPath("$.firstName", is("user1")));
        verify(userService, times(1)).create(user);
    }

    @Test
    @DisplayName("return updated user as JSON through controller layer")
    void whenCallingUpdateReturnUpdatedUser() throws Exception {
        User user = new User(2, "user2");
        when(userService.update(2, user)).thenReturn(user);
        mvc.perform(put("/api/user/{id}", 2)
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(2)))
           .andExpect(jsonPath("$.firstName", is("user2")));
        verify(userService, times(1)).update(2, user);
    }

    @Test
    @DisplayName("delete user successfully through controller layer")
    void whenCallingDeleteRemoveUserSuccessfully() throws Exception {
        mvc.perform(delete("/api/user/1"))
           .andExpect(status().is(200));
        verify(userService, times(1)).delete(1);
    }
}