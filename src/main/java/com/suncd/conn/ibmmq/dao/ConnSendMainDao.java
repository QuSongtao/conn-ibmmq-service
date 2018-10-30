package com.suncd.conn.ibmmq.dao;

import com.suncd.conn.ibmmq.entity.ConnSendMain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConnSendMainDao {
    int deleteByPrimaryKey(String id);

    int insert(ConnSendMain record);

    int insertSelective(ConnSendMain record);

    ConnSendMain selectByPrimaryKey(String id);

    List<ConnSendMain> selectBySendFlag(@Param("sendFlag") String sendFlag, @Param("telType")String telType);

    int updateByPrimaryKeySelective(ConnSendMain record);

    int updateByPrimaryKey(ConnSendMain record);
}