## 基于 Spring Security的JWT通用后台安全框架
前言：每次使用SS框架时，总要重新书写JWT部分，因此我将其抽取出来了

## 开始使用
使用gradle将其打包供其他项目使用
./gradlew build

打成的包会在 build/libs/下，将其引入其他项目即可使用

可选择实现
UserDetailsService（登录时用户从哪获取）, RegisterOperation（注册或修改用户账号密码） 接口
默认使用内存保存用户


默认注册登录都使用内存

|操作|接口|
|----|----|
|默认登录|http://localhost:80/login?username=xxx&password=xxx 需要自行实现 UserDetailsService接口，该接口与登录相关|
|默认注册|http://localhost:80/register?username=xxx&password=xxx 需自行实现 RegisterOperation 接口，该接口有注册时相关操作|
|默认修改密码 |http://localhost:80/register?username=xxx&password=xxx&code=xxx 需自行实现 RegisterOperation 接口，该接口有修改密码时验证码的操作|

可参考如下实现
```java
package com.example.service;

import com.example.dao.UserDao;
import com.example.exception.DynamicCodeInvalidException;
import com.example.pojo.User;
import com.example.utils.PermissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
@Primary
public class UserServiceImpl implements UserDetailsService, RegisterOperation {
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = userDao.queryByUsername(username);
        if (CollectionUtils.isEmpty(users)) {
            throw new UsernameNotFoundException("username not exist");
        }
        User user = users.get(0);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), PermissionUtils.convert(PermissionUtils.teacher()));
    }

    @Override
    public void registerOrRestPassword(String username, String password, String code) throws DynamicCodeInvalidException {
        //TODO 不晓得是邮箱，还是手机号，还是啥
        if (!code.equalsIgnoreCase("666666")) {
            throw new DynamicCodeInvalidException("code invalid!");
        }
        String encodedPassword = passwordEncoder.encode(password);
        try {
            UserDetails user = loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            User savedUser = userDao.save(new User(username, encodedPassword));
            return;
        }
        userDao.updatePassword(username, encodedPassword);
    }
}


```

## 权限校验
登录成功会返回token
需要在请求头处添加 Authorization: Bearer ${token}

可使用SpringSecurity注解将在登录时注入的用户权限添加到接口上做访问限制；
```java
package com.example.controller;


import com.example.constants.StatusCode;
import com.example.service.CampusCodeServiceImpl;
import com.example.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("${api.baseUri:/app/school-api}")
public class IndexController {

    @Autowired
    private CampusCodeServiceImpl campusCodeService;

    @GetMapping(value = "/campus-code", produces = {"application/json;charset=utf-8"})
    @PreAuthorize("hasAuthority('user:scan')")
    public String campusCode(@RequestParam("auth_code") String authCode, HttpServletRequest request) {
        return ResponseUtils.build(StatusCode.SUCCESS, "resolve success", campusCodeService.decodeQrCode(authCode,request));
    }

    @GetMapping(value = "records", produces = {"application/json;charset=utf-8"})
    @PreAuthorize("hasAuthority('user:records')")
    public String records(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return ResponseUtils.build(StatusCode.SUCCESS, "get records success", campusCodeService.records(page, pageSize));
    }
}


```

