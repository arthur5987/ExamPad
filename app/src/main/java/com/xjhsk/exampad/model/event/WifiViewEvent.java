package com.xjhsk.exampad.model.event;

/**
 * 作者：weidingqiang on 2018/1/15 21:22
 * 邮箱：dqwei@iflytek.com
 */

public class WifiViewEvent {

    public static final String SELECT_ITEM = "select_item";

    private String type;

    private String bssid;

    public WifiViewEvent(String type,String bssid){
        this.type = type;
        this.bssid = bssid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }
}
