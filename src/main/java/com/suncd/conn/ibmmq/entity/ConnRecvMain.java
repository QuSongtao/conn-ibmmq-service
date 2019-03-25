package com.suncd.conn.ibmmq.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class ConnRecvMain implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 电文ID-20位
     */
    private String telId;

    /**
     * 消息ID,对应conn_recv_msg.id
     */
    private String msgId;

    /**
     * 接收时间
     */
    private Date recvTime;

    private String sender;
    private String senderName;
    private String receiver;
    private String receiverName;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTelId() {
        return telId;
    }

    public void setTelId(String telId) {
        this.telId = telId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Date getRecvTime() {
        return recvTime;
    }

    public void setRecvTime(Date recvTime) {
        this.recvTime = recvTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}