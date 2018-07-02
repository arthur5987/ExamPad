package com.xjhsk.exampad.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：weidingqiang on 2018/1/18 10:34
 * 邮箱：dqwei@iflytek.com
 */

public class PaperSectionHeader implements Parcelable {

    /**
     * pagerType : QUESTION
     * questionType : 1
     * questionTitleType : AUDIO
     * questionOptionType :
     * questionOptionNum : 0
     * showType : 1
     * questionNo : 01
     * questionId : xhk100001
     */

    private String pagerType;
    private String questionType;
    private String questionTitleType;
    private String questionOptionType;
    private int questionOptionNum;
    private int showType;
    private String questionNo;
    private String questionId;

    public String getPagerType() {
        return pagerType;
    }

    public void setPagerType(String pagerType) {
        this.pagerType = pagerType;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionTitleType() {
        return questionTitleType;
    }

    public void setQuestionTitleType(String questionTitleType) {
        this.questionTitleType = questionTitleType;
    }

    public String getQuestionOptionType() {
        return questionOptionType;
    }

    public void setQuestionOptionType(String questionOptionType) {
        this.questionOptionType = questionOptionType;
    }

    public int getQuestionOptionNum() {
        return questionOptionNum;
    }

    public void setQuestionOptionNum(int questionOptionNum) {
        this.questionOptionNum = questionOptionNum;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public String getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pagerType);
        dest.writeString(this.questionType);
        dest.writeString(this.questionTitleType);
        dest.writeString(this.questionOptionType);
        dest.writeInt(this.questionOptionNum);
        dest.writeInt(this.showType);
        dest.writeString(this.questionNo);
        dest.writeString(this.questionId);
    }

    public PaperSectionHeader() {
    }

    protected PaperSectionHeader(Parcel in) {
        this.pagerType = in.readString();
        this.questionType = in.readString();
        this.questionTitleType = in.readString();
        this.questionOptionType = in.readString();
        this.questionOptionNum = in.readInt();
        this.showType = in.readInt();
        this.questionNo = in.readString();
        this.questionId = in.readString();
    }

    public static final Parcelable.Creator<PaperSectionHeader> CREATOR = new Parcelable.Creator<PaperSectionHeader>() {
        @Override
        public PaperSectionHeader createFromParcel(Parcel source) {
            return new PaperSectionHeader(source);
        }

        @Override
        public PaperSectionHeader[] newArray(int size) {
            return new PaperSectionHeader[size];
        }
    };
}
