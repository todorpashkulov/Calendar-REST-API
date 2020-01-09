package com.musala.calendar.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.musala.calendar.dto.AuthenticationRequest;
import com.musala.calendar.exeptions.NotFoundInDBException;
import com.musala.calendar.models.User;
import com.musala.calendar.repositories.UserRepository;
import com.musala.calendar.security.JwtUtil;
import com.musala.calendar.security.UserDetailsServiceImpl;

@Service
public class UserServiceImpl implements UserService {
    private static final String ERROR_MESSAGE = "Can't find user with ID: ";

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public static String getErrorMessage() {
        return ERROR_MESSAGE;
    }

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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    //TODO:fix passwordEncoding
    public User update(Integer id, User updatedUser) {
        if (userRepository.existsById(id)) {
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
                    : updatedUser.getLastName());
            updatedUser.setPassword(updatedUser.getPassword() == null
                    ? oldUser.getPassword()
                    : updatedUser.getPassword());
            if (updatedUser.getEventList() == null) {
                updatedUser.setEventList(new ArrayList<>());
            }
            updatedUser.getEventList().addAll(oldUser.getEventList());
            return userRepository.save(updatedUser);
        } else {
            throw new NotFoundInDBException(ERROR_MESSAGE + id);
        }
    }

    @Override
    public void delete(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new NotFoundInDBException(ERROR_MESSAGE + id);
        }
    }

    @Override
    public String createToken(AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        return jwtTokenUtil.generateToken(userDetails);
    }
}
