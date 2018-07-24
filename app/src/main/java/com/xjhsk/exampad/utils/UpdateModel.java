package com.xjhsk.exampad.utils;

import java.io.Serializable;

/**
 * Created by weidingqiang on 16/4/25.
 */
public class UpdateModel implements Serializable {

//    public final static String YES = "Yes";
//    public final static String NO = "No";
//
//    /**
//     * 是否更新
//     */
//    private String update;
//    //新版本
//    private String new_version;
//    //apk 地址
//    private String apk_url;
//    //更新日志
//    private String update_log;
//    //size
//    private String target_size;
//    //是否强制更新
//    private Boolean is_must_upgrade;

    private String id; //
    private String appId; //-1：android 2：ios 3：pc
    private String StringappCode;
    private String appName;

    private String versionCode;
    private String versionCodeBefore;
    private String versionType;
    private String versionSize;
    private String downloadUrl;
    private String downloadAmount;
    private String updateTile;
    private String updateMessage;
    private String status;
    private String createTime;

    public UpdateModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getStringappCode() {
        return StringappCode;
    }

    public void setStringappCode(String stringappCode) {
        StringappCode = stringappCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionCodeBefore() {
        return versionCodeBefore;
    }

    public void setVersionCodeBefore(String versionCodeBefore) {
        this.versionCodeBefore = versionCodeBefore;
    }

    public String getVersionType() {
        return versionType;
    }

    public void setVersionType(String versionType) {
        this.versionType = versionType;
    }

    public String getVersionSize() {
        return versionSize;
    }

    public void setVersionSize(String versionSize) {
        this.versionSize = versionSize;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadAmount() {
        return downloadAmount;
    }

    public void setDownloadAmount(String downloadAmount) {
        this.downloadAmount = downloadAmount;
    }

    public String getUpdateTile() {
        return updateTile;
    }

    public void setUpdateTile(String updateTile) {
        this.updateTile = updateTile;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    //    public String getUpdate() {
//        return update;
//    }
//
//    public void setUpdate(String update) {
//        this.update = update;
//    }
//
//    public String getNew_version() {
//        return new_version;
//    }
//
//    public void setNew_version(String new_version) {
//        this.new_version = new_version;
//    }
//
//    public String getApk_url() {
//        return apk_url;
//    }
//
//    public void setApk_url(String apk_url) {
//        this.apk_url = apk_url;
//    }
//
//    public String getUpdate_log() {
//        return update_log;
//    }
//
//    public void setUpdate_log(String update_log) {
//        this.update_log = update_log;
//    }
//
//    public String getTarget_size() {
//        return target_size;
//    }
//
//    public void setTarget_size(String target_size) {
//        this.target_size = target_size;
//    }
//
//    public Boolean getIs_must_upgrade() {
//        return is_must_upgrade;
//    }
//
//    public void setIs_must_upgrade(Boolean is_must_upgrade) {
//        this.is_must_upgrade = is_must_upgrade;
//    }
}
