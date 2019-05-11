package com.suncd.conn.ibmmq.dao;

import com.suncd.conn.ibmmq.entity.ConnConfSyscode;
import org.apache.ibatis.annotations.Param;

public interface ConnConfSyscodeDao {

    ConnConfSyscode selectBySysCode(@Param("sysCode") String sysCode);

}