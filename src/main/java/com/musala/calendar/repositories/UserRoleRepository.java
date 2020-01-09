package com.musala.calendar.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.musala.calendar.models.UserRole;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Integer> {
    List<UserRole> findByName(String name);
}
