package com.example.authorization.service;

import com.example.authorization.controller.*;
import com.example.authorization.entity.*;

public interface AuthorizationService {
    User registerUser(UserRequest userRequest);

    String loginUser(UserRequest userRequest);

    UserResponse getUserDetails(String jwt);
}
