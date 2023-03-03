package com.example.authorization.config;

import com.example.authorization.model.User;
import com.example.authorization.model.*;
import com.example.authorization.repository.*;
import lombok.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;


@Service
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow();
        return new SecurityUser(user);
    }
}
