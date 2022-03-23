package com.epcm.service;

import com.epcm.common.EntityMapConvertor;
import com.epcm.dao.PunchInHistoryMapper;
import com.epcm.dao.UserMapper;
import com.epcm.entity.PunchInHistory;
import com.epcm.entity.PunchInHistoryExample;
import com.epcm.entity.User;
import com.epcm.entity.UserExample;
import com.epcm.enunn.StatusEnum;
import com.epcm.enunn.UserTypeEnum;
import com.epcm.exception.StatusException;
import com.epcm.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Service
public class PunchInHistoryServiceImpl implements PunchInHistoryService{
    @Autowired
    PunchInHistoryMapper punchInHistoryMapper;
    @Autowired
    UserMapper userMapper;

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
    public List<Map<String, Object>> getAllHistory(Long uid, Long page, Integer pageSize) {
        //检查用户是否有管理员权限
        UserExample userExample = new UserExample();
        UserExample.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andIdEqualTo(uid)
                .andTypeEqualTo(UserTypeEnum.ADMIN.getCode());
        List<User> users = userMapper.selectByExample(userExample);
        if(CollectionUtils.isEmpty(users)){
            throw new StatusException(StatusEnum.FORBIDDEN);
        }

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
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id, Long uid) {
        //检查用户是否有管理员权限
        UserExample userExample = new UserExample();
        UserExample.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andIdEqualTo(uid)
                .andTypeEqualTo(UserTypeEnum.ADMIN.getCode());
        List<User> users = userMapper.selectByExample(userExample);
        if(CollectionUtils.isEmpty(users)){
            throw new StatusException(StatusEnum.FORBIDDEN);
        }

        return punchInHistoryMapper.deleteByPrimaryKey(id)!=0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long getCountByUid(Long uid) {
        PunchInHistoryExample punchInHistoryExample = getExampleByUid(uid);
        return punchInHistoryMapper.countByExample(punchInHistoryExample);
    }

    @Override
    public long getAllCount(Long uid) {
        //检查用户是否有管理员权限
        UserExample userExample = new UserExample();
        UserExample.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andIdEqualTo(uid)
                .andTypeEqualTo(UserTypeEnum.ADMIN.getCode());
        List<User> users = userMapper.selectByExample(userExample);
        if(CollectionUtils.isEmpty(users)){
            throw new StatusException(StatusEnum.FORBIDDEN);
        }

        PunchInHistoryExample punchInHistoryExample = new PunchInHistoryExample();
        PunchInHistoryExample.Criteria criteria = punchInHistoryExample.createCriteria();
        criteria.andIdIsNotNull();
        return punchInHistoryMapper.countByExample(punchInHistoryExample);
    }

    public PunchInHistoryExample getExampleByUid(Long uid) {
        PunchInHistoryExample punchInHistoryExample = new PunchInHistoryExample();
        PunchInHistoryExample.Criteria criteria = punchInHistoryExample.createCriteria();
        criteria.andUidEqualTo(uid);
        return punchInHistoryExample;
    }
}
