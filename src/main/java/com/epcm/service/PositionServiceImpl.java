package com.epcm.service;

import com.epcm.dao.PunchInHistoryMapper;
import com.epcm.dao.UserPositionMapper;
import com.epcm.entity.PunchInHistory;
import com.epcm.entity.PunchInHistoryExample;
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
    @Autowired
    PunchInHistoryMapper punchInHistoryMapper;
    private final Double radius = 1.5;
    private final long ten_min = 10*60*1000;
    private final long fourteen_days = 14*24*60*60*1000;

    @Override
    public int positionPost(UserPosition userPosition, Long uid) {
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

    //0-has not, 1-has
    @Override
    public int getNearBy(Double radius, Double lon, Double lat, String city, Long uid) {
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
        if (!CollectionUtils.isEmpty(positions)) {
            for (UserPosition position : positions) {
                long userId = position.getUid();
                PunchInHistoryExample punchInHistoryExample = new PunchInHistoryExample();
                PunchInHistoryExample.Criteria criteria1 = punchInHistoryExample.createCriteria();
                Date now1 = new Date(System.currentTimeMillis());
                Date before1 = new Date(now.getTime() - fourteen_days);
                criteria1.andUidEqualTo(userId)
                        .andHealthNotEqualTo(0)
                        .andGmtCreateBetween(before1, now1);
                List<PunchInHistory> histories = punchInHistoryMapper.selectByExample(punchInHistoryExample);
                if (!CollectionUtils.isEmpty(histories)) {
                    return 1;
                }
            }
        }
        return 0;
    }

    @Override
    public int testGetNearBy(Double lon, Double lat, String city, Long uid) {
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
        criteria
                .andLatitudeBetween(BigDecimal.valueOf(minlat), BigDecimal.valueOf(maxlat))
                .andLongitudeBetween(BigDecimal.valueOf(minlng), BigDecimal.valueOf(maxlng))
                .andCityEqualTo(city)
                .andUidNotEqualTo(uid)
                .andGmtModifyBetween(before, now);
        List<UserPosition> positions = userPositionMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(positions)) {
            for (UserPosition position : positions) {
                long userId = position.getUid();
                PunchInHistoryExample punchInHistoryExample = new PunchInHistoryExample();
                PunchInHistoryExample.Criteria criteria1 = punchInHistoryExample.createCriteria();
                Date now1 = new Date(System.currentTimeMillis());
                Date before1 = new Date(now.getTime() - fourteen_days);
                criteria1.andUidEqualTo(userId)
                        .andHealthNotEqualTo(0)
                        .andGmtCreateBetween(before1, now1);
                List<PunchInHistory> histories = punchInHistoryMapper.selectByExample(punchInHistoryExample);
                if (!CollectionUtils.isEmpty(histories)) {
                    return 1;
                }
            }
        }
        return 0;
//        if(CollectionUtils.isEmpty(positions)){
//            return "has not";
//        } else {
//            System.out.println(positions.get(0).getGmtModify().toString());
//            return "has";
//        }
    }

}
