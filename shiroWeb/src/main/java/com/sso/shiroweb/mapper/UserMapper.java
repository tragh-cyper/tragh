package com.sso.shiroweb.mapper;

import com.sso.shiroweb.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {


     User register(User user);

     User queryBuName(String name);

}
