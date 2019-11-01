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
    @Value("${ibm.mq.conn-name}")
    private String hostPort;
    @Value("${ibm.mq.channel}")
    private String channel;
    @Value("${ibm.mq.user}")
    private String user;
    @Value("${ibm.mq.password}")
    private String password;
    @Value("${ibm.mq.ccsidDef}")
    private int ccsid;

    public MQQueueManager createMqQueueManager(){
        MQEnvironment.hostname = hostPort.split("\\(")[0];
        MQEnvironment.port = Integer.parseInt(hostPort.split("\\(")[1].substring(0,hostPort.split("\\(")[1].length() - 1));
        MQEnvironment.userID = user;
        MQEnvironment.password = password;
        MQEnvironment.channel = channel;
        MQEnvironment.CCSID = ccsid;
        MQQueueManager mqQueueManager = null;
        LOGGER.info("字符集:{}", ccsid);
        try {
            mqQueueManager = new MQQueueManager(qmName);
            Constant.QMGR_STATUS = 1;
            LOGGER.info("初始化队列管理器完成!");
        } catch (MQException e) {
            Constant.QMGR_STATUS = 0;
            LOGGER.error("初始化队列管理器出现异常",e);
        }
        return mqQueueManager;
    }
}
