package com.suncd.conn.ibmmq.dao;

import com.suncd.conn.ibmmq.entity.ConnConfObject;

public interface ConnConfObjectDao {
    int deleteByPrimaryKey(String id);

    int insert(ConnConfObject record);

    int insertSelective(ConnConfObject record);

    ConnConfObject selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ConnConfObject record);

    int updateByPrimaryKey(ConnConfObject record);
}