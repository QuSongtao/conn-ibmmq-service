/*
成都太阳高科技有限责任公司
http://www.suncd.com
*/
package com.suncd.conn.ibmmq.service.statusservice;


import com.suncd.conn.ibmmq.utils.Response;

public interface ConnObjectService {

    Response getObjByType(String objType, String transferType, int pageIndex, int pageSize);
}
