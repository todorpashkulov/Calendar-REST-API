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

import com.musala.calendar.models.UserRole;
import com.musala.calendar.services.UserRoleService;

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

@WebMvcTest(controllers = UserRoleController.class)
@DisplayName("UserRoleController should")
class UserRoleControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserRoleService userRoleService;

    @Test
    @DisplayName("return JSON array with all userRoles through controller layer")
    void whenCallingFindAllReturnUserRoleList() throws Exception {
        when(userRoleService.findAll()).thenReturn(Arrays.asList(new UserRole("userRole1"),
                new UserRole("userRole2"),
                new UserRole("userRole3")));
        mvc.perform(get("/api/userRole").accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(3)))
           .andExpect(jsonPath("$[0].name", is("userRole1")))
           .andExpect(jsonPath("$[1].name", is("userRole2")))
           .andExpect(jsonPath("$[2].name", is("userRole3")));
        verify(userRoleService, times(1)).findAll();
    }

    @Test
    @DisplayName("return userRole as JSON through controller layer")
    void whenCallingFindByIdReturnUserRole() throws Exception {
        UserRole userRole = new UserRole(1, "userRole1");
        when(userRoleService.findById(1)).thenReturn(userRole);
        mvc.perform(get("/api/userRole/{id}", 1).accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(1)))
           .andExpect(jsonPath("$.name", is("userRole1")));
        verify(userRoleService, times(1)).findById(1);
    }

    @Test
    @DisplayName("return userRole as JSON when it's saved in DB through controller layer")
    void whenCallingCreateReturnPersistedUser() throws Exception {
        UserRole userRole = new UserRole(1, "userRole1");
        when(userRoleService.create(userRole)).thenReturn(userRole);
        mvc.perform(post("/api/userRole")
                .content(objectMapper.writeValueAsString(userRole))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.id", is(1)))
           .andExpect(jsonPath("$.name", is("userRole1")));
        verify(userRoleService, times(1)).create(userRole);
    }

    @Test
    @DisplayName("return updated userRole as JSON through controller layer")
    void whenCallingUpdateReturnUpdatedUserRole() throws Exception {
        UserRole userRole = new UserRole(2, "userRole2");
        when(userRoleService.update(2, userRole)).thenReturn(userRole);
        mvc.perform(put("/api/userRole/{id}", 2)
                .content(objectMapper.writeValueAsString(userRole))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(2)))
           .andExpect(jsonPath("$.name", is("userRole2")));
        verify(userRoleService, times(1)).update(2, userRole);
    }

    @Test
    @DisplayName("delete userRole successfully through controller layer")
    void whenCallingDeleteRemoveUserRoleSuccessfully() throws Exception {
        mvc.perform(delete("/api/userRole/1"))
           .andExpect(status().is(200));
        verify(userRoleService, times(1)).delete(1);
    }
}