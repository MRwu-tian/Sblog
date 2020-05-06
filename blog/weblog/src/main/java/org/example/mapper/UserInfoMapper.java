package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.bean.UserInfo;

import java.util.List;

@Mapper
public interface UserInfoMapper {
    List<UserInfo> getAllUserInfo(int id);

    int addurl(String url);

    int updateUserInfo(UserInfo userInfo);
}
