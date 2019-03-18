package com.suncd.conn.ibmmq.service.messageservice.sender;

import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;
import org.springframework.stereotype.Service;

@Service
public class MessagePTPService {
    public static String recvMsg(String qmName, String qname) {
        String msgString = null;
        boolean enabled = true;
        MQQueueManager qMgr = null;
        MQQueue queue = null;
        try {
            String qManager = qmName;
            String qName = qname;
            MQEnvironment.properties.put(CMQC.TRANSPORT_PROPERTY, CMQC.TRANSPORT_MQSERIES);
            /* 连接到队列管理器 */
            qMgr = new MQQueueManager(qManager);
            MQEnvironment.CCSID = 1381;
            int openOptions = CMQC.MQOO_INPUT_SHARED
                    | CMQC.MQOO_FAIL_IF_QUIESCING | CMQC.MQOO_INQUIRE;
			/* 打开队列 */
            queue = qMgr.accessQueue(qName, openOptions, null, null, null);
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
                inMsg.characterSet = 1381;
                inMsg.format = CMQC.MQFMT_STRING;
			/* 从队列到消息缓冲区获取消息 */
                queue.get(inMsg, gmo);
			/* 从消息读取用户数据 */
                msgString = "";
                String strBuff = "";

			/* 读取单条消息的全部长度
			*/
//                inMsg.readUTF()
                if (inMsg != null) {
                    byte[] bStr = new byte[inMsg.getDataLength()];
                    inMsg.readFully(bStr);
                    msgString = new String(bStr);
                    System.out.println("msg:" + msgString);
                }
            }
			/* 提交事务 */
            qMgr.commit();
        } catch (MQException ex) {

        } catch (Exception e) {

        } finally {
            try {
				/* 关闭队列和队列管理器对象 */
                queue.close();
                qMgr.disconnect();
            } catch (Exception ex) {

            }
        }
        return msgString;
    }

    public static String[] sendMessage(String qmName,String qName,String msg){
        String[] ret = new String[2];
        MQQueueManager qMgr = null;
        MQQueue queue = null;
        try {
//			String hostName = "localhost";
//			String channel = "TEST1.TEST2.CH";
            String qManager = qmName;
            String requestQueue = qName;
//			String replyToQueue = "TEST2.Q";
//			String replyToQueueManager = "TEST1";

			/* 设置MQEnvironment 属性以便客户机连接 */
//			MQEnvironment.hostname = hostName;
//			MQEnvironment.channel = channel;
//			MQEnvironment.port = 1415;
            MQEnvironment.CCSID = 1386;
//			MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY,MQC.TRANSPORT_MQSERIES);
			/* 连接到队列管理器 */
            qMgr = new MQQueueManager(qManager);
			/* 设置打开选项以便打开用于输出的队列，如果队列管理器停止，我们也已设置了选项去应对不成功情况 */
            int openOptions = CMQC.MQOO_OUTPUT | CMQC.MQOO_FAIL_IF_QUIESCING;
			/* 打开队列 */
            queue = qMgr.accessQueue(requestQueue, openOptions, null, null, null);
			/* 设置放置消息选项，我们将使用默认设置 */
            MQPutMessageOptions pmo = new MQPutMessageOptions();
            pmo.options = pmo.options + CMQC.MQPMO_NEW_MSG_ID;
            pmo.options = pmo.options + CMQC.MQPMO_SYNCPOINT;
			/* 创建消息缓冲区 */
            MQMessage outMsg = new MQMessage();
			/* 设置MQMD 格式字段 */
            outMsg.format = CMQC.MQFMT_STRING;
            //outMsg.messageFlags = MQC.MQMT_REQUEST;
            outMsg.characterSet=1386;
			/* 准备用户数据消息 */
            String msgString = msg;
            //转成字节流传输
            byte[] b = msgString.getBytes("GBK");
//            System.out.println("ByteLength="+b.length);
            outMsg.write(b);
			/* 在队列上放置消息 */
            queue.put(outMsg, pmo);
			/* 提交事务 */
            qMgr.commit();
            ret[0]="1";
            ret[1]="发送成功!";
        } catch (Exception e) {
            e.printStackTrace();
            ret[0]="0";
            ret[1]="[X]发送失败!失败原因为:"+e.getMessage()+",具体原因请查阅日志:";
        }finally{
            try{
				/* 关闭请求队列 */
                queue.close();
				/* 断开队列管理器 */
                qMgr.disconnect();
            }
            catch(Exception ex){
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        MessagePTPService rm = new MessagePTPService();
        //String QName  = "ERP.SMN.Q.1";//args[0].substring(8,55).trim();
        //String QMName  = "SMN.QM";//args[0].substring(args[0].length() - 60).trim();
        String QName  = args[0].substring(8,55).trim();
        String QMName  = args[0].substring(args[0].length() - 60).trim();
        //rm.recvMsg("SMN.QM",QName);
        rm.recvMsg(QMName,QName);
    }

}
