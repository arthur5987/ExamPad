package com.xjhsk.exampad.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 作者：weidingqiang on 2018/1/18 10:32
 * 邮箱：dqwei@iflytek.com
 */

public class PagerInfo implements Parcelable {

    /**
     * pagerName : 新疆汉语考试一级试卷
     * pagerLevel : 1
     * pagerNo : xhk_1_001
     * questionNum : 60
     */

    private String pagerName;
    private int pagerLevel;
    private String pagerNo;
    private String questionNum;

    public ArrayList<String> getPagerStep() {
        return pagerStep;
    }

    public void setPagerStep(ArrayList<String> pagerStep) {
        this.pagerStep = pagerStep;
    }

    private ArrayList<String> pagerStep;

    public String getPagerName() {
        return pagerName;
    }

    public void setPagerName(String pagerName) {
        this.pagerName = pagerName;
    }

    public int getPagerLevel() {
        return pagerLevel;
    }

    public void setPagerLevel(int pagerLevel) {
        this.pagerLevel = pagerLevel;
    }

    public String getPagerNo() {
        return pagerNo;
    }

    public void setPagerNo(String pagerNo) {
        this.pagerNo = pagerNo;
    }

    public String getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(String questionNum) {
        this.questionNum = questionNum;
    }

    public PagerInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pagerName);
        dest.writeInt(this.pagerLevel);
        dest.writeString(this.pagerNo);
        dest.writeString(this.questionNum);
        dest.writeStringList(this.pagerStep);
    }

    protected PagerInfo(Parcel in) {
        this.pagerName = in.readString();
        this.pagerLevel = in.readInt();
        this.pagerNo = in.readString();
        this.questionNum = in.readString();
        this.pagerStep = in.createStringArrayList();
    }

    public static final Creator<PagerInfo> CREATOR = new Creator<PagerInfo>() {
        @Override
        public PagerInfo createFromParcel(Parcel source) {
            return new PagerInfo(source);
        }

        @Override
        public PagerInfo[] newArray(int size) {
            return new PagerInfo[size];
        }
    };
}
