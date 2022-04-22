package com.sso.shiroweb.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    private static final String SECRET = "!@55180129";

    /**
     * 生成token
     * @param map
     * @return
     */
    public static String getToken(Map<String,String> map){

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,7); //7天有效

        JWTCreator.Builder builder = JWT.create();

        //payload
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });

        String token = builder.withExpiresAt(instance.getTime()).sign(Algorithm.HMAC256(SECRET)); //指定令牌过期时间

        return token;

    }

    /**
     * 验证并返回token信息
     * @param token
     * @return
     */
    public static DecodedJWT verify(String token){
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }

    public static String getUserName(String token){
         try{
             DecodedJWT decodedJWT = verify(token);
             return decodedJWT.getClaim("name").asString();
         }catch (Exception e){

         }
         return null;
    }

    public static String updateToken(String token){
        try{
            DecodedJWT decodedJWT = verify(token);
            Map<String,String> map = new HashMap<>();
            map.put("id",decodedJWT.getClaim("id").asString());
            map.put("name",decodedJWT.getClaim("name").asString());
            return getToken(map);
        }catch (Exception e){

        }
        return null;
    }


}
