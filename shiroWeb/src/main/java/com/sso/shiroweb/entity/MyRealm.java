package com.sso.shiroweb.entity;

import com.sso.shiroweb.service.UserService;
import com.sso.shiroweb.utils.JWTUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class MyRealm extends AuthorizingRealm {
    private Logger logger = LoggerFactory.getLogger(MyRealm.class);

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    //授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("<<<enterd MyRealm doGetAuthorizationInfo method");
        return null;
    }

    //认证

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info(">>>enterd MyRealm doGetAuthenticationInfo method");
        //得到客户端传过来的令牌
        String tokenString =(String) authenticationToken.getPrincipal();
        //根据令牌得用户名,可以查用户库
        String username = JWTUtil.getUserName(tokenString);



        User user = userService.queryByName(username);
        if (user==null)
        {
            throw new RuntimeException("Fail to login");
        }
        return new SimpleAuthenticationInfo(tokenString, user.getPassword(), "");
    }
}
