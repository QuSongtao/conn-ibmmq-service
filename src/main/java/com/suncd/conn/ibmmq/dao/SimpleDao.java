/*
成都太阳高科技有限责任公司
http://www.suncd.com
*/
package com.suncd.conn.ibmmq.dao;

import java.util.List;
import java.util.Map;

public interface SimpleDao {
    Map<String, Object> query(String var1);

    List<Map<String, Object>> queryList(String var1);

    int update(String var1);
}
