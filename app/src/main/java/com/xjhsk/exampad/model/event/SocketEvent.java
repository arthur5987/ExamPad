package com.xjhsk.exampad.model.event;

/**
 * 作者：weidingqiang on 2018/1/13 09:13
 * 邮箱：dqwei@iflytek.com
 */

/**
 * socket 通信中需要发送的事件
 */
public class SocketEvent {

    //socket连接成功
    public static final String ConnectSuccess = "ConnectSuccess";
    //允许登陆
    public static final String AllowLogin = "AllowLogin";
    //开始考试
    public static final String StartExam = "StartExam";
    //考试结束
    public static final String ExamEnd = "ExamEnd";

    //考试结束跳转到 准备登陆界面
    public static final String ReadyLogin = "ReadyLogin";
    //下线考生
    public static final String Logout = "Logout";
    //重启
    public static final String ReStart = "ReStart";
    //关机
    public static final String ShutDown = "ShutDown";


    private String type;

    public SocketEvent(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
