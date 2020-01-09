package com.musala.calendar.repositories;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import com.musala.calendar.models.User;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@DataJpaTest
@Sql(scripts = "classpath:sql_scripts/createUsersFixture.sql")
@DisplayName("UserRepository should")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("return all users in DB as ArrayList")
    void whenCallingFindAllReturnUserList() {
        List<User> userList = (List<User>) userRepository.findAll();
        assertThat(userList).hasSize(6);
    }

    @Test
    @DisplayName("return the correct user found by id")
    void whenCallingFindByIdReturnUser() {
        User user = userRepository.findById(3).get();
        assertThat(user.getId()).isEqualTo(3);
        assertThat(user.getFirstName()).isEqualTo("USER3");
    }

    @Test
    @DisplayName("return new user when saving and persist new user")
    void whenCallingSaveReturnPersistedUser() {
        User user = new User(7, "USER7");
        userRepository.save(user);
        User savedUser = userRepository.findById(7).get();
        assertThat(savedUser).isEqualTo(user);
    }

    @Test
    @DisplayName("delete user successfully")
    void whenCallingDeleteRemoveUserSuccessfully() {
        List<User> startUserList = (List<User>) userRepository.findAll();
        userRepository.deleteById(3);
        List<User> endUserList = (List<User>) userRepository.findAll();
        assertThat(startUserList).contains(new User(3, "USER3"));
        assertThat(endUserList).doesNotContain(new User(3, "USER3"));
    }
}