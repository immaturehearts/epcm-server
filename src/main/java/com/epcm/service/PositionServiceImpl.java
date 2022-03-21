package com.epcm.service;

import com.epcm.dao.UserPositionMapper;
import com.epcm.entity.UserPosition;
import com.epcm.entity.UserPositionExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PositionServiceImpl implements PositionService{
    @Autowired
    UserPositionMapper userPositionMapper;
    private final Double radius = 1.0;
    private final long ten_min = 10*60*1000;

    @Override
    public String positionPost(UserPosition userPosition, Long uid) {
        UserPositionExample example = new UserPositionExample();
        UserPositionExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(uid);
        List<UserPosition> positions = userPositionMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(positions)){
            userPosition.setUid(uid);
            userPositionMapper.insertSelective(userPosition);
        } else {
            userPositionMapper.updateByExampleSelective(userPosition, example);
        }
//        userPosition.setUid(uid);
//        try {
//            long id = userPositionMapper.insertSelective(userPosition);
//            userPosition.setId(id);
//        }
//        catch (Exception e){
//            throw new StatusException(StatusEnum.POSITION_INSERT_FAILED);
//        }
//        return EntityMapConvertor.entity2Map(userPosition);
//        return getNearBy(1, uid);
        return getNearBy(radius, userPosition.getLongitude().doubleValue(),
                userPosition.getLatitude().doubleValue(), userPosition.getCity(), uid);
    }

    @Override
    public String getNearBy(Double radius, Double lon, Double lat, String city, Long uid) {
        //查询用户经纬度信息
        double r = 6371;//地球半径千米
        double dlng =  2*Math.asin(Math.sin(radius/(2*r))/Math.cos(lat*Math.PI/180));
        dlng = dlng*180/Math.PI;//角度转为弧度
        double dlat = radius/r;
        dlat = dlat*180/Math.PI;
        double minlat = lat - dlat;
        double maxlat = lat + dlat;
        double minlng = lon - dlng;
        double maxlng = lon + dlng;

        Date now = new Date(System.currentTimeMillis());
        Date before = new Date(now.getTime() - ten_min);
        UserPositionExample example = new UserPositionExample();
        UserPositionExample.Criteria criteria = example.createCriteria();
        criteria.andLatitudeBetween(BigDecimal.valueOf(minlat), BigDecimal.valueOf(maxlat))
                .andLongitudeBetween(BigDecimal.valueOf(minlng), BigDecimal.valueOf(maxlng))
                .andCityEqualTo(city)
                .andUidNotEqualTo(uid)
                .andGmtModifyBetween(before, now);
        List<UserPosition> positions = userPositionMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(positions)){
            return "has not";
        } else {
            return "has";
        }
    }

    @Override
    public String testGetNearBy(Double lon, Double lat, String city, Long uid) {
        double r = 6371;//地球半径千米
        double dlng =  2*Math.asin(Math.sin(radius/(2*r))/Math.cos(lat*Math.PI/180));
        dlng = dlng*180/Math.PI;//角度转为弧度
        double dlat = radius/r;
        dlat = dlat*180/Math.PI;
        double minlat = lat - dlat;
        double maxlat = lat + dlat;
        double minlng = lon - dlng;
        double maxlng = lon + dlng;

        Date now = new Date(System.currentTimeMillis());
        System.out.println(now.toString());
        Date before = new Date(now.getTime() - ten_min);
        UserPositionExample example = new UserPositionExample();
        UserPositionExample.Criteria criteria = example.createCriteria();
        criteria
                .andLatitudeBetween(BigDecimal.valueOf(minlat), BigDecimal.valueOf(maxlat))
                .andLongitudeBetween(BigDecimal.valueOf(minlng), BigDecimal.valueOf(maxlng))
                .andCityEqualTo(city)
                .andUidNotEqualTo(uid)
                .andGmtModifyBetween(before, now);
        List<UserPosition> positions = userPositionMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(positions)){
            return "has not";
        } else {
            System.out.println(positions.get(0).getGmtModify().toString());
            return "has";
        }
    }

}
