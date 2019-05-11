package com.suncd.conn.ibmmq.service.messageservice.listener;

import com.ibm.jms.JMSBytesMessage;
import com.ibm.jms.JMSTextMessage;
import com.suncd.conn.ibmmq.dao.ConnConfSyscodeDao;
import com.suncd.conn.ibmmq.dao.ConnRecvMainDao;
import com.suncd.conn.ibmmq.dao.ConnRecvMsgDao;
import com.suncd.conn.ibmmq.dao.ConnTotalNumDao;
import com.suncd.conn.ibmmq.entity.ConnConfSyscode;
import com.suncd.conn.ibmmq.entity.ConnRecvMain;
import com.suncd.conn.ibmmq.entity.ConnRecvMsg;
import com.suncd.conn.ibmmq.system.constants.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.Message;
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
    @Autowired
    private ConnConfSyscodeDao connConfSyscodeDao;

    /**
     * 接收消息核心处理逻辑
     *
     * @param message  消息体
     * @param sysCode  系统编码
     */
    public void dealMessage(Message message,String sysCode,boolean charset, int headLength) {
        // 0. 根据sysCode查找对应的中文名称
        ConnConfSyscode connConfSyscode = connConfSyscodeDao.selectBySysCode(sysCode);
        ConnConfSyscode connConfSyscodeCr = connConfSyscodeDao.selectBySysCode(Constant.MES_CR);

        // 1.监听并读取消息
        String recvStrMsg = "";
        if (message instanceof JMSBytesMessage) {
            JMSBytesMessage bm = (JMSBytesMessage) message;
            byte[] buff;
            try {
                buff = new byte[(int) bm.getBodyLength()];
                bm.readBytes(buff);
                recvStrMsg = new String(buff, connConfSyscode.getCharSet());
                StringBuilder bStr = new StringBuilder("[ ");
                for(byte b : buff){
                    bStr.append((int) b).append(",");
                }
                String  retStr = bStr.substring(0,bStr.lastIndexOf(",")) + " ]";
                LOGGER.info("字节消息:{}",retStr);
            } catch (Exception e) {
                WARN_LOGGER.error(e.getMessage(), e);
            }
        } else {
            JMSTextMessage bm = (JMSTextMessage) message;
            try {
                recvStrMsg = bm.getText();
                LOGGER.info("文本消息");
            } catch (Exception e) {
                WARN_LOGGER.error(e.getMessage(), e);
            }
        }

        // 2.记录消息日志
        LOGGER.info(recvStrMsg);
        int headLen;
        if(headLength == 0){
            headLen = 10;
        }else{
            headLen = headLength;
        }
        String telId;
        String totalType = "RR"; // 统计接收累计标识 RR-正常接收消息 RE-异常接收消息
        if (recvStrMsg.length() < headLen) { // 判断消息长度是否>10
            telId = "LENGTH<10";
            totalType = "RE";
        } else {
            telId = recvStrMsg.substring(0, headLen);
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
            connRecvMain.setMsgId(msgId);
            connRecvMain.setTelId(telId);
            connRecvMain.setRecvTime(new Date());
            if(null != connConfSyscode){
                connRecvMain.setSender(connConfSyscode.getSysCode());
                connRecvMain.setSenderName(connConfSyscode.getSysName());
            }else{
                connRecvMain.setSenderName("未配置");
            }
            if(null != connConfSyscodeCr) {
                connRecvMain.setReceiver(connConfSyscodeCr.getSysCode());
                connRecvMain.setReceiverName(connConfSyscodeCr.getSysName());
            }else{
                connRecvMain.setReceiverName("未配置");
            }
            connRecvMainDao.insertSelective(connRecvMain);

            // 5.更新统计表
            connTotalNumDao.updateTotalNum(totalType);
        }catch (Exception e){
            WARN_LOGGER.error("MQ消息处理出现异常:",e);
        }
    }

}
