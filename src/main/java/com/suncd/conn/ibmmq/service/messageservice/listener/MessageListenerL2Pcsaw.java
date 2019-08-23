package com.suncd.conn.ibmmq.service.messageservice.listener;

import com.suncd.conn.ibmmq.service.messageservice.sender.MessagePTPService;
import com.suncd.conn.ibmmq.system.constants.Constant;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jms.annotation.JmsListener;
//import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//import javax.jms.Message;

/**
 * 二级系统消息侦听: 酸洗机组
 *
 * @author qust
 * @version 1.0 20190318
 * @version 1.1 20190823  处理中文字符集问题,改为传统方式获取消息
 */
@Component
public class MessageListenerL2Pcsaw {

//    private static final Logger WARN_LOGGER = LoggerFactory.getLogger("warnAndErrorLogger");

    @Autowired
    private ReceiveService receiveService;

    @Autowired
    private MessagePTPService messagePTPService;

    @Scheduled(fixedDelay = 3000)
    public void handleMessage() {
        String msgStr = messagePTPService.recvMsg("PCSAW.CR.Q");
        receiveService.handleMsg(msgStr, Constant.L2_PCSAW, 4);
    }

//    @Override
//    @JmsListener(destination = "PCSAW.CR.Q")
//    public void onMessage(Message message) {
//        receiveService.dealMessage(message, Constant.L2_PCSAW, false, 4);
//    }
//
//    @Override
//    protected void handleListenerException(Throwable e) {
//        WARN_LOGGER.error(e.getMessage(), e);
//    }
}
