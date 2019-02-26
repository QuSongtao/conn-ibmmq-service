package com.suncd.conn.ibmmq.service.messageservice.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.annotation.JmsListeners;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import javax.jms.Message;

/**
 * MES消息侦听
 * 可以根据业务量大小扩展消息侦听器
 *
 * @author qust
 * @version 1.0 20180927
 */
@Component
public class MessageListenerMES extends MessageListenerAdapter {

    private static final Logger WARN_LOGGER = LoggerFactory.getLogger("warnAndErrorLogger");

    @Autowired
    private ReceiveService receiveService;

    @Override
    @JmsListeners(value = {
            @JmsListener(destination = "${ibm.recv.queue.2130_CR_M_1}"),    // 2130
            @JmsListener(destination = "${ibm.recv.queue.2150_CR_M_1}"),    // 2150
            @JmsListener(destination = "${ibm.recv.queue.ASP_1700_CR_M_1}"),// 1700
            @JmsListener(destination = "${ibm.recv.queue.HR_1780_CR_M_1}"), // 1780
            @JmsListener(destination = "${ibm.recv.queue.LZ4M1_CR_M_1}"),   // 1450线
            @JmsListener(destination = "${ibm.recv.queue.TEST_CR}"),        // 检化验系统
            @JmsListener(destination = "${ibm.recv.queue.ASL_CR_Q}")        // 鞍神高强线
    })
    public void onMessage(Message message) {
        receiveService.dealMessage(message);
    }

    @Override
    protected void handleListenerException(Throwable e) {
        WARN_LOGGER.error(e.getMessage(), e);
    }
}
