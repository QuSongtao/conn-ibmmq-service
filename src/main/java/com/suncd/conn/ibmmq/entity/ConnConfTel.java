package com.suncd.conn.ibmmq.entity;

import java.io.Serializable;

/**
 * @author 
 */
public class ConnConfTel implements Serializable {
    /**
     * 主键id
     */
    private String id;

    /**
     * 电文ID
     */
    private String telId;

    /**
     * 电文名称或描述
     */
    private String telName;

    /**
     * 收发类型: S-发送 R-接收
     */
    private String transferType;

    /**
     * 【收发类型=S时配置生效】队列名称
     */
    private String queueName;

    /**
     * 远程系统名称
     */
    private String remoteSystem;

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

    public String getTelName() {
        return telName;
    }

    public void setTelName(String telName) {
        this.telName = telName;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getRemoteSystem() {
        return remoteSystem;
    }

    public void setRemoteSystem(String remoteSystem) {
        this.remoteSystem = remoteSystem;
    }
}