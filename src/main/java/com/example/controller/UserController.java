package com.example.controller;


import com.example.constants.UserStatusCode;
import com.example.exception.DynamicCodeInvalidException;
import com.example.exception.UsernameAlreadyExistException;
import com.example.service.RegisterOperation;
import com.example.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("${api.baseUri:/}")
public class UserController {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private RegisterOperation registerOperation;

    @PostMapping(value = "/login", produces = {"application/json;charset=utf-8"})
    public void login(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof String)) {
            SecurityContextHolder.clearContext();
        }
        request.login(username, password);
        authenticationSuccessHandler.onAuthenticationSuccess(request, response, SecurityContextHolder.getContext().getAuthentication());
    }

    @PostMapping(value = "/register", produces = {"application/json;charset=utf-8"})
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(value = "code", required = false) String code) throws DynamicCodeInvalidException, UsernameAlreadyExistException {
        registerOperation.registerOrRestPassword(username, password,code);
        return ResponseUtils.build(UserStatusCode.SUCCESS, "success");
    }

}
