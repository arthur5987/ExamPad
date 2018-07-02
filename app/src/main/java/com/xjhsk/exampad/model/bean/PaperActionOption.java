package com.xjhsk.exampad.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：weidingqiang on 2018/1/18 10:38
 * 邮箱：dqwei@iflytek.com
 */

public class PaperActionOption implements Parcelable {

    public static final String IMG = "IMG";

    public static final String TXT = "TXT";

    /**
     * content : 看
     * key : A
     * optionFormat : TXT
     */

    private String content;
    private String key;
    private String optionFormat;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    private boolean isSelect;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOptionFormat() {
        return optionFormat;
    }

    public void setOptionFormat(String optionFormat) {
        this.optionFormat = optionFormat;
    }

    public PaperActionOption() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.key);
        dest.writeString(this.optionFormat);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }

    protected PaperActionOption(Parcel in) {
        this.content = in.readString();
        this.key = in.readString();
        this.optionFormat = in.readString();
        this.isSelect = in.readByte() != 0;
    }

    public static final Creator<PaperActionOption> CREATOR = new Creator<PaperActionOption>() {
        @Override
        public PaperActionOption createFromParcel(Parcel source) {
            return new PaperActionOption(source);
        }

        @Override
        public PaperActionOption[] newArray(int size) {
            return new PaperActionOption[size];
        }
    };
}
