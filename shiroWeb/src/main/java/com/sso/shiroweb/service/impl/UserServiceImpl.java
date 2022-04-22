package com.sso.shiroweb.service.impl;

import com.sso.shiroweb.entity.User;
import com.sso.shiroweb.mapper.UserMapper;
import com.sso.shiroweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

//    @Autowired
//    private UserMapper userMapper;


    @Override
    public User login(User user) {
        return null;
    }

    @Override
    public User queryByName(String name) {
        return new User();
//        return userMapper.queryBuName(name);
    }
}
