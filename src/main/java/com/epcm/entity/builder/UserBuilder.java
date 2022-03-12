package com.epcm.entity.builder;

import com.epcm.entity.User;

import java.util.Date;

public final class UserBuilder {
    private Long id;
    private String userName;
    private String password;
    private String phone;
    private Integer type;
    private Integer status;
    private Date gmtModify;
    private Date gmtCreate;

    private UserBuilder() {
    }

    public static UserBuilder anUser() {
        return new UserBuilder();
    }

    public UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public UserBuilder withType(Integer type) {
        this.type = type;
        return this;
    }

    public UserBuilder withStatus(Integer status) {
        this.status = status;
        return this;
    }

    public UserBuilder withGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
        return this;
    }

    public UserBuilder withGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        return this;
    }

    public User build() {
        User user = new User();
        user.setId(id);
        user.setUserName(userName);
        user.setPassword(password);
        user.setPhone(phone);
        user.setType(type);
        user.setStatus(status);
        user.setGmtModify(gmtModify);
        user.setGmtCreate(gmtCreate);
        return user;
    }
}
