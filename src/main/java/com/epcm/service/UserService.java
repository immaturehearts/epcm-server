package com.epcm.service;

import com.epcm.entity.User;
import com.epcm.entity.UserInfo;

import java.util.Map;

public interface UserService {

    //查询用户
    User selectByPrimaryKey(Long id);

    //注册用户
    Map<String,Object> register (User user);

    //上传用户信息
    Map<String,Object> basicInfoUpload (UserInfo userInfo, Long uid);

    //获取用户信息
    Map<String,Object> getUserInfo (Long uid);

    //修改用户信息
    Map<String,Object> modifyUserInfo (UserInfo userInfo, Long uid);

//    //查找用户
//    List<User> selectByExample (UserExample example);

    //获取用户头像
    String getAvatar (Long uid);
}
