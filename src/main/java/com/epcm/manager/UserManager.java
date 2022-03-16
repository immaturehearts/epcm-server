package com.epcm.manager;

import com.epcm.entity.User;

import java.util.List;

public interface UserManager {
    //通过手机号查询用户
    List<User> selectByUserWithPhone(User user);

    //通过用户名查询用户
    List<User> selectByUserWithUserNameLike(User user);

    List<User> selectByUserWithUserNameLikeAndCreateTimeAfter(User user);

    List<User> selectByUserWithUserNameLikeAndCreateTimeBefore(User user);

    //验证用户手机号密码
    List<User> checkPhoneAndPassword(User user);
}
