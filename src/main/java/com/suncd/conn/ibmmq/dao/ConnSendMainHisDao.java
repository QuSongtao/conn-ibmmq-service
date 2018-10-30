package com.suncd.conn.ibmmq.dao;

import com.suncd.conn.ibmmq.entity.ConnSendMainHis;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConnSendMainHisDao {
    int deleteByPrimaryKey(String id);

    int insert(ConnSendMainHis record);

    int insertSelective(ConnSendMainHis record);

    ConnSendMainHis selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ConnSendMainHis record);

    int updateByPrimaryKey(ConnSendMainHis record);

    List<ConnSendMainHis> findBySendTimeAndTelId(@Param("dtStart") String dtStart,
                                                 @Param("dtEnd") String dtEnd,
                                                 @Param("telId") String telId);
}