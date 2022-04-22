package com.sso.shiroweb.service;

import com.sso.shiroweb.entity.User;

public interface UserService {

    User login(User user);

    User queryByName(String name);


}
