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
            @JmsListener(destination = "${ibm.recv.queue.ERP_CR_O_OK28}"),
            @JmsListener(destination = "${ibm.recv.queue.ERP_CR_O_OL28}"),
            @JmsListener(destination = "${ibm.recv.queue.ERP_CR_O_OO28}"),
            @JmsListener(destination = "${ibm.recv.queue.ERP_CR_Q_1}"),
            @JmsListener(destination = "${ibm.recv.queue.ERP_CR_Q_2}"),
            @JmsListener(destination = "${ibm.recv.queue.ERP_CR_W_BUL}"),
            @JmsListener(destination = "${ibm.recv.queue.ERP_CR_W_OTHER}")
    })
    public void onMessage(Message message) {
        receiveService.dealMessage(message, Constant.ERP_CODE);
    }

    @Override
    protected void handleListenerException(Throwable e) {
        WARN_LOGGER.error(e.getMessage(), e);
    }
}
