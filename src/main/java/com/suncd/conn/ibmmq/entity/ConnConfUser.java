package com.suncd.conn.ibmmq.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class ConnConfUser implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 登录账号
     */
    private String loginName;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户令牌
     */
    private String accessToken;

    /**
     * 上次登录时间
     */
    private Date lastLoginTime;

    /**
     * 是否管理员
     */
    private String isAdmin;

    /**
     * 备注说明
     */
    private String des;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}