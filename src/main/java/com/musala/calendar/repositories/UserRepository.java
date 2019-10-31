package com.musala.calendar.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.musala.calendar.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
}
