package com.suncd.conn.ibmmq.dao;

import com.suncd.conn.ibmmq.entity.ConnConfObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConnConfObjectDao {
    int deleteByPrimaryKey(String id);

    int insert(ConnConfObject record);

    int insertSelective(ConnConfObject record);

    ConnConfObject selectByPrimaryKey(String id);

    List<ConnConfObject> selectByType(@Param("objType") String objType, @Param("transferType") String transferType);

    int updateByPrimaryKeySelective(ConnConfObject record);

    int updateByPrimaryKey(ConnConfObject record);
}