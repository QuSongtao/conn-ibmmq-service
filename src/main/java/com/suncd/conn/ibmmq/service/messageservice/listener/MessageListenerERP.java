package com.suncd.conn.ibmmq.service.messageservice.listener;

import com.suncd.conn.ibmmq.system.constants.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.annotation.JmsListeners;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import javax.jms.Message;

/**
 * ERP消息侦听
 * 可以根据业务量大小扩展消息侦听器
 *
 * @author qust
 * @version 1.0 20180927
 */
@Component
public class MessageListenerERP extends MessageListenerAdapter {

    private static final Logger WARN_LOGGER = LoggerFactory.getLogger("warnAndErrorLogger");

    @Autowired
    private ReceiveService receiveService;

    @Override
    @JmsListeners(value = {
            @JmsListener(destination = "ERP.CR.O.OK28"),
            @JmsListener(destination = "ERP.CR.O.OL28"),
            @JmsListener(destination = "ERP.CR.O.OO28"),
            @JmsListener(destination = "ERP.CR.Q.1"),
            @JmsListener(destination = "ERP.CR.Q.2"),
            @JmsListener(destination = "ERP.CR.W.BUL"),
            @JmsListener(destination = "ERP.CR.W.OTHER")
    })
    public void onMessage(Message message) {
        receiveService.dealMessage(message, Constant.ERP_CODE);
    }

    @Override
    protected void handleListenerException(Throwable e) {
        WARN_LOGGER.error(e.getMessage(), e);
    }
}
