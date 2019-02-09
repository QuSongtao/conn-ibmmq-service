/*
成都太阳高科技有限责任公司
http://www.suncd.com
*/
package com.suncd.conn.ibmmq.service.messageservice.listener;

import com.ibm.jms.JMSBytesMessage;
import com.suncd.conn.ibmmq.dao.ConnRecvMainDao;
import com.suncd.conn.ibmmq.dao.ConnRecvMsgDao;
import com.suncd.conn.ibmmq.dao.ConnTotalNumDao;
import com.suncd.conn.ibmmq.entity.ConnRecvMain;
import com.suncd.conn.ibmmq.entity.ConnRecvMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.annotation.JmsListeners;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.Date;
import java.util.UUID;

/**
 * 消息侦听
 * 可以根据业务量大小扩展消息侦听器
 *
 * @author qust
 * @version 1.0 20180927
 */
@Component
public class ReceiveMessage extends MessageListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveMessage.class);
    private static final Logger WARN_LOGGER = LoggerFactory.getLogger("warnAndErrorLogger");

    @Autowired
    private ConnRecvMainDao connRecvMainDao;
    @Autowired
    private ConnRecvMsgDao connRecvMsgDao;
    @Autowired
    private ConnTotalNumDao connTotalNumDao;

    @Override
    @JmsListeners(value = {
            @JmsListener(destination = "${ibm.recv.queue.q1}")//, // xxx队列
            //@JmsListener(destination = "Q2")                    // xxx队列
    })
    public void onMessage(Message message) {
        // 1.监听并读取消息
        String recvStrMsg = "";
        if (message instanceof JMSBytesMessage) {
            WARN_LOGGER.info("字节类型的消息");
            JMSBytesMessage bm = (JMSBytesMessage) message;
            byte[] buff;
            try {
                buff = new byte[(int) bm.getBodyLength()];
                bm.readBytes(buff);
                recvStrMsg = new String(buff);
            } catch (Exception e) {
                WARN_LOGGER.error(e.getMessage(), e);
            }
        } else {
            WARN_LOGGER.info("文本类型的消息");
            TextMessage bm = (TextMessage) message;
            try {
                recvStrMsg = bm.getText();
            } catch (JMSException e) {
                WARN_LOGGER.error(e.getMessage(), e);
            }
        }

        // 2.记录消息日志
        LOGGER.info("收到消息:{}", recvStrMsg);
        String telId;
        String totalType = "RR"; // 统计接收累计标识 RR-正常接收消息 RE-异常接收消息
        if (recvStrMsg.length() < 10) { // 判断消息长度是否>10
            telId = "LENGTH<10";
            totalType = "RE";
        } else {
            telId = recvStrMsg.substring(0, 10);
        }

        try {
            // 3.插入接收消息表
            String msgId = UUID.randomUUID().toString();
            ConnRecvMsg connRecvMsg = new ConnRecvMsg();
            connRecvMsg.setId(msgId);
            connRecvMsg.setMsgTxt(recvStrMsg);
            connRecvMsg.setCreateTime(new Date());
            connRecvMsgDao.insertSelective(connRecvMsg);

            // 4.插入接收总表
            String mainId = UUID.randomUUID().toString();
            ConnRecvMain connRecvMain = new ConnRecvMain();
            connRecvMain.setId(mainId);
            connRecvMain.setDealFlag("0");
            connRecvMain.setMsgId(msgId);
            connRecvMain.setTelId(telId);
            connRecvMain.setRecvTime(new Date());
            connRecvMain.setTelType("MQ");
            connRecvMainDao.insertSelective(connRecvMain);

            // 5.更新统计表
            connTotalNumDao.updateTotalNum(totalType);
        }catch (Exception e){
            WARN_LOGGER.error("MQ消息处理出现异常:",e);
        }
    }

    @Override
    protected void handleListenerException(Throwable e) {
        WARN_LOGGER.error(e.getMessage(), e);
    }
}
