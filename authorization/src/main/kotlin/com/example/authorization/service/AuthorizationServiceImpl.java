package com.example.authorization.service;

import com.example.authorization.controller.*;
import com.example.authorization.entity.*;
import com.example.authorization.exception.*;
import com.example.authorization.repository.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.*;
import lombok.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import javax.crypto.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

import static com.example.authorization.constants.SecurityConstants.*;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {
    private final UserRepository userRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserRequest userRequest) {
        String username = userRequest.username();
        String password = userRequest.password();
        String role = userRequest.role();

        validateUserWithThatUsernameDoesNotExist(username);
        Authorities authority = getAuthority(role);
        User newUser = buildUser(username, password, authority);

        return userRepository.save(newUser);
    }

    @Override
    public String loginUser(UserRequest userRequest) {
        String username = userRequest.username();
        String password = userRequest.password();
        User user = getUserByUsername(username);
        validPasswords(password, user.getPassword());

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getAuthorities().getAuthority())
        );
        SecretKey key = createSecretKey();

        return buildJwt(username, authorities, key);
    }

    @Override
    public UserResponse getUserDetails(String jwt) {
        Claims claims = parseJwtClaims(jwt);
        String username = String.valueOf(claims.get("username"));
        String authorities = String.valueOf(claims.get("authorities"));
        return new UserResponse(username, authorities);
    }

    private User buildUser(String username, String password, Authorities authority) {
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .authorities(authority)
                .build();
    }

    private String buildJwt(String username, List<GrantedAuthority> authorities, SecretKey key) {
        return Jwts.builder()
                .setIssuer("Social Media")
                .setSubject("JWT Token")
                .claim("username", username)
                .claim("authorities", populateAuthorities(authorities))
                .setIssuedAt(new Date())
                .setExpiration(new Date(JWT_EXPIRE_TIME))
                .signWith(key)
                .compact();
    }

    private SecretKey createSecretKey() {
        return Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private void validPasswords(String passwordProvided, String passwordRegistered) {
        if (!isValidPasswords(passwordProvided, passwordRegistered)) {
            throw new WrongCredentialsException("Invalid password!");
        }
    }

    private void validateUserWithThatUsernameDoesNotExist(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("User with this username already exists!");
        }
    }

    private boolean isValidPasswords(String passwordProvided, String passwordRegistered) {
        return passwordEncoder.matches(passwordProvided, passwordRegistered);
    }

    private User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("No user registered with this username!"));
    }

    private Authorities getAuthority(String authority) {
        return authoritiesRepository.findAuthoritiesByAuthority(authority)
                .orElse(authoritiesRepository.findAuthoritiesByAuthority("ROLE_USER")
                        .orElseThrow(() -> new RuntimeException("No authorities found!")));
    }

    private Claims parseJwtClaims(String jwt) {
        SecretKey key = Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        return collection.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}
