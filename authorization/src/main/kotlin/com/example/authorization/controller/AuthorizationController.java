package com.example.authorization.controller;

import com.example.authorization.service.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody UserRequest userRequest) {
        authorizationService.registerUser(userRequest);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String loginUser(@RequestBody UserRequest userRequest) {
        return authorizationService.loginUser(userRequest);
    }

    @GetMapping("/user-info")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserDetailsAfterLogin(@RequestHeader("Authorization") String jwt) {
        return authorizationService.getUserDetails(jwt);
    }
}
