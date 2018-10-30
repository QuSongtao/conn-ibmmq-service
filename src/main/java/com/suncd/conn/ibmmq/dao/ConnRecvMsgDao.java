package com.suncd.conn.ibmmq.dao;

import com.suncd.conn.ibmmq.entity.ConnRecvMsg;

public interface ConnRecvMsgDao {
    int deleteByPrimaryKey(String id);

    int insert(ConnRecvMsg record);

    int insertSelective(ConnRecvMsg record);

    ConnRecvMsg selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ConnRecvMsg record);

    int updateByPrimaryKeyWithBLOBs(ConnRecvMsg record);

    int updateByPrimaryKey(ConnRecvMsg record);
}