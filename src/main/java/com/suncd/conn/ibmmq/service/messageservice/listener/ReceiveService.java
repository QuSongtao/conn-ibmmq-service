package com.suncd.conn.ibmmq.service.messageservice.listener;

import com.ibm.jms.JMSBytesMessage;
import com.ibm.jms.JMSTextMessage;
import com.suncd.conn.ibmmq.dao.*;
import com.suncd.conn.ibmmq.entity.ConnConfSyscode;
import com.suncd.conn.ibmmq.entity.ConnRecvMain;
import com.suncd.conn.ibmmq.entity.ConnRecvMainHis;
import com.suncd.conn.ibmmq.entity.ConnRecvMsg;
import com.suncd.conn.ibmmq.system.constants.Constant;
import com.suncd.conn.ibmmq.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    @Autowired
    private ConnRecvMainHisDao connRecvMainHisDao;

    /**
     * 接收消息核心处理逻辑
     *
     * @param message 消息体
     * @param sysCode 系统编码
     */
    public void dealMessage(Message message, String sysCode, boolean charset, int headLength) {

        // 1.监听并读取消息
        String recvStrMsg = "";
        if (message instanceof JMSBytesMessage) {
            JMSBytesMessage bm = (JMSBytesMessage) message;
            byte[] buff;
            try {
                buff = new byte[(int) bm.getBodyLength()];
                bm.readBytes(buff);
                recvStrMsg = new String(buff);
                StringBuilder bStr = new StringBuilder("[ ");
                for (byte b : buff) {
                    bStr.append((int) b).append(",");
                }
                String retStr = bStr.substring(0, bStr.lastIndexOf(",")) + " ]";
                LOGGER.info("字节消息:{}", retStr);
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

        // 2.处理消息
        handleMsg(recvStrMsg, sysCode, headLength);
    }

    /**
     * 处理接收消息
     *
     * @param recvStrMsg 接收消息字符串
     * @param headLength 电文ID长度
     * @author qust 20190823 代码分离
     * @author qust 20190827 处理触发器执行异常的消息
     */
    private void handleMsg(String recvStrMsg, String sysCode, int headLength) {
        if (StringUtils.isEmpty(recvStrMsg)) {
            return;
        }
        // 1.记录消息日志
        LOGGER.info(recvStrMsg);

        // 2. 根据sysCode查找对应的中文名称
        ConnConfSyscode connConfSyscode = connConfSyscodeDao.selectBySysCode(sysCode);
        ConnConfSyscode connConfSyscodeCr = connConfSyscodeDao.selectBySysCode(Constant.MES_CR);

        int headLen;
        if (headLength == 0) {
            headLen = 10;
        } else {
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

        String mainId = UUID.randomUUID().toString();
        String msgId = UUID.randomUUID().toString();
        try {
            // 3.插入接收消息表
            ConnRecvMsg connRecvMsg = new ConnRecvMsg();
            connRecvMsg.setId(msgId);
            connRecvMsg.setMsgTxt(recvStrMsg);
            connRecvMsg.setCreateTime(new Date());
            connRecvMsgDao.insertSelective(connRecvMsg);
            // 记录插入接收总表成功日志
            CommonUtil.SYSLOGGER.info("插入接收总表成功,msgId={},telId={}", msgId, telId);

            // 4.插入接收总表
            ConnRecvMain connRecvMain = new ConnRecvMain();
            connRecvMain.setId(mainId);
            connRecvMain.setMsgId(msgId);
            connRecvMain.setTelId(telId);
            connRecvMain.setRecvTime(new Date());
            if (null != connConfSyscode) {
                connRecvMain.setSender(connConfSyscode.getSysCode());
                connRecvMain.setSenderName(connConfSyscode.getSysName());
            } else {
                connRecvMain.setSenderName("未配置");
            }
            if (null != connConfSyscodeCr) {
                connRecvMain.setReceiver(connConfSyscodeCr.getSysCode());
                connRecvMain.setReceiverName(connConfSyscodeCr.getSysName());
            } else {
                connRecvMain.setReceiverName("未配置");
            }
            connRecvMainDao.insertSelective(connRecvMain);

            // 5.更新统计表
            connTotalNumDao.updateTotalNum(totalType);
        } catch (UncategorizedSQLException e) {
            WARN_LOGGER.error("SQL异常(触发器可能执行失败):", e);
            // 触发器处理异常的消息,记录到接收历史表
            ConnRecvMainHis connRecvMainHis = new ConnRecvMainHis();
            connRecvMainHis.setId(mainId);
            connRecvMainHis.setDealTime(new Date());
            connRecvMainHis.setRecvTime(new Date());
            connRecvMainHis.setMsgId(msgId);
            if (null != connConfSyscode) {
                connRecvMainHis.setSender(connConfSyscode.getSysCode());
                connRecvMainHis.setSenderName(connConfSyscode.getSysName());
            } else {
                connRecvMainHis.setSenderName("未配置");
            }
            connRecvMainHis.setDes(e.getMessage());
            connRecvMainHis.setTelId(telId);
            if (null != connConfSyscodeCr) {
                connRecvMainHis.setReceiver(connConfSyscodeCr.getSysCode());
                connRecvMainHis.setReceiverName(connConfSyscodeCr.getSysName());
            } else {
                connRecvMainHis.setReceiverName("未配置");
            }
            connRecvMainHis.setDealFlag("9"); // 触发器处理异常
            connRecvMainHisDao.insertSelective(connRecvMainHis);
        } catch (Exception e){
            WARN_LOGGER.error("接收服务处理异常:", e);
        }
    }

}
