package com.epcm.service;

import com.epcm.entity.PunchInHistory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface PunchInHistoryService {
    //打卡记录上传
    Map<String, Object> post (PunchInHistory history, Long uid);

    //查询某用户打卡记录
    List<Map<String, Object>> getUserHistoryListByModifyTimeDESC (Long uid, Long page, Integer pageSize);

    //查询所有用户打卡记录
    List<Map<String,Object>> getAllHistory (Long page, Integer pageSize);

    //删除记录
    boolean deleteById(Long id, Long uid);

    //获取用户打卡记录总数
    long getCountByUid(Long uid);
}
