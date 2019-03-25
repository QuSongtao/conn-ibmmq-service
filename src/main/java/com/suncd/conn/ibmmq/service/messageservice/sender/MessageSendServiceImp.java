package com.suncd.conn.ibmmq.service.messageservice.sender;

import com.suncd.conn.ibmmq.dao.*;
import com.suncd.conn.ibmmq.entity.*;
import com.suncd.conn.ibmmq.system.constants.Constant;
import com.suncd.conn.ibmmq.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

//import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class MessageSendServiceImp implements MessageSendService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSendServiceImp.class);
    @Autowired
    private JmsTemplate jmsOperations;
    @Autowired
    private ConnSendMainDao connSendMainDao;
    @Autowired
    private ConnSendMainHisDao connSendMainHisDao;
    @Autowired
    private ConnSendMsgDao connSendMsgDao;
    @Autowired
    private ConnConfTelDao connConfTelDao;
    @Autowired
    private ConnTotalNumDao connTotalNumDao;
    @Autowired
    private ConnConfSyscodeDao connConfSyscodeDao;
    @Autowired
    private MessagePTPService messagePTPService;

    @Override
    public void sendMessage(String destinationName, String message) {
        // 所有消息转为字节发送
        byte[] msgBuf = message.getBytes();
        jmsOperations.convertAndSend(destinationName, msgBuf);
    }

    @Override
    public void sendMessageJMS(String destinationName, byte[] msgBuf, Map<String, Object> result) {
        // 所有消息转为字节发送
        try {
            jmsOperations.convertAndSend(destinationName, msgBuf);
            result.put("sendResult", "发送成功!");
            result.put("sendFlag", "1");
            result.put("totalType", "SS");
        } catch (Exception e) {
            result.put("sendResult", "发送失败!" + e.getMessage());
            result.put("sendFlag", "0");
            result.put("totalType", "SE");
            CommonUtil.SYSLOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void sendMessagePTP(String destinationName, byte[] msgBuf, Map<String, Object> result) {
        messagePTPService.sendMessagePTP(destinationName, msgBuf, result);
    }

//    @PostConstruct
//    public void testSend() {
//        sendMessage("WIN.LUX.Q", "cgx22222222222222222中文1233333333333333333333333333");
//    }

    @Scheduled(fixedDelay = 5000)
    public void circleSend() {
        // 查询待发送为MQ的消息
        List<ConnSendMain> connSendMains = connSendMainDao.selectAll();
        for (ConnSendMain connSendMain : connSendMains) {
            Map<String, Object> result = new HashMap<>();

            // 1. 判断待发消息是否为MQ通信
            checkConnType(connSendMain.getReceiver(), result);
            if (!(boolean) result.get("isMQ")) {
                // 不为MQ通信则直接跳过
                continue;
            }

            // 2.组装消息和查找路由队列
            String queueName = getQueueName(connSendMain.getTelId(), connSendMain.getSender(), connSendMain.getReceiver());
            byte[] msgBuf = getSendMsg(connSendMain.getMsgId());

            // 3.发送消息
            sendToRemote(queueName, msgBuf, result);

            // 4.按主键删除发送总表记录
            connSendMainDao.deleteByPrimaryKey(connSendMain.getId());

            // 5.插入发送历史表
            ConnSendMainHis connSendMainHis = new ConnSendMainHis();
            connSendMainHis.setId(UUID.randomUUID().toString());
            connSendMainHis.setCreateTime(connSendMain.getCreateTime());
            connSendMainHis.setSendTime(new Date());
            connSendMainHis.setMsgId(connSendMain.getMsgId());
            connSendMainHis.setSendResult(result.get("sendResult").toString());
            connSendMainHis.setSendFlag(result.get("sendFlag").toString());
            connSendMainHis.setTelId(connSendMain.getTelId());
            connSendMainHis.setSender(connSendMain.getSender());
            connSendMainHis.setSenderName(connSendMain.getSenderName());
            connSendMainHis.setReceiver(connSendMain.getReceiver());
            connSendMainHis.setReceiverName(connSendMain.getReceiverName());
            connSendMainHisDao.insertSelective(connSendMainHis);

            // 6.更新统计表
            connTotalNumDao.updateTotalNum(result.get("totalType").toString());
        }
    }

    /**
     * 获取发送消息字节组
     *
     * @param msgId 消息id
     * @return 消息字节组
     */
    private byte[] getSendMsg(String msgId) {
        byte[] ret = null;
        ConnSendMsg connSendMsg = connSendMsgDao.selectByPrimaryKey(msgId);
        if (null != connSendMsg && null != connSendMsg.getMsgTxt()) {
            ret = connSendMsg.getMsgTxt().getBytes();
        }
        if (ret == null) {
            CommonUtil.SYSLOGGER.warn("【警告】消息内容为空,消息表:CONN_SEND_MSG,主键:{}", msgId);
        }
        return ret;
    }

    private void sendToRemote(String queueName, byte[] msgBuf, Map<String, Object> result) {
        if (queueName != null && msgBuf != null) {
            // 1.发送消息
            if ((boolean) result.get("isJMS")) {
                sendMessageJMS(queueName, msgBuf, result);
            } else {
                sendMessagePTP(queueName, msgBuf, result);
            }

            // 2.记录发送日志到文件
            LOGGER.info(new String(msgBuf));

        } else {
            if (queueName == null) {
                result.put("sendResult", "发送失败: 在CONN_CONF_TEL表没有配置对应的队列");
            } else {
                result.put("sendResult", "发送失败: 发送消息内容为空,消息表:CONN_SEND_MSG");
            }
            result.put("sendFlag", "0");
            result.put("totalType", "SE");
        }
    }

    /**
     * 从队列配置表中获取电文id对应的队列
     *
     * @param telId 电文id
     * @return 队列名称
     */
    private String getQueueName(String telId, String sender, String receiver) {
        String queueName = null;
        ConnConfTel connConfTel = connConfTelDao.selectQueueName(telId, sender, receiver);
        if (null != connConfTel) {
            queueName = connConfTel.getQueueName();
        }
        if (queueName == null) {
            CommonUtil.SYSLOGGER.warn("【警告】电文ID: {} 在CONN_CONF_TEL表没有配置对应的队列", telId);
        }
        return queueName;
    }

    /**
     * 根据通信系统编码判断通信类型
     *
     * @param sysCode 通信系统编码
     */
    private void checkConnType(String sysCode, Map<String, Object> result) {
        ConnConfSyscode connConfSyscode = connConfSyscodeDao.selectBySysCode(sysCode);
        if (null == connConfSyscode) {
            CommonUtil.SYSLOGGER.warn("【警告】通信系统编码:{} 没有在CONN_CONF_SYSCODE表中定义！", sysCode);
            result.put("isMQ", false);
        } else {
            result.put("isJMS", connConfSyscode.getConnProperties().equals(Constant.MQ_JMS));
            result.put("isMQ", connConfSyscode.getConnType().equals(Constant.CONN_MQ));
        }
    }
}
