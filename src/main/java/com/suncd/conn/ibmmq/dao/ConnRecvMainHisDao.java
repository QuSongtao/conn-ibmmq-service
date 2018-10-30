package com.suncd.conn.ibmmq.dao;

import com.suncd.conn.ibmmq.entity.ConnRecvMainHis;

public interface ConnRecvMainHisDao {
    int deleteByPrimaryKey(String id);

    int insert(ConnRecvMainHis record);

    int insertSelective(ConnRecvMainHis record);

    ConnRecvMainHis selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ConnRecvMainHis record);

    int updateByPrimaryKey(ConnRecvMainHis record);
}