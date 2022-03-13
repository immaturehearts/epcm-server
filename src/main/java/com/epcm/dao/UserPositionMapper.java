package com.epcm.dao;

import com.epcm.entity.UserPosition;
import com.epcm.entity.example.UserPositionExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserPositionMapper {
    long countByExample(UserPositionExample example);

    int deleteByExample(UserPositionExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserPosition record);

    int insertSelective(UserPosition record);

    List<UserPosition> selectByExample(UserPositionExample example);

    UserPosition selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserPosition record, @Param("example") UserPositionExample example);

    int updateByExample(@Param("record") UserPosition record, @Param("example") UserPositionExample example);

    int updateByPrimaryKeySelective(UserPosition record);

    int updateByPrimaryKey(UserPosition record);
}