package com.xjhsk.exampad.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 作者：weidingqiang on 2018/1/18 10:35
 * 邮箱：dqwei@iflytek.com
 */

public class PaperAction implements Parcelable {

    public static final String QUESTION_AUDIO = "QUESTION_AUDIO";

    public static final String WAIT  = "WAIT";

    public static final String QUESTION_OPTION = "QUESTION_OPTION";

    public static final String TXT = "TXT";

    public static final String TITLE = "TITLE";

    public static final String TITLE1 = "TITLE1";

    public static final String TITLE2 = "TITLE2";

    public static final String TITLE3 = "TITLE3";

    public static final String QUESTION_TITLE = "QUESTION_TITLE";

    public static final String QUESTION_IMG = "QUESTION_IMG";

    public static final String QUESTION_TXT = "QUESTION_TXT";

    public static final String RECORD = "RECORD";

    public static final String AUDIO = "AUDIO";

    private String actionType;

    private String actionAudioPath;

    private String actionWaitSecond;

    private String actionText;

    private String actionRecordSecond;

    public String getActionImgPath() {
        return actionImgPath;
    }

    public void setActionImgPath(String actionImgPath) {
        this.actionImgPath = actionImgPath;
    }

    private String actionImgPath;

    private ArrayList<PaperActionOption> paperActionOptions;

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionAudioPath() {
        return actionAudioPath;
    }

    public void setActionAudioPath(String actionAudioPath) {
        this.actionAudioPath = actionAudioPath;
    }

    public String getActionWaitSecond() {
        return actionWaitSecond;
    }

    public void setActionWaitSecond(String actionWaitSecond) {
        this.actionWaitSecond = actionWaitSecond;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public String getActionRecordSecond() {
        return actionRecordSecond;
    }

    public void setActionRecordSecond(String actionRecordSecond) {
        this.actionRecordSecond = actionRecordSecond;
    }

    public ArrayList<PaperActionOption> getPaperActionOptions() {
        return paperActionOptions;
    }

    public void setPaperActionOptions(ArrayList<PaperActionOption> paperActionOptions) {
        this.paperActionOptions = paperActionOptions;
    }

    public PaperAction() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.actionType);
        dest.writeString(this.actionAudioPath);
        dest.writeString(this.actionWaitSecond);
        dest.writeString(this.actionText);
        dest.writeString(this.actionRecordSecond);
        dest.writeString(this.actionImgPath);
        dest.writeTypedList(this.paperActionOptions);
    }

    protected PaperAction(Parcel in) {
        this.actionType = in.readString();
        this.actionAudioPath = in.readString();
        this.actionWaitSecond = in.readString();
        this.actionText = in.readString();
        this.actionRecordSecond = in.readString();
        this.actionImgPath = in.readString();
        this.paperActionOptions = in.createTypedArrayList(PaperActionOption.CREATOR);
    }

    public static final Creator<PaperAction> CREATOR = new Creator<PaperAction>() {
        @Override
        public PaperAction createFromParcel(Parcel source) {
            return new PaperAction(source);
        }

        @Override
        public PaperAction[] newArray(int size) {
            return new PaperAction[size];
        }
    };
}
