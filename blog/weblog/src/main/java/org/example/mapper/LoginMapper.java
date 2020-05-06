package org.example.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.bean.Users;

public interface LoginMapper {
    Users findUserByUsername(String userSerial);

    int register(String regname);

    int editReg(@Param("regname") String regname,@Param("regpassword") String regpassword,@Param("username") String username);

}
