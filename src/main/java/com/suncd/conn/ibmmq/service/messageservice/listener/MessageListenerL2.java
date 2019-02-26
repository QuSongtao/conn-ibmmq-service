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
 * 二级系统消息侦听
 * 可以根据业务量大小扩展消息侦听器
 *
 * @author qust
 * @version 1.0 20180927
 */
@Component
public class MessageListenerL2 extends MessageListenerAdapter {

    private static final Logger WARN_LOGGER = LoggerFactory.getLogger("warnAndErrorLogger");

    @Autowired
    private ReceiveService receiveService;

    @Override
    @JmsListeners(value = {
            @JmsListener(destination = "${ibm.recv.queue.GAL1_CR_Q}"),  // 1#镀锌
            @JmsListener(destination = "${ibm.recv.queue.GAL3_CR_Q}"),  // 3#镀锌
            @JmsListener(destination = "${ibm.recv.queue.CF_CR_Q}"),    // 清洗机组
            @JmsListener(destination = "${ibm.recv.queue.PCSAW_CR_Q}"), // 酸洗机组
            @JmsListener(destination = "${ibm.recv.queue.CC_CR_M_1}")   // 彩涂机组
    })
    public void onMessage(Message message) {
        receiveService.dealMessage(message);
    }

    @Override
    protected void handleListenerException(Throwable e) {
        WARN_LOGGER.error(e.getMessage(), e);
    }
}
