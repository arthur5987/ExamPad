package com.xjhsk.exampad.model.event;

/**
 * 作者：weidingqiang on 2018/1/17 10:42
 * 邮箱：dqwei@iflytek.com
 */

public class VolumeEvent {

    public static final String KEYCODE_VOLUME = "keycode_volume";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public VolumeEvent(String type){
        this.type =type;
    }

}
