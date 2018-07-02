package com.xjhsk.exampad.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 作者：weidingqiang on 2018/1/18 10:33
 * 邮箱：dqwei@iflytek.com
 */

public class PaperSection implements Parcelable {
    private PaperSectionHeader paperSectionHeader;

    private ArrayList<PaperAction> paperActions;

    public PaperSectionHeader getPaperSectionHeader() {
        return paperSectionHeader;
    }

    public void setPaperSectionHeader(PaperSectionHeader paperSectionHeader) {
        this.paperSectionHeader = paperSectionHeader;
    }

    public ArrayList<PaperAction> getPaperActions() {
        return paperActions;
    }

    public void setPaperActions(ArrayList<PaperAction> paperActions) {
        this.paperActions = paperActions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.paperSectionHeader, flags);
        dest.writeTypedList(this.paperActions);
    }

    public PaperSection() {
    }

    protected PaperSection(Parcel in) {
        this.paperSectionHeader = in.readParcelable(PaperSectionHeader.class.getClassLoader());
        this.paperActions = in.createTypedArrayList(PaperAction.CREATOR);
    }

    public static final Parcelable.Creator<PaperSection> CREATOR = new Parcelable.Creator<PaperSection>() {
        @Override
        public PaperSection createFromParcel(Parcel source) {
            return new PaperSection(source);
        }

        @Override
        public PaperSection[] newArray(int size) {
            return new PaperSection[size];
        }
    };
}
