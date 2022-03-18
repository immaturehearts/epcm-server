package com.epcm.service;

import com.epcm.common.CollectionUtil;
import com.epcm.common.EntityMapConvertor;
import com.epcm.common.VerifyCodeCheck;
import com.epcm.dao.LoginInfoMapper;
import com.epcm.dao.UserInfoMapper;
import com.epcm.dao.UserMapper;
import com.epcm.entity.*;
import com.epcm.entity.builder.LoginInfoBuilder;
import com.epcm.entity.builder.UserBuilder;
import com.epcm.enunn.StatusEnum;
import com.epcm.enunn.UserStatusEnum;
import com.epcm.enunn.UserTypeEnum;
import com.epcm.exception.StatusException;
import com.epcm.manager.RedisManager;
import com.epcm.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    public static String REDIS_TOKEN_KEY = "token";

    public static String REDIS_USER_KEY = "user";

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    UserManager userManager;
    @Autowired
    RedisManager redisManager;
    @Autowired
    LoginInfoMapper loginInfoMapper;

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
    public Map<String, Object> register (User user, String verifyCode) {
        //检查手机号
        if (!VerifyCodeCheck.checkTelephoneNumber(user.getPhone())) {
            throw new StatusException(StatusEnum.INVALID_TELEPHONE_NUMBER);
        }
        //检查手机号与短信验证码
        if (!VerifyCodeCheck.checkTelephoneNumberAndCode(user.getPhone(),verifyCode)) {
            throw new StatusException(StatusEnum.INVALID_VERIFY_CODE);
        }
        //检查手机号是否被注册
        User existUser = UserBuilder.anUser()
                .withPhone(user.getPhone())
                .build();
        if(!CollectionUtils.isEmpty(userManager.selectByUserWithPhone(existUser))){
            throw new StatusException(StatusEnum.USER_ALREADY_EXIST);
        }

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
    public String loginByTelephoneAndPassword(String phone, String password) {
        //验证手机号是否合法
        if (!VerifyCodeCheck.checkTelephoneNumber(phone)) {
            throw new StatusException(StatusEnum.INVALID_TELEPHONE_NUMBER);
        }
        //登录
        User user = UserBuilder
                .anUser()
                .withPhone(phone)
                .build();
        //查找是否存在该用户
        List<User> users = userManager.selectByUserWithPhone(user);
        //用户不存在
        if (CollectionUtils.isEmpty(users)) {
            throw new StatusException(StatusEnum.USER_NOT_EXIST);
        }
        //用户不唯一
        if (users.size() > 1) {
            throw new StatusException(StatusEnum.USER_NOT_UNIQUE);
        }

        user.setPassword(password);
        //验证用户密码
        List<User> users2 = userManager.checkPhoneAndPassword(user);
        //密码错误
        if (CollectionUtils.isEmpty(users2)) {
            throw new StatusException(StatusEnum.PASSWORD_NOT_CORRECT);
        }
        //用户不唯一
        if (users2.size() > 1) {
            throw new StatusException(StatusEnum.USER_NOT_UNIQUE);
        }

        //写入登录信息表
        LoginInfoExample example = new LoginInfoExample();
        LoginInfoExample.Criteria criteria = example.createCriteria();
        Long uid = users2.get(0).getId();
        criteria.andUidEqualTo(uid);
        List<LoginInfo> login_types = loginInfoMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(login_types)){
            LoginInfo loginInfo = LoginInfoBuilder.aLoginInfo()
                    .withLoginType(0)
                    .withLoginCount(1L)
                    .withUid(uid)
                    .build();
            loginInfoMapper.insertSelective(loginInfo);
        } else {
            Long login_count = login_types.get(0).getLoginCount();
            Date last_login_time = login_types.get(0).getGmtModify();
            LoginInfo loginInfo = LoginInfoBuilder.aLoginInfo()
                    .withLoginCount(login_count + 1)
                    .withLastLoginTime(last_login_time)
                    .build();
            loginInfoMapper.updateByExampleSelective(loginInfo, example);
        }

        return SetUserToken(users2.get(0));
    }

    //设置token
    private String SetUserToken(User user) {
        String token = UUID.randomUUID().toString().replaceAll("-","");
        redisManager.hSet(REDIS_TOKEN_KEY, token, user.getId().toString(), 100000*1000);
        redisManager.hSetAll(REDIS_USER_KEY + user.getId().toString(), EntityMapConvertor.entity2Map(user), 100000*1000);
        return token;
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

    @Override
    public Map<String, Object> modifyUserName(User user, Long uid) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(uid);
        userMapper.updateByExampleSelective(user, example);
        return EntityMapConvertor.entity2Map(user);
    }

    @Override
    public boolean logout(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if(cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies){
                if(cookie.getName().equals(REDIS_TOKEN_KEY)){
                    if (redisManager.hHasKey(REDIS_TOKEN_KEY, cookie.getValue())) {
                        String userKey = (String) redisManager.hGet(REDIS_TOKEN_KEY, cookie.getValue());
                        if(redisManager.hHasKey(REDIS_USER_KEY, userKey)){
                            redisManager.hDel(REDIS_USER_KEY, userKey);
                            redisManager.hDel(REDIS_TOKEN_KEY, cookie.getValue());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public List<UserInfo> getUserInfoByUserId(Long uid){
        UserInfoExample example = new UserInfoExample();
        UserInfoExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(uid);
        return userInfoMapper.selectByExample(example);
    }
}
