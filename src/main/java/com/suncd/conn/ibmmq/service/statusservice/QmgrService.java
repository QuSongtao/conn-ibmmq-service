/*
成都太阳高科技有限责任公司
http://www.suncd.com
*/
package com.suncd.conn.ibmmq.service.statusservice;

public interface QmgrService {

    int getLocalQueueDepth(String qName);

    int getChannelStatus(String channelName);
}
