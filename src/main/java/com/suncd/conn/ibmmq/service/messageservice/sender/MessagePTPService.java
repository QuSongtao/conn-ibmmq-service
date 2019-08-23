package com.suncd.conn.ibmmq.service.messageservice.sender;

import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;
import com.suncd.conn.ibmmq.utils.CommonUtil;
import com.suncd.conn.ibmmq.utils.QmgrFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class MessagePTPService {
    @Autowired
    private QmgrFactory qmgrFactory;

    private MQQueueManager mqQueueManager;

    public String recvMsg(String qname) {
        // 1.如果队列管理器为空,则从工厂进行创建
        if (null == this.mqQueueManager) {
            this.mqQueueManager = qmgrFactory.createMqQueueManager();
        }
        String msgString = null;
        MQQueue queue = null;
        try {
            MQEnvironment.properties.put(CMQC.TRANSPORT_PROPERTY, CMQC.TRANSPORT_MQSERIES);
            MQEnvironment.CCSID = 1386;
            int openOptions = CMQC.MQOO_INPUT_SHARED | CMQC.MQOO_FAIL_IF_QUIESCING | CMQC.MQOO_INQUIRE;
            /* 打开队列 */
            queue = mqQueueManager.accessQueue(qname, openOptions, null, null, null);
            while (queue.getCurrentDepth() > 0) {
                /* 设置放置消息选项 */
                MQGetMessageOptions gmo = new MQGetMessageOptions();
                /* 在同步点控制下获取消息 */
                gmo.options = gmo.options + CMQC.MQGMO_NO_SYNCPOINT;
                /* 如果在队列上没有消息则等待 */
                gmo.options = gmo.options + CMQC.MQGMO_NO_WAIT;
                /* 如果队列管理器停顿则失败 */
                gmo.options = gmo.options + CMQC.MQGMO_FAIL_IF_QUIESCING;
                /* 设置等待的时间限制 */
//                gmo.waitInterval = 3000;
                /* 创建MQMessage 类 */
                MQMessage inMsg = new MQMessage();
                inMsg.characterSet = 1386;
                inMsg.format = CMQC.MQFMT_STRING;
                /* 从队列到消息缓冲区获取消息 */
                queue.get(inMsg, gmo);

                /* 从消息读取用户数据, 读取单条消息的全部长度 */
                byte[] bStr = new byte[inMsg.getDataLength()];
                inMsg.readFully(bStr);
                msgString = new String(bStr, StandardCharsets.UTF_8);
            }
            /* 提交事务 */
            mqQueueManager.commit();
        } catch (MQException ex) {
            handleMgr();
        } catch (IOException e) {
            CommonUtil.SYSLOGGER.error(e.getMessage());
        } finally {
            try {
                /* 关闭队列和队列管理器对象 */
                queue.close();
            } catch (Exception ex) {
                CommonUtil.SYSLOGGER.error(ex.getMessage());
            }
        }
        return msgString;
    }

    /**
     * PTP模式消息发送
     *
     * @param destinationName 队列名称
     * @param msgBuf          消息字符串
     * @param result          发送结果
     */
    public void sendMessagePTP(String destinationName, String msgBuf, Map<String, Object> result) {
        // 1.如果队列管理器为空,则从工厂进行创建
        if (null == this.mqQueueManager) {
            this.mqQueueManager = qmgrFactory.createMqQueueManager();
        }
        MQQueue queue = null;
        try {
            /* 设置打开选项以便打开用于输出的队列，如果队列管理器停止，我们也已设置了选项去应对不成功情况 */
            int openOptions = CMQC.MQOO_OUTPUT | CMQC.MQOO_FAIL_IF_QUIESCING;
            /* 打开队列 */
            queue = mqQueueManager.accessQueue(destinationName, openOptions, null, null, null);
            /* 设置放置消息选项，我们将使用默认设置 */
            MQPutMessageOptions pmo = new MQPutMessageOptions();
            pmo.options = pmo.options + CMQC.MQPMO_NEW_MSG_ID;
            pmo.options = pmo.options + CMQC.MQPMO_SYNCPOINT;
            /* 创建消息缓冲区 */
            MQMessage outMsg = new MQMessage();
            /* 设置MQMD 格式字段 */
            outMsg.format = CMQC.MQFMT_STRING;
            //outMsg.messageFlags = MQC.MQMT_REQUEST;
            outMsg.characterSet = 1386;
            /* 准备用户数据消息 */
//            String msgString = msg;
            //转成字节流传输
//            byte[] b = msgString.getBytes("GBK");
            outMsg.write(msgBuf.getBytes("GBK"));
            /* 在队列上放置消息 */
            queue.put(outMsg, pmo);
            /* 提交事务 */
            mqQueueManager.commit();
            result.put("sendResult", "发送成功!");
            result.put("sendFlag", "1");
            result.put("totalType", "SS");
        } catch (Exception e) {
            handleMgr();
            CommonUtil.SYSLOGGER.error(e.getMessage(), e);
            result.put("sendResult", "发送失败!" + e.getMessage());
            result.put("sendFlag", "0");
            result.put("totalType", "SE");
        } finally {
            try {
                queue.close();
            } catch (Exception e) {
                CommonUtil.SYSLOGGER.error(e.getMessage(), e);
            }
        }
    }

    private void handleMgr(){
        // 异常则关闭队列管理器
        try {
            mqQueueManager.close();
        } catch (Exception eq) {
            CommonUtil.SYSLOGGER.error(eq.getMessage(), eq);
        }
        try {
            mqQueueManager.disconnect();
        } catch (Exception eq) {
            CommonUtil.SYSLOGGER.error(eq.getMessage(), eq);
        }
        // 置空队列管理器,下一次发送的新创建队列管理器连接
        this.mqQueueManager = null;
    }

//    public static void main(String[] args) {
//        MessagePTPService rm = new MessagePTPService();
//        //String QName  = "ERP.SMN.Q.1";//args[0].substring(8,55).trim();
//        //String QMName  = "SMN.QM";//args[0].substring(args[0].length() - 60).trim();
//        String QName  = args[0].substring(8,55).trim();
//        String QMName  = args[0].substring(args[0].length() - 60).trim();
//        //rm.recvMsg("SMN.QM",QName);
//        rm.recvMsg(QMName,QName);
//    }

}
