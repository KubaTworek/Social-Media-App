package com.example.authorization.model;

import org.springframework.security.authentication.*;
import org.springframework.security.core.*;

import java.io.*;
import java.util.*;

public class UsernamePasswordAuthentication extends UsernamePasswordAuthenticationToken {
    @Serial
    private static final long serialVersionUID = 1L;

    public UsernamePasswordAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public UsernamePasswordAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
