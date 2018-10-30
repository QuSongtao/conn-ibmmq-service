package com.suncd.conn.ibmmq.dao;

import com.suncd.conn.ibmmq.entity.ConnConfTel;

public interface ConnConfTelDao {
    int deleteByPrimaryKey(String id);

    int insert(ConnConfTel record);

    int insertSelective(ConnConfTel record);

    ConnConfTel selectByPrimaryKey(String id);
    ConnConfTel selectQueueName(String telId);

    int updateByPrimaryKeySelective(ConnConfTel record);

    int updateByPrimaryKey(ConnConfTel record);
}