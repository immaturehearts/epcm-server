package com.epcm.dao;

import com.epcm.entity.PunchInHistory;
import com.epcm.entity.example.PunchInHistoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PunchInHistoryMapper {
    long countByExample(PunchInHistoryExample example);

    int deleteByExample(PunchInHistoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PunchInHistory record);

    int insertSelective(PunchInHistory record);

    List<PunchInHistory> selectByExample(PunchInHistoryExample example);

    PunchInHistory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PunchInHistory record, @Param("example") PunchInHistoryExample example);

    int updateByExample(@Param("record") PunchInHistory record, @Param("example") PunchInHistoryExample example);

    int updateByPrimaryKeySelective(PunchInHistory record);

    int updateByPrimaryKey(PunchInHistory record);
}