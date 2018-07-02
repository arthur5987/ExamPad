package com.xjhsk.exampad.model.bean;

import android.net.wifi.ScanResult;

/**
 * 作者：weidingqiang on 2018/1/15 20:31
 * 邮箱：dqwei@iflytek.com
 */

public class WifiVO {

    private ScanResult scanResult;

    private Boolean isSelect = false;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private int level;

    public ScanResult getScanResult() {
        return scanResult;
    }

    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }
}
