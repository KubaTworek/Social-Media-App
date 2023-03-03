package com.example.authorization.filter;

import java.io.IOException;
import java.util.logging.Logger;
import jakarta.servlet.*;


public class AuthoritiesLoggingAtFilter implements Filter {

    private final Logger LOG =
            Logger.getLogger(AuthoritiesLoggingAtFilter.class.getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        LOG.info("Authentication Validation is in progress");
        chain.doFilter(request, response);
    }

}
