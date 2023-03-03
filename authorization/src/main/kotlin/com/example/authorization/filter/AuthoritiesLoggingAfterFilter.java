package com.example.authorization.filter;

import java.io.IOException;
import java.util.logging.Logger;

import jakarta.servlet.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthoritiesLoggingAfterFilter implements Filter {

    private final Logger LOG =
            Logger.getLogger(AuthoritiesLoggingAfterFilter.class.getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(null!=authentication) {
            LOG.info("User "+authentication.getName()+" is successfully authenticated and "
                    + "has the authorities "+authentication.getAuthorities().toString());
        }

        chain.doFilter(request, response);
    }

}
