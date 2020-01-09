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
import com.musala.calendar.models.UserRole;
import com.musala.calendar.repositories.UserRoleRepository;
import com.musala.calendar.services.UserRoleServiceImpl;

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
@DisplayName("UserRoleService Integration Test should")
class UserRoleServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Test
    @DisplayName("return JSON array with all userRoles through all layers")
    void whenCallingFindAllReturnUserList() throws Exception {
        mockMvc.perform(get("/api/userRole")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    @DisplayName("return userRole as JSON through all layers")
    void whenCallingFindByIdReturnUser() throws Exception {
        final Integer testUserRoleId = 1;
        MvcResult mvcResult = mockMvc.perform(get("/api/userRole/{id}", testUserRoleId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isOk())
                                     .andReturn();
        UserRole actualValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                UserRole.class);
        UserRole expectedValue = userRoleRepository.findById(testUserRoleId).get();
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("return HTTP:Status 404 when searched userRole isn't found in DB")
    void whenCantFindUserByIdThrowException() throws Exception {
        final Integer testUserRoleId = 4;
        MvcResult mvcResult = mockMvc.perform(get("/api/userRole/{id}", testUserRoleId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isNotFound())
                                     .andReturn();
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Entity Not Found",
                UserRoleServiceImpl.getErrorMessage() + testUserRoleId);
        String actualValue = mvcResult.getResponse().getContentAsString();
        String expectedValue = objectMapper.writeValueAsString(errorDetails);
        assertThat(actualValue).contains(expectedValue.substring(expectedValue.indexOf("\"status\"")));
    }

    @Test
    @DisplayName("return userRole as a JSON when it's saved in DB through all layers")
    void whenCallingCreateReturnPersistedUser() throws Exception {
        UserRole userRole = new UserRole("USER_ROLE_FOR_POST");

        MvcResult mvcResult = mockMvc.perform(post("/api/userRole")
                .content(objectMapper.writeValueAsString(userRole))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isCreated())
                                     .andReturn();
        UserRole actualValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                UserRole.class);
        UserRole expectedValue = userRoleRepository.findByName(userRole.getName()).get(0);
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("return updated userRole as JSON through all layers")
    void whenCallingUpdateReturnUpdatedUser() throws Exception {
        UserRole userRole = userRoleRepository.findById(3).get();
        userRole.setName("USER_ROLE3UPDATED");
        MvcResult mvcResult = mockMvc.perform(put("/api/userRole/{id}", userRole.getId())
                .content(objectMapper.writeValueAsString(userRole))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isOk())
                                     .andReturn();
        UserRole actualValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                UserRole.class);
        UserRole expectedValue = userRoleRepository.findById(userRole.getId()).get();
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("return HTTP:Status 404 when userRole for update isn't found in DB")
    void whenCantFindUserToUpdateThrowException() throws Exception {
        UserRole userRole = new UserRole(4, "USER_ROLE_FOR_PUT");
        MvcResult mvcResult = mockMvc.perform(put("/api/userRole/{id}", userRole.getId())
                .content(objectMapper.writeValueAsString(userRole))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isNotFound())
                                     .andReturn();
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Entity Not Found",
                UserRoleServiceImpl.getErrorMessage() + userRole.getId());
        String actualValue = mvcResult.getResponse().getContentAsString();
        String expectedValue = objectMapper.writeValueAsString(errorDetails);
        assertThat(actualValue).contains(expectedValue.substring(expectedValue.indexOf("\"status\"")));
    }

    @Test
    @DisplayName("delete userRole successfully through controller all layers")
    void whenCallingDeleteRemoveUserSuccessfully() throws Exception {
        UserRole userRole = userRoleRepository.findById(5).get();
        List<UserRole> startEventTypeList = (List<UserRole>) userRoleRepository.findAll();
        mockMvc.perform(delete("/api/userRole/{id}", userRole.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
        List<UserRole> endEventTypeList = (List<UserRole>) userRoleRepository.findAll();
        assertThat(startEventTypeList).contains(userRole);
        assertThat(endEventTypeList).doesNotContain(userRole);
    }

    @Test
    @DisplayName("return HTTP:Status 404 when userRole for deletion isn't found in DB")
    void whenCantFindUserToDeleteThrowException() throws Exception {
        final Integer testUserRoleId = 4;
        MvcResult mvcResult = mockMvc.perform(delete("/api/userRole/{id}", testUserRoleId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isNotFound())
                                     .andReturn();
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Entity Not Found",
                UserRoleServiceImpl.getErrorMessage() + testUserRoleId);
        String actualValue = mvcResult.getResponse().getContentAsString();
        String expectedValue = objectMapper.writeValueAsString(errorDetails);
        assertThat(actualValue).contains(expectedValue.substring(expectedValue.indexOf("\"status\"")));
    }
}