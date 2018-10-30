package com.suncd.conn.ibmmq.entity;

import java.io.Serializable;

/**
 * @author 
 */
public class ConnTotalNum implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 合计数量
     */
    private Long totalNum;

    /**
     * 类型 SS-发送正常,SE-发送失败 RR-接收成功,RE-接收失败
     */
    private String type;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}