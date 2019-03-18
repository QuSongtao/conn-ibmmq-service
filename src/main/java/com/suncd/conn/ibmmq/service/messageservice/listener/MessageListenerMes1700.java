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
 * MES系统消息侦听: 1700
 *
 * @author qust
 * @version 1.0 20190318
 */
@Component
public class MessageListenerMes1700 extends MessageListenerAdapter {

    private static final Logger WARN_LOGGER = LoggerFactory.getLogger("warnAndErrorLogger");

    @Autowired
    private ReceiveService receiveService;

    @Override
    @JmsListener(destination = "ASP_1700.CR.M.1")
    public void onMessage(Message message) {
        receiveService.dealMessage(message, Constant.MES_1700);
    }

    @Override
    protected void handleListenerException(Throwable e) {
        WARN_LOGGER.error(e.getMessage(), e);
    }
}
