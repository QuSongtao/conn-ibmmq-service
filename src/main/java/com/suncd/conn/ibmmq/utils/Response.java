/*
 * 版权所有 © 成都太阳高科技有限责任公司
 * http://www.suncd.com
 */
package com.suncd.conn.ibmmq.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

/**
 * 功能描述:
 *
 * @author qust
 * @version 1.0 2018/4/14 21:21
 */
public class Response<T> {
    private Response.Meta meta;
    private T data;

    public Boolean checkState() {
        return this.meta.getCode() == ResponseState.SUCCESS.getCode();
    }

    public Response success() {
        this.meta = new Response.Meta(ResponseState.SUCCESS);
        return this;
    }

    public Response success(T data) {
        this.success();
        this.data = data;
        return this;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.getMeta().getCode() == ResponseState.SUCCESS.getCode();
    }

    public Response failure() {
        this.meta = new Response.Meta(ResponseState.FAIL);
        return this;
    }

    public Response failure(String message) {
        this.meta = new Response.Meta(ResponseState.FAIL, message);
        return this;
    }

    public Response failure(T data, String message) {
        this.failure(message);
        this.data = data;
        return this;
    }

    public Response.Meta getMeta() {
        return this.meta;
    }

    public T getData() {
        return this.data;
    }

    public Response setData(T data) {
        this.data = data;
        return this;
    }

    public static class Meta {
        @Expose
        private ResponseState state;
        private int code;
        private String message;

        public Meta() {
        }

        public Meta(ResponseState responseState) {
            this.setCode(responseState.getCode());
            this.message = responseState.getCodeInfo();
        }

        public Meta(ResponseState responseState, String message) {
            this.state = responseState;
            String msg = message;
            if (msg == null) {
                msg = responseState.getCodeInfo();
            }

            this.message = msg;
        }

        public int getCode() {
            return this.code;
        }

        public void setCode(int code) {
            this.code = code;
            this.state = ResponseState.byCode(code);
        }

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
