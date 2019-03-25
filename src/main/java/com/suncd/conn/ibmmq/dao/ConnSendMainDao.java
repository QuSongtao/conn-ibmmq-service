package com.suncd.conn.ibmmq.dao;

import com.suncd.conn.ibmmq.entity.ConnSendMain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConnSendMainDao {
    int deleteByPrimaryKey(String id);

//    List<ConnSendMain> selectBySendFlag(@Param("sendFlag") String sendFlag, @Param("telType")String telType);
    List<ConnSendMain> selectAll();

}