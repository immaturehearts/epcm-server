package com.epcm.entity.builder;

import com.epcm.entity.LoginInfo;

import java.util.Date;

public final class LoginInfoBuilder {
    private Long id;
    private Long uid;
    private Integer loginType;
    private Long loginCount;
    private Date lastLoginTime;
    private Date gmtModify;
    private Date gmtCreate;

    private LoginInfoBuilder() {
    }

    public static LoginInfoBuilder aLoginInfo() {
        return new LoginInfoBuilder();
    }

    public LoginInfoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public LoginInfoBuilder withUid(Long uid) {
        this.uid = uid;
        return this;
    }

    public LoginInfoBuilder withLoginType(Integer loginType) {
        this.loginType = loginType;
        return this;
    }

    public LoginInfoBuilder withLoginCount(Long loginCount) {
        this.loginCount = loginCount;
        return this;
    }

    public LoginInfoBuilder withLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        return this;
    }

    public LoginInfoBuilder withGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
        return this;
    }

    public LoginInfoBuilder withGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        return this;
    }

    public LoginInfo build() {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setId(id);
        loginInfo.setUid(uid);
        loginInfo.setLoginType(loginType);
        loginInfo.setLoginCount(loginCount);
        loginInfo.setLastLoginTime(lastLoginTime);
        loginInfo.setGmtModify(gmtModify);
        loginInfo.setGmtCreate(gmtCreate);
        return loginInfo;
    }
}
