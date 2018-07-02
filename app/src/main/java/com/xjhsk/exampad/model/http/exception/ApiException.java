package com.xjhsk.exampad.model.http.exception;

/**
 * 作者：weidingqiang on 2017/7/10 17:20
 * 邮箱：dqwei@iflytek.com
 */
public class ApiException extends Exception{

    private int code;

    public ApiException(String msg) {
        super(msg);
    }

    public ApiException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
