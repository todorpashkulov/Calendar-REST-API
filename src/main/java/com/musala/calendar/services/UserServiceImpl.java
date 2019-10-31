package com.musala.calendar.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musala.calendar.exeptions.NotFoundInDBException;
import com.musala.calendar.models.User;
import com.musala.calendar.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private static final String ERROR_MESSAGE = "Can't find user with ID: ";

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User findById(Integer id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundInDBException(ERROR_MESSAGE + id));
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(Integer id, User updatedUser) {
        User oldUser = userRepository.findById(id).get();
        updatedUser.setId(id);
        updatedUser.setEmail(updatedUser.getEmail() == null
                ? oldUser.getEmail()
                : updatedUser.getEmail());
        updatedUser.setFirstName(updatedUser.getFirstName() == null
                ? oldUser.getFirstName()
                : updatedUser.getFirstName());
        updatedUser.setLastName(updatedUser.getLastName() == null
                ? oldUser.getLastName()
                : updatedUser.getFirstName());
        updatedUser.setPassword(updatedUser.getLastName() == null
                ? oldUser.getLastName()
                : updatedUser.getFirstName());
        if (updatedUser.getEventList() == null) {
            updatedUser.setEventList(new ArrayList<>());
        }
        updatedUser.getEventList().addAll(oldUser.getEventList());
        return userRepository.save(updatedUser);
    }

    @Override
    public void delete(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new NotFoundInDBException(ERROR_MESSAGE + id);
        }
    }
}
