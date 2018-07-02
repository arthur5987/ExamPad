package com.xjhsk.exampad.model.event;

/**
 * 作者：weidingqiang on 2018/1/18 14:21
 * 邮箱：dqwei@iflytek.com
 */

public class CloseEvent {

    public static final String CLOSE_TEST_SOUND = "close_test_sound";

    private String type;

    public CloseEvent(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
