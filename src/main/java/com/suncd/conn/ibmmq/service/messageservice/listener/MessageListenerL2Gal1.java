package com.suncd.conn.ibmmq.service.messageservice.listener;

import com.suncd.conn.ibmmq.system.constants.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import javax.jms.Message;

/**
 * 二级系统消息侦听: 1#镀锌机组
 *
 * @author qust
 * @version 1.0 20190318
 */
@Component
public class MessageListenerL2Gal1 extends MessageListenerAdapter {

    private static final Logger WARN_LOGGER = LoggerFactory.getLogger("warnAndErrorLogger");
    @Autowired
    private ReceiveService receiveService;

    @Override
    @JmsListener(destination = "GAL1.CR.Q")
    public void onMessage(Message message) {
        receiveService.dealMessage(message, Constant.L2_GAL1, false, 10);
    }

    @Override
    protected void handleListenerException(Throwable e) {
        WARN_LOGGER.error(e.getMessage(), e);
    }
}
