package com.example.exceptionHandler;

import com.example.constants.UserStatusCode;
import com.example.exception.UsernameAlreadyExistException;
import com.example.utils.ResponseUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler({UsernameAlreadyExistException.class})
    public String usernameAlreadyExistException(UsernameAlreadyExistException e) {
        return ResponseUtils.build(UserStatusCode.USERNAME_ALREADY_EXIST, e.getMessage());
    }
}
