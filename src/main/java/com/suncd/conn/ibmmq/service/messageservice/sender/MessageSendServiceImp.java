/*
成都太阳高科技有限责任公司
http://www.suncd.com
*/
package com.suncd.conn.ibmmq.service.messageservice.sender;

import com.ibm.mq.jms.MQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class MessageSendServiceImp implements MessageSendService {

    @Autowired
    private JmsOperations jmsOperations;

    @Autowired
    private MQConnectionFactory mqConnectionFactory;

    @Override
    public void sendMessage(String destinationName, String message) {
        // 所有消息转为字节发送
        byte[] msgBuf = message.getBytes();
        jmsOperations.convertAndSend(destinationName, msgBuf);
    }

    @PostConstruct
    public void testSend() {
        sendMessage("WIN.LUX.Q", "cgx中文123");
    }
}
