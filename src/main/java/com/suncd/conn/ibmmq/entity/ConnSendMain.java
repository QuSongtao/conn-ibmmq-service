package com.suncd.conn.ibmmq.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class ConnSendMain implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 电文ID-20位
     */
    private String telId;

    private String sender;
    private String senderName;
    private String receiver;
    private String receiverName;

    /**
     * 消息ID
     */
    private String msgId;

    /**
     * 记录创建时间
     */
    private Date createTime;

    /**
     * 【socket通信使用】推送时间戳
     */
    private Integer pushLongTime;

    /**
     * 【socket通信使用】推送序号-5位循环
     */
    private Integer pushSeqNo;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getPushLongTime() {
        return pushLongTime;
    }

    public void setPushLongTime(Integer pushLongTime) {
        this.pushLongTime = pushLongTime;
    }

    public Integer getPushSeqNo() {
        return pushSeqNo;
    }

    public void setPushSeqNo(Integer pushSeqNo) {
        this.pushSeqNo = pushSeqNo;
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