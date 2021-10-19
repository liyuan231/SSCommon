package com.example.component.security;

import com.example.component.jwt.JwtTokenGenerator;
import com.example.component.jwt.JwtTokenPair;
import com.example.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
@ConditionalOnProperty(prefix = "jwt.config", name = "enabled")
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Set<String> roles = new HashSet<>();
        for (GrantedAuthority authority : authorities) {
            roles.add(authority.getAuthority());
        }
        Map<String, String> additional = new HashMap<>();
        JwtTokenPair jwtTokenPair = jwtTokenGenerator.jwtTokenPairWithUsername(userDetails.getUsername(), roles, additional);
        Map<String, Object> data = new HashMap<>();
        data.put("token", jwtTokenPair);
        String build = ResponseUtils.build(HttpStatus.OK.value(), "Login success!", data);
        ResponseUtils.printJson(response, build);
    }
}
