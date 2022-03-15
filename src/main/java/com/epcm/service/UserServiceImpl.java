package com.epcm.service;

import com.epcm.common.CollectionUtil;
import com.epcm.common.EntityMapConvertor;
import com.epcm.dao.UserInfoMapper;
import com.epcm.dao.UserMapper;
import com.epcm.entity.User;
import com.epcm.entity.UserInfo;
import com.epcm.entity.UserInfoExample;
import com.epcm.enunn.StatusEnum;
import com.epcm.enunn.UserStatusEnum;
import com.epcm.enunn.UserTypeEnum;
import com.epcm.exception.StatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{

    public static String REDIS_TOKEN_KEY = "token";

    public static String REDIS_USER_KEY = "user";

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserInfoMapper userInfoMapper;

//    @Override
//    public int insertUser(User user) {
//        return userMapper.insertSelective(user);
//    }

    @Override
    public User selectByPrimaryKey(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> register (User user) {
        long uid;
        user.setType(UserTypeEnum.USER.getCode());
        user.setStatus(UserStatusEnum.AVAILABLE.getCode());
        try {
            System.out.println(user.toString());
            uid = userMapper.insertSelective(user);
            System.out.println(uid);
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new StatusException(StatusEnum.USER_REGISTER_FAILED);
        }
        user.setId((long)uid);

        return EntityMapConvertor.entity2Map(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> basicInfoUpload(UserInfo userInfo, Long uid) {
        long id;
        userInfo.setUid(uid);
        try {
            id = userInfoMapper.insertSelective(userInfo);
        } catch (Exception e) {
            throw new StatusException(StatusEnum.USERINFO_INSERT_FAILED);
        }
        userInfo.setId(id);

        return EntityMapConvertor.entity2Map(userInfo);
    }

    @Override
    public Map<String, Object> getUserInfo(Long uid) {
        List<UserInfo> userInfos = getUserInfoByUserId(uid);
        UserInfo userInfo = CollectionUtil.getUniqueObjectFromList(userInfos);
        return EntityMapConvertor.entity2Map(userInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> modifyUserInfo(UserInfo userInfo, Long uid) {
        UserInfoExample example = new UserInfoExample();
        UserInfoExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(uid);
        List<UserInfo> userInfos = getUserInfoByUserId(uid);
        if (CollectionUtils.isEmpty(userInfos)) {
            userInfo.setUid(uid);
            userInfoMapper.insertSelective(userInfo);
        } else {
            userInfoMapper.updateByExampleSelective(userInfo,example);
        }
        return EntityMapConvertor.entity2Map(userInfo);
    }

//    @Override
//    public List<User> selectByExample(UserExample example) {
//        return null;
//    }

    @Override
    public String getAvatar(Long uid) {
        List<UserInfo> userInfos = getUserInfoByUserId(uid);
        UserInfo userInfo = CollectionUtil.getUniqueObjectFromList(userInfos);
        return userInfo.getAvatar() == null ? "" : userInfo.getAvatar();
    }

    public List<UserInfo> getUserInfoByUserId(Long uid){
        UserInfoExample example = new UserInfoExample();
        UserInfoExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(uid);
        return userInfoMapper.selectByExample(example);
    }
}
