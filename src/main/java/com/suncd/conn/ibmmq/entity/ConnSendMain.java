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
     * 电文ID-10位
     */
    private String telId;

    /**
     * 电文类型：SK-socket通信 ，MQ-mq通信
     */
    private String telType;

    /**
     * 消息ID
     */
    private String msgId;

    /**
     * 发送标识:0-未发送,1-已发送,默认值0
     */
    private String sendFlag;

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

    /**
     * 消息发送时间
     */
    private Date sendTime;

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

    public String getTelType() {
        return telType;
    }

    public void setTelType(String telType) {
        this.telType = telType;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(String sendFlag) {
        this.sendFlag = sendFlag;
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

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}