package com.suncd.conn.ibmmq.dao;

import com.suncd.conn.ibmmq.entity.ConnConfTel;
import org.apache.ibatis.annotations.Param;

public interface ConnConfTelDao {

    ConnConfTel selectQueueName(@Param("telId") String telId, @Param("sender") String sender, @Param("receiver") String receiver);

}