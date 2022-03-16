package com.epcm.manager;

import com.epcm.dao.UserMapper;
import com.epcm.entity.User;
import com.epcm.entity.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserManagerImpl implements UserManager{
    @Autowired
    UserMapper userMapper;

    /**
     * 查找用户，使用手机号查询
     * @param user
     * @return
     */
    @Override
    public List<User> selectByUserWithPhone(User user) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        buildUserCriteria(user,criteria);
        if(user.getPhone() != null) {
            criteria.andPhoneEqualTo(user.getPhone());
        }
        return userMapper.selectByExample(userExample);
    }

    /**
     * 查找用户，其中用户名是模糊查询
     * @param user
     * @return
     */
    @Override
    public List<User> selectByUserWithUserNameLike(User user) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        buildUserCriteria(user,criteria);
        if (user.getUserName() != null) {
            criteria.andUserNameLike(user.getUserName());
        }
        return userMapper.selectByExample(userExample);
    }

    /**
     * 查找创建晚于某个时间点的用户，其中用户名模糊查询
     * @param user
     * @return
     */
    @Override
    public List<User> selectByUserWithUserNameLikeAndCreateTimeAfter(User user) {
        if (user.getGmtCreate() == null) {
            return new ArrayList<>();
        }
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        buildUserCriteria(user,criteria);
        if (user.getUserName() != null) {
            criteria.andUserNameLike(user.getUserName());
        }
        criteria.andGmtCreateGreaterThanOrEqualTo(user.getGmtCreate());
        return userMapper.selectByExample(userExample);
    }

    /**
     * 查找创建早于某个时间点的用户，其中用户名模糊查询
     * @param user
     * @return
     */
    @Override
    public List<User> selectByUserWithUserNameLikeAndCreateTimeBefore(User user) {
        if (user.getGmtCreate() == null) {
            return new ArrayList<>();
        }
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        buildUserCriteria(user,criteria);
        if (user.getUserName() != null) {
            criteria.andUserNameLike(user.getUserName());
        }
        criteria.andGmtCreateLessThanOrEqualTo(user.getGmtCreate());
        return userMapper.selectByExample(userExample);
    }

    @Override
    public List<User> checkPhoneAndPassword(User user) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        buildUserCriteria(user,criteria);
        criteria.andPhoneEqualTo(user.getPhone())
                .andPasswordEqualTo(user.getPassword());
        return userMapper.selectByExample(userExample);
    }


    public void buildUserCriteria(User user, UserExample.Criteria criteria){
        if (user.getId() != null) {
            criteria.andIdEqualTo(user.getId());
        }
        if (user.getPhone() != null) {
            criteria.andPhoneEqualTo(user.getPhone());
        }
        if (user.getPassword() != null) {
            criteria.andPasswordEqualTo(user.getPassword());
        }
        if (user.getType() != null) {
            criteria.andTypeEqualTo(user.getType());
        }
        if (user.getStatus() != null) {
            criteria.andStatusEqualTo(user.getStatus());
        }
    }
}
