package com.xjhsk.exampad.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 作者：weidingqiang on 2018/1/18 10:20
 * 邮箱：dqwei@iflytek.com
 */

public class PaperVO implements Parcelable {

    private PagerInfo pagerInfo;

    private ArrayList<PaperSection> paperSections;

    public String getZipfilename() {
        return zipfilename;
    }

    public void setZipfilename(String zipfilename) {
        this.zipfilename = zipfilename;
    }

    private String zipfilename;

    public PagerInfo getPagerInfo() {
        return pagerInfo;
    }

    public void setPagerInfo(PagerInfo pagerInfo) {
        this.pagerInfo = pagerInfo;
    }

    public ArrayList<PaperSection> getPaperSections() {
        return paperSections;
    }

    public void setPaperSections(ArrayList<PaperSection> paperSections) {
        this.paperSections = paperSections;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.pagerInfo, flags);
        dest.writeTypedList(this.paperSections);
        dest.writeString(this.zipfilename);
    }

    public PaperVO() {
    }

    protected PaperVO(Parcel in) {
        this.pagerInfo = in.readParcelable(PagerInfo.class.getClassLoader());
        this.paperSections = in.createTypedArrayList(PaperSection.CREATOR);
        this.zipfilename = in.readString();
    }

    public static final Parcelable.Creator<PaperVO> CREATOR = new Parcelable.Creator<PaperVO>() {
        @Override
        public PaperVO createFromParcel(Parcel source) {
            return new PaperVO(source);
        }

        @Override
        public PaperVO[] newArray(int size) {
            return new PaperVO[size];
        }
    };
}
