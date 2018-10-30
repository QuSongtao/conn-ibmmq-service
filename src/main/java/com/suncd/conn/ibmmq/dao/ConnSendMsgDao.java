package com.suncd.conn.ibmmq.dao;

import com.suncd.conn.ibmmq.entity.ConnSendMsg;

public interface ConnSendMsgDao {
    int deleteByPrimaryKey(String id);

    int insert(ConnSendMsg record);

    int insertSelective(ConnSendMsg record);

    ConnSendMsg selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ConnSendMsg record);

    int updateByPrimaryKeyWithBLOBs(ConnSendMsg record);

    int updateByPrimaryKey(ConnSendMsg record);
}