package org.example.service;


import org.example.bean.UserInfo;

import java.util.List;

public interface UserInfoService {
    List<UserInfo> getAllUserInfo(int id);

    int addurl(String url);

    int updateUserInfo(UserInfo userInfo);
}
