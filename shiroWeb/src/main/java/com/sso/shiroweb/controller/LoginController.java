package com.sso.shiroweb.controller;

import com.sso.shiroweb.entity.User;
import com.sso.shiroweb.utils.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/common")
public class LoginController {
    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/login")
    public Object login(User user, HttpServletResponse response){
        Map<String,String> errorMsg = new HashMap<String, String>();
        Subject currentUser = SecurityUtils.getSubject();
        if(!currentUser.isAuthenticated()){
            UsernamePasswordToken token = new UsernamePasswordToken(user.getName(),user.getPassword());
            try{
                currentUser.login(token);
                Map<String,String> map = new HashMap<>();
                map.put("id",user.getId());
                map.put("name",user.getName());
                String jwtToken = JWTUtil.getToken(map);
                response.addHeader("Access-Control-Expose-Headers","token");
                response.addHeader("token", jwtToken);
                return "login success";
            }catch (UnknownAccountException uae){
                logger.info("There is no user with username of " + token.getPrincipal());
                errorMsg.put("errorMsg","The user not exists");
            }catch (IncorrectCredentialsException ice){
                logger.info("Password for account " + token.getPrincipal() + "was incorrect!");
                errorMsg.put("errorMsg","Error password");
            }catch (LockedAccountException lae){
                logger.info("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
                errorMsg.put("errorMsg","The user is locked");
            }
            // ... catch more exceptions here (maybe custom ones specific to your application?
            catch (AuthenticationException ae) {
                //unexpected condition?  error?
                errorMsg.put("errorMsg","Fail to login");
            }
        }else{
            logger.info("Success to login");
        }
        return errorMsg;
    }

    @PostMapping("/register")
    public void register(User user){

    }

    @GetMapping("/logout")
    public void logout(){

    }
}
