package com.epcm.entity.builder;

import com.epcm.entity.UserInfo;

import java.util.Date;

public final class UserInfoBuilder {
    private Long id;
    private Long uid;
    private Integer gender;
    private String avatar;
    private String idCardNumber;
    private String emailAddr;
    private String trueName;
    private Date gmtModify;
    private Date gmtCreate;

    private UserInfoBuilder() {
    }

    public static UserInfoBuilder anUserInfo() {
        return new UserInfoBuilder();
    }

    public UserInfoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserInfoBuilder withUid(Long uid) {
        this.uid = uid;
        return this;
    }

    public UserInfoBuilder withGender(Integer gender) {
        this.gender = gender;
        return this;
    }

    public UserInfoBuilder withAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public UserInfoBuilder withIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
        return this;
    }

    public UserInfoBuilder withEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
        return this;
    }

    public UserInfoBuilder withTrueName(String trueName) {
        this.trueName = trueName;
        return this;
    }

    public UserInfoBuilder withGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
        return this;
    }

    public UserInfoBuilder withGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        return this;
    }

    public UserInfo build() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setUid(uid);
        userInfo.setGender(gender);
        userInfo.setAvatar(avatar);
        userInfo.setIdCardNumber(idCardNumber);
        userInfo.setEmailAddr(emailAddr);
        userInfo.setTrueName(trueName);
        userInfo.setGmtModify(gmtModify);
        userInfo.setGmtCreate(gmtCreate);
        return userInfo;
    }
}
