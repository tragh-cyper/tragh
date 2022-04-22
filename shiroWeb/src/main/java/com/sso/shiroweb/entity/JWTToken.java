package com.sso.shiroweb.entity;

import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * JWTFilter要登录令牌LoginRealm时候，封装的一个参数
 * 不能直接传String
 */

public class JWTToken implements AuthenticationToken {
    private  String token;
    public JWTToken(String token)
    {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
