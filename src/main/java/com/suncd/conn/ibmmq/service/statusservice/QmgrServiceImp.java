/*
成都太阳高科技有限责任公司
http://www.suncd.com
*/
package com.suncd.conn.ibmmq.service.statusservice;

import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.constants.CMQCFC;
import com.ibm.mq.headers.MQDataException;
import com.ibm.mq.headers.pcf.PCFMessage;
import com.ibm.mq.headers.pcf.PCFMessageAgent;
import com.suncd.conn.ibmmq.system.configuration.IbmMqJmsConfiguration;
import com.suncd.conn.ibmmq.utils.QmgrFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class QmgrServiceImp implements QmgrService {
    private static final Logger LOGGER = LoggerFactory.getLogger(QmgrServiceImp.class);

    @Autowired
    private QmgrFactory qmgrFactory;

    private MQQueueManager mqQueueManager;

    /**
     * 按队列名称获取队列深度
     * <p>
     * CMQC.MQOO_FAIL_IF_QUIESCING  如果队列管理器停止则返回失败
     * CMQC.MQOO_INPUT_AS_Q_DEF     以队列默认读取方式打开队列
     * CMQC.MQOO_OUTPUT             以写方式打开队列
     * CMQC.MQOO_BROWSE             以浏览方式打开队列
     * CMQC.MQOO_INQUIRE            和读取队列的深度有关系，如果用到队列深度的话，必须采用这个参数。
     *
     * @param qName 队列名称
     * @return 队列深度
     */
    @Override
    public int getLocalQueueDepth(String qName) {
        if(this.mqQueueManager == null){
            this.mqQueueManager = qmgrFactory.createmqQueueManager();
        }
        int qOption = CMQC.MQOO_FAIL_IF_QUIESCING | CMQC.MQOO_INQUIRE | CMQC.MQOO_BROWSE;
        MQQueue queue;
        try {
            queue = mqQueueManager.accessQueue(qName, qOption);
            int depth = queue.getCurrentDepth();
            queue.close();
            return depth;
        } catch (MQException e) {
            LOGGER.error(e.getMessage());
            if(e.getReason() == 2009){
                this.mqQueueManager = qmgrFactory.createmqQueueManager();
            }
            return -1;
        }
    }

    /**
     * 按通道名称获取通道状态
     *
     * @param channelName 通道名称
     * @return 通道状态int类型
     * 0-正在初始化
     * 1-正在绑定
     * 3-正在运行
     * 5-正在重试
     * 6-已停止
     * -1-不活动的
     */
    @Override
    public int getChannelStatus(String channelName) {
        if(this.mqQueueManager == null){
            this.mqQueueManager = qmgrFactory.createmqQueueManager();
        }
        try {
            PCFMessageAgent agent = new PCFMessageAgent(mqQueueManager);
            PCFMessage pcfRequest = new PCFMessage(CMQCFC.MQCMD_INQUIRE_CHANNEL_STATUS);
            pcfRequest.addParameter(CMQCFC.MQCACH_CHANNEL_NAME, channelName);
            PCFMessage[] response = agent.send(pcfRequest);
            int channelStatus = response[0].getIntParameterValue(CMQCFC.MQIACH_CHANNEL_STATUS);
            LOGGER.info("通道状态:{}",channelStatus);
            agent.disconnect();
            return channelStatus;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return -9;
        } catch (MQDataException eh){
            LOGGER.error(eh.getMessage());
            if(eh.getReason() == 2009){
                this.mqQueueManager = qmgrFactory.createmqQueueManager();
            }
            return -1;
        }
    }
}
