package com.example.service.impl;

import com.example.exception.DynamicCodeInvalidException;
import com.example.exception.UsernameAlreadyExistException;
import com.example.service.RegisterOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class RegisterOperationImpl implements RegisterOperation {

    @Autowired
    private InMemoryUserDetailsManager userDetailsManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerOrRestPassword(String username, String password,String code) throws UsernameAlreadyExistException, DynamicCodeInvalidException {
        try {
            userDetailsManager.createUser(new User(username, passwordEncoder.encode(password), AuthorityUtils.NO_AUTHORITIES));
        } catch (IllegalArgumentException e) {
            throw new UsernameAlreadyExistException("user already exist");
        }
    }
}
