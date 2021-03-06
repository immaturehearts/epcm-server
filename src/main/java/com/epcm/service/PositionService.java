package com.epcm.service;

import com.epcm.entity.UserPosition;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface PositionService {
    //用户定位上传
    int positionPost (UserPosition userPosition, Long uid);

    //查询附近的人，半径单位：km
     int getNearBy(Double radius, Double lon, Double lat, String city, Long uid);

    int testGetNearBy(Double lon, Double lat, String city, Long uid);
}
