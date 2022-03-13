package com.epcm.manager;

import com.epcm.entity.User;

import java.util.List;

public interface UserManager {
    List<User> selectByUserWithPhone(User user);

    List<User> selectByUserWithUserNameLike(User user);

    List<User> selectByUserWithUserNameLikeAndCreateTimeAfter(User user);

    List<User> selectByUserWithUserNameLikeAndCreateTimeBefore(User user);
}
