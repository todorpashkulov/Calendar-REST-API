package com.musala.calendar.services;

import com.musala.calendar.dto.AuthenticationRequest;
import com.musala.calendar.models.User;

public interface UserService extends BaseCRUDService<User> {
    String createToken(AuthenticationRequest authenticationRequest) throws Exception;
}
