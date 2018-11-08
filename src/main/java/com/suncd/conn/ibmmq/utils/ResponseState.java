/*
 * 版权所有 © 成都太阳高科技有限责任公司
 * http://www.suncd.com
 */
package com.suncd.conn.ibmmq.utils;

/**
 * 功能描述:
 *
 * @author qust
 * @version 1.0 2018/4/14 21:24
 */
public enum ResponseState {
    FAIL(0, "失败"),
    SUCCESS(1, "成功");

    private int code;
    private String codeInfo;

    private ResponseState(int code, String codeInfo) {
        this.code = code;
        this.codeInfo = codeInfo;
    }

    public int getCode() {
        return this.code;
    }

    public String getCodeInfo() {
        return this.codeInfo;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public static ResponseState byCode(int code) {
        ResponseState[] states = values();

        for(int i = 0; i < states.length; ++i) {
            ResponseState state = states[i];
            if (state.getCode() == code) {
                return state;
            }
        }

        return FAIL;
    }
}
