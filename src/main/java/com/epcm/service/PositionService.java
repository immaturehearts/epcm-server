package com.epcm.service;

import com.epcm.entity.UserPosition;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface PositionService {
    //用户定位上传
    Map<String, Object> positionPost (UserPosition userPosition, Long uid);

    ///TODO: 附近的人并报警

    ///TODO: 定时器-清除一定时间（如：一天）之前记录
}
