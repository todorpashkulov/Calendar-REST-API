package com.musala.calendar.repositories;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import com.musala.calendar.models.UserRole;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@DataJpaTest
@Sql(scripts = "classpath:sql_scripts/createUserRolesFixture.sql")
@DisplayName("UserRoleRepository should")
class UserRoleRepositoryTest {

    @Autowired
    UserRoleRepository userRoleRepository;

    @Test
    @DisplayName("return all userRoles in DB as ArrayList")
    void whenCallingFindAllReturnUserList() {
        List<UserRole> userRoleList = (List<UserRole>) userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(6);
    }

    @Test
    @DisplayName("return the correct userRole found by id")
    void whenCallingFindByIdReturnUser() {
        UserRole userRole = userRoleRepository.findById(3).get();
        assertThat(userRole.getId()).isEqualTo(3);
        assertThat(userRole.getName()).isEqualTo("USER_ROLE3");
    }

    @Test
    @DisplayName("return new userRole when saving and persist new user")
    void whenCallingSaveReturnPersistedUser() {
        UserRole userRole = new UserRole(7, "USER_ROLE7");
        userRoleRepository.save(userRole);
        UserRole savedUserRole = userRoleRepository.findById(7).get();
        assertThat(savedUserRole).isEqualTo(userRole);
    }

    @Test
    @DisplayName("delete userRole successfully")
    void whenCallingDeleteRemoveUserSuccessfully() {
        List<UserRole> startUserRoleList = (List<UserRole>) userRoleRepository.findAll();
        userRoleRepository.deleteById(3);
        List<UserRole> endUserRoleList = (List<UserRole>) userRoleRepository.findAll();
        assertThat(startUserRoleList).contains(new UserRole(3, "USER_ROLE3", new ArrayList<>()));
        assertThat(endUserRoleList).doesNotContain(new UserRole(3, "USER_ROLE3", new ArrayList<>()));
    }
}