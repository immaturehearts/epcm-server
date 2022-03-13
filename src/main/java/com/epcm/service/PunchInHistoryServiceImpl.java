package com.epcm.service;

import com.epcm.common.EntityMapConvertor;
import com.epcm.dao.PunchInHistoryMapper;
import com.epcm.entity.PunchInHistory;
import com.epcm.entity.example.PunchInHistoryExample;
import com.epcm.enunn.StatusEnum;
import com.epcm.exception.StatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class PunchInHistoryServiceImpl implements PunchInHistoryService{
    @Autowired
    PunchInHistoryMapper punchInHistoryMapper;

    @Override
    public Map<String, Object> post(PunchInHistory history, Long uid) {
        history.setUid(uid);
        try {
            long id = punchInHistoryMapper.insertSelective(history);
            history.setId(id);
        }
        catch (Exception e){
            throw new StatusException(StatusEnum.HISTORY_INSERT_FAIL);
        }
        return EntityMapConvertor.entity2Map(history);
    }

    @Override
    public List<Map<String, Object>> getUserHistoryListByModifyTimeDESC(Long uid, Long page, Integer pageSize) {
        PunchInHistoryExample punchInHistoryExample = new PunchInHistoryExample();
        PunchInHistoryExample.Criteria criteria = punchInHistoryExample.createCriteria();
        criteria.andUidEqualTo(uid);
        punchInHistoryExample.setOrderByClause("gmt_modify DESC");

        if (page > 0) {
            Long offset = (page - 1L) * pageSize;
            punchInHistoryExample.setLimit(pageSize);
            punchInHistoryExample.setOffset(offset);
        }

        List<PunchInHistory> punchInHistories = punchInHistoryMapper.selectByExample(punchInHistoryExample);
        return EntityMapConvertor.entityList2MapList(punchInHistories);
    }

    @Override
    public List<Map<String, Object>> getAllHistory(Long page, Integer pageSize) {
        PunchInHistoryExample punchInHistoryExample = new PunchInHistoryExample();
        PunchInHistoryExample.Criteria criteria = punchInHistoryExample.createCriteria();
        criteria.andIdIsNotNull();
        punchInHistoryExample.setOrderByClause("gmt_modify DESC");

        if (page > 0) {
            Long offset = (page - 1L) * pageSize;
            punchInHistoryExample.setLimit(pageSize);
            punchInHistoryExample.setOffset(offset);
        }

        List<PunchInHistory> punchInHistories = punchInHistoryMapper.selectByExample(punchInHistoryExample);
        return EntityMapConvertor.entityList2MapList(punchInHistories);
    }

    @Override
    public boolean deleteById(Long id, Long uid) {
        ///TODO: 删除用户打卡记录
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long getCountByUid(Long uid) {
        PunchInHistoryExample punchInHistoryExample = getExampleByUid(uid);
        return punchInHistoryMapper.countByExample(punchInHistoryExample);
    }

    public PunchInHistoryExample getExampleByUid(Long uid) {
        PunchInHistoryExample punchInHistoryExample = new PunchInHistoryExample();
        PunchInHistoryExample.Criteria criteria = punchInHistoryExample.createCriteria();
        criteria.andUidEqualTo(uid);
        return punchInHistoryExample;
    }
}
