package com.xjhsk.exampad.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：weidingqiang on 2018/1/21 22:29
 * 邮箱：dqwei@iflytek.com
 */

public class CheckVerVO implements Parcelable {

    /**
     * version : 1.0
     * down_path : /usr/exam.apk
     */

    private String version;
    private String down_path;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDown_path() {
        return down_path;
    }

    public void setDown_path(String down_path) {
        this.down_path = down_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.version);
        dest.writeString(this.down_path);
    }

    public CheckVerVO() {
    }

    protected CheckVerVO(Parcel in) {
        this.version = in.readString();
        this.down_path = in.readString();
    }

    public static final Parcelable.Creator<CheckVerVO> CREATOR = new Parcelable.Creator<CheckVerVO>() {
        @Override
        public CheckVerVO createFromParcel(Parcel source) {
            return new CheckVerVO(source);
        }

        @Override
        public CheckVerVO[] newArray(int size) {
            return new CheckVerVO[size];
        }
    };
}
