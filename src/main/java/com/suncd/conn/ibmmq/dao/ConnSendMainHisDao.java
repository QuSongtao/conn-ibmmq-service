package com.suncd.conn.ibmmq.dao;

import com.suncd.conn.ibmmq.entity.ConnSendMainHis;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConnSendMainHisDao {

    int insertSelective(ConnSendMainHis record);

}