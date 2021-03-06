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
import com.suncd.conn.ibmmq.system.constants.Constant;
import com.suncd.conn.ibmmq.utils.QmgrFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class QmgrServiceImp implements QmgrService {
    private static final Logger LOGGER = LoggerFactory.getLogger(QmgrServiceImp.class);

    @Value("${ibm.mq.queue-manager}")
    private String qmgrName;

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
    public String getLocalQueueDepth(String qName) {
        checkQmgr();
        int qOption = CMQC.MQOO_FAIL_IF_QUIESCING | CMQC.MQOO_INQUIRE | CMQC.MQOO_BROWSE;
        MQQueue queue;
        try {
            queue = mqQueueManager.accessQueue(qName, qOption);
            int depth = queue.getCurrentDepth();
            queue.close();
            return depth + "条";
        } catch (MQException e) {
            LOGGER.error("队列:{},深度未知:{}", qName, e.getMessage());
            reconnect(e);
            return "未知";
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
     * -1-未知状态
     */
    @Override
    public int getChannelStatus(String channelName) {
        checkQmgr();
        PCFMessageAgent agent = null;
        int channelStatus;
        try {
            agent = new PCFMessageAgent(mqQueueManager);
            PCFMessage pcfRequest = new PCFMessage(CMQCFC.MQCMD_INQUIRE_CHANNEL_STATUS);
            pcfRequest.addParameter(CMQCFC.MQCACH_CHANNEL_NAME, channelName);
            PCFMessage[] response = agent.send(pcfRequest);
            channelStatus = response[0].getIntParameterValue(CMQCFC.MQIACH_CHANNEL_STATUS);
            LOGGER.debug("通道:{} 状态:{}", channelName, channelStatus);
        } catch (IOException e) {
            LOGGER.warn("通道:{},状态异常:{}", channelName, e.getMessage());
            channelStatus = -9;
        } catch (MQDataException eh) {
            LOGGER.warn("通道:{},状态异常:{}", channelName, eh.getMessage());
            reconnect(eh);
            channelStatus = -1;
        } finally {
            try {
                if (null != agent) {
                    agent.disconnect();
                }
            } catch (MQDataException e) {
                LOGGER.warn("PCFMessageAgent主动断开出现异常,可忽略!");
            }
        }
        return channelStatus;
    }

    /**
     * 通道控制
     *
     * MQCMD_RESET_CHANNEL = 27;
     * MQCMD_START_CHANNEL = 28;
     * MQCMD_STOP_CHANNEL = 29;
     *
     * @param channelName 通道名称
     * @param handleCode  控制代码
     *                    CMQCFC.MQCMD_RESET_CHANNEL = 27; 重置通道
     *                    CMQCFC.MQCMD_START_CHANNEL = 28; 启动通道
     *                    CMQCFC.MQCMD_STOP_CHANNEL = 29;  停止通道
     * @return int 完成代码 0-成功  非0-异常
     */
    @Override
    public int controlChannel(String channelName, int handleCode) {
        checkQmgr();
        PCFMessageAgent agent = null;
        int compCode;
        try {
            agent = new PCFMessageAgent(mqQueueManager);
            PCFMessage pcfRequest = new PCFMessage(handleCode);
            pcfRequest.addParameter(CMQCFC.MQCACH_CHANNEL_NAME, channelName);
            PCFMessage[] response = agent.send(pcfRequest);
            compCode = response[0].getCompCode();
            LOGGER.debug("控制通道:{}, 动作:{}, 成功!", channelName, ctlString(handleCode));
        } catch (IOException e) {
            LOGGER.warn("控制通道:{}, 动作:{},状态异常:{}", channelName, ctlString(handleCode), e.getMessage());
            compCode = -9;
        } catch (MQDataException eh) {
            LOGGER.warn("控制通道:{}, 动作:{},状态异常:{}", channelName, ctlString(handleCode), eh.getMessage());
            reconnect(eh);
            compCode = -1;
        } finally {
            try {
                if (null != agent) {
                    agent.disconnect();
                }
            } catch (MQDataException e) {
                LOGGER.warn("PCFMessageAgent主动断开出现异常,可忽略!");
            }
        }
        return compCode;
    }

    /**
     * 获取队列管理器状态
     *
     * @return map {'qmgrName':'XXXX.QM','status':'RUNNING or DOWN'}
     */
    @Override
    public Map getQmgrStatus() {
        Map<String, String> map = new HashMap<>();
        map.put("qmgrName", qmgrName);
        getChannelStatus("SYSTEM.DEF.SVRCONN");
        map.put("status", Constant.QMGR_STATUS == 0 ? "DOWN" : "RUNNING");
        return map;
    }

    private String ctlString(int handleCode){
        switch (handleCode){
            case 27: return "重置";
            case 28: return "启动";
            case 29: return "停止";
            default: return "";
        }
    }

    /**
     * 服务启动时检测队列管理器状态
     * 能够连接上则证明队列管理器正常
     */
    @PostConstruct
    private void checkQmgr() {
        if (this.mqQueueManager == null) {
            this.mqQueueManager = qmgrFactory.createMqQueueManager();
        }
    }

    /**
     * MQ 2009错误代码 为队列管理器连接异常
     *
     * @param e MQException
     */
    private void reconnect(MQException e) {
        if (e.getReason() == 2009 || e.getReason() == 2017) {
            this.mqQueueManager = qmgrFactory.createMqQueueManager();
        }
    }

    /**
     * MQ 2009错误代码 为队列管理器连接异常
     *
     * @param e MQDataException
     */
    private void reconnect(MQDataException e) {
        if (e.getReason() == 2009 || e.getReason() == 2017) {
            this.mqQueueManager = qmgrFactory.createMqQueueManager();
        }
    }
}
