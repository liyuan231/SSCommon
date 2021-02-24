package com.example.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtTokenExpiredAuthenticationException extends AuthenticationException {
    public JwtTokenExpiredAuthenticationException(String msg) {
        super(msg);
    }
}
