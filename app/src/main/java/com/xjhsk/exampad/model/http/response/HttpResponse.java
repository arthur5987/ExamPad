package com.xjhsk.exampad.model.http.response;

/**
 * 作者：weidingqiang on 2017/9/7 09:56
 * 邮箱：dqwei@iflytek.com
 */

public class HttpResponse<T> {


    /**
     * status : 1
     * msg : 获取数据成功
     * data : T
     */

    private String status;
    private String msg;
    private T data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
