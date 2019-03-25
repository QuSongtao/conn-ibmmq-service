/*
成都太阳高科技有限责任公司
http://www.suncd.com
*/
package com.suncd.conn.ibmmq.service.messageservice.sender;

import java.util.Map;

public interface MessageSendService {
    void sendMessage(String destinationName,String message);
    void sendMessageJMS(String destinationName, byte[] msgBuf, Map<String, Object> result);
    void sendMessagePTP(String destinationName, byte[] msgBuf, Map<String, Object> result);
}
