package com.suncd.conn.ibmmq.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class ConnRecvMainHis implements Serializable {
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
     * 消息ID,对应conn_recv_msg.id
     */
    private String msgId;

    /**
     * 接收时间
     */
    private Date recvTime;

    /**
     * 业务处理标识:0-未处理,1-已处理
     */
    private String dealFlag;

    /**
     * 业务处理时间
     */
    private Date dealTime;

    /**
     * 业务处理备注
     */
    private String des;

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

    public Date getRecvTime() {
        return recvTime;
    }

    public void setRecvTime(Date recvTime) {
        this.recvTime = recvTime;
    }

    public String getDealFlag() {
        return dealFlag;
    }

    public void setDealFlag(String dealFlag) {
        this.dealFlag = dealFlag;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}