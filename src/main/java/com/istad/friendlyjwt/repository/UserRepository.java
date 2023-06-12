package com.istad.friendlyjwt.repository;


import com.istad.friendlyjwt.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserRepository {

    @Select("select * from users_tb where username like #{username}")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "secret_key",property = "password"),
            @Result(property = "roles",column = "id", many = @Many(select = "findRolesByUserId"))
    })
    User loadUserByUsername(String username);

    @Select("select role from user_role_tb\n" +
            "         inner join role_tb rt\n" +
            "             on rt.id = user_role_tb.role_id\n" +
            "         where user_id= #{id}")
    List<String> findRolesByUserId(int id);
}
