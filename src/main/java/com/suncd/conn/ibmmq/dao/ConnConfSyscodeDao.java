package com.suncd.conn.ibmmq.dao;

import com.suncd.conn.ibmmq.entity.ConnConfSyscode;
import org.apache.ibatis.annotations.Param;

public interface ConnConfSyscodeDao {
    int deleteByPrimaryKey(String id);

    int insert(ConnConfSyscode record);

    int insertSelective(ConnConfSyscode record);

    ConnConfSyscode selectByPrimaryKey(String id);

    ConnConfSyscode selectBySysCode(@Param("sysCode") String sysCode);

    int updateByPrimaryKeySelective(ConnConfSyscode record);

    int updateByPrimaryKey(ConnConfSyscode record);
}