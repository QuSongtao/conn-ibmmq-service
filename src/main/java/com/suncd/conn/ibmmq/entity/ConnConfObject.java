package com.suncd.conn.ibmmq.entity;

import java.io.Serializable;

/**
 * @author
 */
public class ConnConfObject implements Serializable {
    /**
     * 主键id
     */
    private String id;

    /**
     * 对象名称
     */
    private String objName;

    /**
     * 对象描述
     */
    private String objDes;

    /**
     * 对象类型:CHANNEL-通道,QUEUE-队列
     */
    private String objType;

    /**
     * 收发类型: S-发送 R-接收
     */
    private String transferType;

    /**
     * 远程系统名称
     */
    private String remoteSystem;

    private String objStatus;

    public String getObjStatus() {
        return objStatus;
    }

    public void setObjStatus(String objStatus) {
        this.objStatus = objStatus;
    }

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getObjDes() {
        return objDes;
    }

    public void setObjDes(String objDes) {
        this.objDes = objDes;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getRemoteSystem() {
        return remoteSystem;
    }

    public void setRemoteSystem(String remoteSystem) {
        this.remoteSystem = remoteSystem;
    }
}