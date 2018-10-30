package com.suncd.conn.ibmmq.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class ConnRecvMsg implements Serializable {
    /**
     * 消息主键id,对应conn_sk_recv_main.msgId
     */
    private String id;

    /**
     * 记录创建时间
     */
    private Date createTime;

    /**
     * 接收消息大文本
     */
    private String msgTxt;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMsgTxt() {
        return msgTxt;
    }

    public void setMsgTxt(String msgTxt) {
        this.msgTxt = msgTxt;
    }
}