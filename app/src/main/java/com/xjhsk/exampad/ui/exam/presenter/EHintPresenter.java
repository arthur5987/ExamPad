package com.xjhsk.exampad.ui.exam.presenter;

import com.xjhsk.exampad.base.RxPresenter;
import com.xjhsk.exampad.model.http.DataManager;
import com.xjhsk.exampad.ui.exam.contract.EHintContract;
import com.xjhsk.exampad.ui.exam.contract.EWelcomeContract;

import javax.inject.Inject;

/**
 * 作者：weidingqiang on 2018/1/12 14:53
 * 邮箱：dqwei@iflytek.com
 */

public class EHintPresenter extends RxPresenter<EHintContract.View> implements EHintContract.Presenter {

    protected static final String TAG = EHintPresenter.class.getSimpleName();

    private DataManager mDataManager;

    @Inject
    public EHintPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(EHintContract.View view) {
        super.attachView(view);
        registerEvent();
    }


    private void registerEvent() {

    }

}
