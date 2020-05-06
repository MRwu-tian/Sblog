package org.example.service;

import org.example.bean.SUser;
import org.example.bean.Users;
import org.example.mapper.LoginMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginDetailServiceImpl implements UserDetailsService {

    @Resource
    private LoginMapper loginMapper;

    @Override
    public UserDetails loadUserByUsername(String userSerial) throws UsernameNotFoundException {
        Users users = loginMapper.findUserByUsername(userSerial);
        return new SUser(users);
    }
}
