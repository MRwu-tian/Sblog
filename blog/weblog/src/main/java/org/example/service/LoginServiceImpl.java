package org.example.service;

import org.example.bean.Users;
import org.example.mapper.LoginMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginServiceImpl {
    @Resource
    private LoginMapper loginMapper;

    public int register(String regname) {
        return loginMapper.register(regname);
    }

    public int editReg(String regname, String regpassword, String username) {
        return loginMapper.editReg(regname,regpassword,username);
    }
}
