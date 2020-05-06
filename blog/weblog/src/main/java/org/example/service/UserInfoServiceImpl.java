package org.example.service;

import org.example.bean.UserInfo;
import org.example.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Override
    public List<UserInfo> getAllUserInfo(int id) {
        return userInfoMapper.getAllUserInfo(id) ;
    }

    @Override
    public int addurl(String url) {
        return userInfoMapper.addurl(url);
    }

    @Override
    public int updateUserInfo(UserInfo userInfo) {
        return userInfoMapper.updateUserInfo(userInfo);
    }
}
