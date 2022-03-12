package com.epcm.entity.builder;

import com.epcm.entity.UserPosition;

import java.math.BigDecimal;
import java.util.Date;

public final class UserPositionBuilder {
    private Long id;
    private Long uid;
    private String city;
    private String location;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private Date gmtModify;
    private Date gmtCreate;

    private UserPositionBuilder() {
    }

    public static UserPositionBuilder anUserPosition() {
        return new UserPositionBuilder();
    }

    public UserPositionBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserPositionBuilder withUid(Long uid) {
        this.uid = uid;
        return this;
    }

    public UserPositionBuilder withCity(String city) {
        this.city = city;
        return this;
    }

    public UserPositionBuilder withLocation(String location) {
        this.location = location;
        return this;
    }

    public UserPositionBuilder withLongitude(BigDecimal longitude) {
        this.longitude = longitude;
        return this;
    }

    public UserPositionBuilder withLatitude(BigDecimal latitude) {
        this.latitude = latitude;
        return this;
    }

    public UserPositionBuilder withGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
        return this;
    }

    public UserPositionBuilder withGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        return this;
    }

    public UserPosition build() {
        UserPosition userPosition = new UserPosition();
        userPosition.setId(id);
        userPosition.setUid(uid);
        userPosition.setCity(city);
        userPosition.setLocation(location);
        userPosition.setLongitude(longitude);
        userPosition.setLatitude(latitude);
        userPosition.setGmtModify(gmtModify);
        userPosition.setGmtCreate(gmtCreate);
        return userPosition;
    }
}
