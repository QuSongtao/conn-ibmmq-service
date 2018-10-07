/*
成都太阳高科技有限责任公司
http://www.suncd.com
*/
package com.suncd.conn.ibmmq.utils;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.suncd.conn.ibmmq.system.constants.Constant;

@Component
public class QmgrFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(QmgrFactory.class);

    @Value("${ibm.mq.queue-manager}")
    private String qmName;
    @Value("${ibm.mq.hostDef}")
    private String host;
    @Value("${ibm.mq.portDef}")
    private int port;
    @Value("${ibm.mq.channel}")
    private String channel;
    @Value("${ibm.mq.user}")
    private String user;
    @Value("${ibm.mq.password}")
    private String password;
    @Value("${ibm.mq.ccsidDef}")
    private int ccsid;

    public MQQueueManager createmqQueueManager(){
        MQEnvironment.hostname = host;
        MQEnvironment.port = port;
        MQEnvironment.userID = user;
        MQEnvironment.password = password;
        MQEnvironment.channel = channel;
        MQEnvironment.CCSID = ccsid;
        MQQueueManager mqQueueManager = null;
        try {
            mqQueueManager = new MQQueueManager(qmName);
            Constant.QMGR_STATUS = 1;
            LOGGER.error("初始化队列管理器完成!");
        } catch (MQException e) {
            Constant.QMGR_STATUS = 0;
            LOGGER.error("初始化队列管理器出现异常",e);
        }
        return mqQueueManager;
    }
}
