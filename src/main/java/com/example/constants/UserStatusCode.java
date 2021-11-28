package com.example.constants;

public interface UserStatusCode {

    int SUCCESS = 200;
    /**
     * 注册时：用户名已存在
     */
    int USERNAME_ALREADY_EXIST = 50001;

    /**
     * 注册时：动态验证码错误；
     */
    int DYNAMIC_CODE_INVALID = 50002;

    /**
     * 注册时: 动态码过期
     */
    int DYNAMIC_CODE_EXPIRED = 50003;

    /**
     * Jwt部分缺失
     */
    int JWT_TOKEN_MISSING = 50004;

    /**
     * Jwt过期
     */
    int JWT_TOKEN_EXPIRED = 50005;
}
