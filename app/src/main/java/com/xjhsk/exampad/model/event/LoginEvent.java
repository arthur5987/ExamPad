package com.xjhsk.exampad.model.event;

/**
 * 作者：weidingqiang on 2018/1/16 15:34
 * 邮箱：dqwei@iflytek.com
 */

public class LoginEvent {
    //确认登陆
    public static final String LOGIN_SURE = "login_sure";
    //取消
    public static final String LOGIN_CANCEL = "login_cancel";

    private String type;

    public LoginEvent(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
