package com.example.authorization.config;

import com.example.authorization.model.User;
import com.example.authorization.model.*;
import com.example.authorization.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import java.util.*;


@Service
public class CustomUserDetails implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow();
        return new SecurityUser(user);
    }

}
