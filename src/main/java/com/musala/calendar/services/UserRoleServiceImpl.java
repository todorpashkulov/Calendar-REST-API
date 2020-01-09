package com.musala.calendar.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musala.calendar.exeptions.NotFoundInDBException;
import com.musala.calendar.models.UserRole;
import com.musala.calendar.repositories.UserRoleRepository;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    private static final String ERROR_MESSAGE = "Can't find user role with ID: ";

    @Autowired
    UserRoleRepository userRoleRepository;

    public static String getErrorMessage() {
        return ERROR_MESSAGE;
    }

    @Override
    public List<UserRole> findAll() {
        return (List<UserRole>) userRoleRepository.findAll();
    }

    @Override
    public UserRole findById(Integer id) {
        return userRoleRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundInDBException(ERROR_MESSAGE + id));
    }

    @Override
    public UserRole create(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @Override
    public UserRole update(Integer id, UserRole updatedEvent) {
        if (userRoleRepository.existsById(id)) {
            updatedEvent.setId(id);
            return userRoleRepository.save(updatedEvent);
        } else {
            throw new NotFoundInDBException(ERROR_MESSAGE + id);
        }
    }

    @Override
    public void delete(Integer id) {
        if (userRoleRepository.existsById(id)) {
            userRoleRepository.deleteById(id);
        } else {
            throw new NotFoundInDBException(ERROR_MESSAGE + id);
        }
    }
}
