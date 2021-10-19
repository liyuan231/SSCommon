package com.example.service;

import com.example.exception.DynamicCodeInvalidException;
import com.example.exception.UsernameAlreadyExistException;

public interface RegisterOperation {
    /**
     * 用户注册一定会用到用户名，密码，验证码
     * 若用户名已经存在了，那毫无疑问是修改密码
     *
     * @param username
     * @param password
     * @return
     * @throws UsernameAlreadyExistException
     * @throws DynamicCodeInvalidException
     */
    void registerOrRestPassword(String username, String password, String code) throws UsernameAlreadyExistException, DynamicCodeInvalidException;
}
