/*
成都太阳高科技有限责任公司
http://www.suncd.com
*/
package com.suncd.conn.ibmmq.service.messageservice.sender;

import com.ibm.mq.jms.MQConnectionFactory;
import com.suncd.conn.ibmmq.dao.ConnConfTelDao;
import com.suncd.conn.ibmmq.dao.ConnSendMainDao;
import com.suncd.conn.ibmmq.dao.ConnSendMainHisDao;
import com.suncd.conn.ibmmq.dao.ConnSendMsgDao;
import com.suncd.conn.ibmmq.entity.ConnConfTel;
import com.suncd.conn.ibmmq.entity.ConnSendMain;
import com.suncd.conn.ibmmq.entity.ConnSendMainHis;
import com.suncd.conn.ibmmq.entity.ConnSendMsg;
import com.suncd.conn.ibmmq.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MessageSendServiceImp implements MessageSendService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSendServiceImp.class);
    @Autowired
    private JmsOperations jmsOperations;
    @Autowired
    private ConnSendMainDao connSendMainDao;
    @Autowired
    private ConnSendMainHisDao connSendMainHisDao;
    @Autowired
    private ConnSendMsgDao connSendMsgDao;
    @Autowired
    private ConnConfTelDao connConfTelDao;
//
//    @Autowired
//    private MQConnectionFactory mqConnectionFactory;

    @Override
    public void sendMessage(String destinationName, String message) {
        // 所有消息转为字节发送
        byte[] msgBuf = message.getBytes();
        jmsOperations.convertAndSend(destinationName, msgBuf);
    }

    @Override
    public void sendMessage(String destinationName, byte[] msgBuf) {
        // 所有消息转为字节发送
        jmsOperations.convertAndSend(destinationName, msgBuf);
    }

    @PostConstruct
    public void testSend() {
        sendMessage("WIN.LUX.Q", "cgx中文123");
    }

    @Scheduled(fixedDelay = 5000)
    public void circleSend() {
        // 查询待发送为MQ的消息
        List<ConnSendMain> connSendMains = connSendMainDao.selectBySendFlag("0", "MQ");
        for (ConnSendMain connSendMain : connSendMains) {
            String sendFlag;
            String sendReslut;
            // 1.组装消息及队列
            String telId = connSendMain.getTelId();
            String msgId = connSendMain.getMsgId();
            String queueName = getQueueName(telId);
            byte[] msgBuf = getSendMsg(msgId);
            if (queueName != null && msgBuf != null) {
                // 2.发送消息
                sendMessage(queueName, msgBuf);

                // 3.记录发送日志到文件
                LOGGER.info(new String(msgBuf));
                sendReslut = "发送成功!";
                sendFlag = "1";
            }else{
                if(queueName == null){
                    sendReslut = "发送失败: 在CONN_CONF_TEL表没有配置对应的队列";
                }else{
                    sendReslut = "发送失败: 发送消息内容为空,消息表:CONN_SEND_MSG";
                }
                sendFlag = "0";
            }

            // 3.按主键删除发送总表记录
            connSendMainDao.deleteByPrimaryKey(connSendMain.getId());

            // 4.插入发送历史表
            ConnSendMainHis connSendMainHis = new ConnSendMainHis();
            connSendMainHis.setId(UUID.randomUUID().toString());
            connSendMainHis.setCreateTime(connSendMain.getCreateTime());
            connSendMainHis.setSendTime(new Date());
            connSendMainHis.setMsgId(connSendMain.getMsgId());
            connSendMainHis.setSendResult(sendReslut);
            connSendMainHis.setSendFlag(sendFlag);
            connSendMainHis.setTelId(connSendMain.getTelId());
            connSendMainHis.setTelType(connSendMain.getTelType());
            connSendMainHisDao.insertSelective(connSendMainHis);
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

    /**
     * 从队列配置表中获取电文id对应的队列
     *
     * @param telId 电文id
     * @return 队列名称
     */
    private String getQueueName(String telId) {
        String queueName = null;
        ConnConfTel connConfTel = connConfTelDao.selectQueueName(telId);
        if (null != connConfTel) {
            queueName = connConfTel.getQueueName();
        }
        if (queueName == null) {
            CommonUtil.SYSLOGGER.warn("【警告】电文ID: {} 在CONN_CONF_TEL表没有配置对应的队列", telId);
        }
        return queueName;
    }
}
