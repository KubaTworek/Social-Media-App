package com.example.authorization.controller;

import com.example.authorization.constants.*;
import com.example.authorization.model.*;
import com.example.authorization.repository.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.crypto.password.*;
import org.springframework.web.bind.annotation.*;

import javax.crypto.*;
import java.nio.charset.*;
import java.util.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthoritiesRepository authoritiesRepository;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRequest userRequest) {
        Authorities authority = getAuthority(userRequest.role());
        User userCreated = userRepository.save(new User(1, userRequest.username(), passwordEncoder.encode(userRequest.password()), authority));

        return new ResponseEntity<>(userCreated, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserRequest userRequest) {
        String username = userRequest.username();
        String password = userRequest.password();
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new BadCredentialsException("No user registered with this username!"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getAuthorities().getAuthority()));
            SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
            String jwt = Jwts.builder().setIssuer("Eazy Bank").setSubject("JWT Token")
                    .claim("username", username)
                    .claim("authorities", populateAuthorities(authorities))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + 300000000))
                    .signWith(key).compact();
            return new ResponseEntity<>(jwt, HttpStatus.ACCEPTED);
        } else {
            throw new BadCredentialsException("Invalid password!");
        }
    }

    @GetMapping("/user-info")
    public UserResponse getUserDetailsAfterLogin(@RequestHeader("Authorization") String jwt) {
        SecretKey key = Keys.hmacShaKeyFor(
                SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        String username = String.valueOf(claims.get("username"));
        String authorities = (String) claims.get("authorities");
        return new UserResponse(username, authorities);
    }

    private Authorities getAuthority(String authority) {
        return authoritiesRepository.findAuthoritiesByAuthority(authority)
                .orElse(authoritiesRepository.findAuthoritiesByAuthority("ROLE_USER")
                        .orElse(null));
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }
}