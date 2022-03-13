package com.epcm.service;

import com.epcm.common.EntityMapConvertor;
import com.epcm.dao.UserPositionMapper;
import com.epcm.entity.UserPosition;
import com.epcm.enunn.StatusEnum;
import com.epcm.exception.StatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PositionServiceImpl implements PositionService{
    @Autowired
    UserPositionMapper userPositionMapper;

    @Override
    public Map<String, Object> positionPost(UserPosition userPosition, Long uid) {
        userPosition.setUid(uid);
        try {
            long id = userPositionMapper.insertSelective(userPosition);
            userPosition.setId(id);
        }
        catch (Exception e){
            throw new StatusException(StatusEnum.POSITION_INSERT_FAILED);
        }
        return EntityMapConvertor.entity2Map(userPosition);
    }
}
