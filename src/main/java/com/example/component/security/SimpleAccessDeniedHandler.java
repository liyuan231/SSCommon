package com.example.component.security;

import com.example.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SimpleAccessDeniedHandler implements AccessDeniedHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        logger.warn(accessDeniedException.getMessage());
        ResponseUtils.printJson(response, ResponseUtils.build(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value(), accessDeniedException.getMessage()));
    }
}
