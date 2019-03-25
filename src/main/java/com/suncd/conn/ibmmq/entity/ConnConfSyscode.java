package com.suncd.conn.ibmmq.entity;

import java.io.Serializable;

/**
 * @author 
 */
public class ConnConfSyscode implements Serializable {
    /**
     * 主键id
     */
    private String id;

    /**
     * 通信系统编码
     */
    private String sysCode;

    /**
     * 通信系统名称
     */
    private String sysName;

    /**
     * 通信方式
     */
    private String connType;

    /**
     * 连接属性
     */
    private String connProperties;

    /**
     * 生效标识
     */
    private String connFlag;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getConnType() {
        return connType;
    }

    public void setConnType(String connType) {
        this.connType = connType;
    }

    public String getConnProperties() {
        return connProperties;
    }

    public void setConnProperties(String connProperties) {
        this.connProperties = connProperties;
    }

    public String getConnFlag() {
        return connFlag;
    }

    public void setConnFlag(String connFlag) {
        this.connFlag = connFlag;
    }
}