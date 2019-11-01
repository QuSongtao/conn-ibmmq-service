package com.suncd.conn.ibmmq.service.messageservice.listener;

import com.suncd.conn.ibmmq.service.messageservice.sender.MessagePTPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class For1700Recv {
    private static final Logger LOGGER = LoggerFactory.getLogger(For1700Recv.class);
    @Autowired
    private MessagePTPService messagePTPService;


    @Scheduled(fixedDelay = 3000)
    public void getMsg1700(){
        String msg = messagePTPService.recvMsgPTP("ASP_1700.CR.M.1");
        LOGGER.info(msg);
    }
}
