package com.xjhsk.exampad.model.event;

/**
 * 作者：weidingqiang on 2018/1/23 16:57
 * 邮箱：dqwei@iflytek.com
 */

public class SelectEvent {

    public static final String SELECT_ITEM = "select_item";

    private String type;

    private int postion;

    public SelectEvent(String type,int postion){
        this.type = type;
        this.postion = postion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }
}
