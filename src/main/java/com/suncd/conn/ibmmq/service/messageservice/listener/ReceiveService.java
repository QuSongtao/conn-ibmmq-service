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
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.Date;
import java.util.UUID;

/**
 * 消息数据处理服务,处理MQ消息到数据库
 *
 * @author qust
 * @version 1.0 20190226
 */
@Service
public class ReceiveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveService.class);
    private static final Logger WARN_LOGGER = LoggerFactory.getLogger("warnAndErrorLogger");

    @Autowired
    private ConnRecvMainDao connRecvMainDao;
    @Autowired
    private ConnRecvMsgDao connRecvMsgDao;
    @Autowired
    private ConnTotalNumDao connTotalNumDao;

    /**
     * 接收消息核心处理逻辑
     *
     * @param message  消息体
     * @param sysCode  系统编码
     */
    public void dealMessage(Message message,String sysCode) {
        // 0. 根据sysCode查找对应的中文名称
        // todo
        // 1.监听并读取消息
        String recvStrMsg = "";
        if (message instanceof JMSBytesMessage) {
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
            TextMessage bm = (TextMessage) message;
            try {
                recvStrMsg = bm.getText();
            } catch (JMSException e) {
                WARN_LOGGER.error(e.getMessage(), e);
            }
        }

        // 2.记录消息日志
        LOGGER.info(recvStrMsg);
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

}
