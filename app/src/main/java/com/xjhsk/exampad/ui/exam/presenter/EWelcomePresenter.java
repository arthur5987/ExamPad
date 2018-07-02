package com.xjhsk.exampad.ui.exam.presenter;

import com.xjhsk.exampad.base.RxPresenter;
import com.xjhsk.exampad.model.http.DataManager;
import com.xjhsk.exampad.ui.exam.contract.EWelcomeContract;
import com.xjhsk.exampad.ui.main.contract.ExamContract;

import javax.inject.Inject;

/**
 * 作者：weidingqiang on 2018/1/12 14:53
 * 邮箱：dqwei@iflytek.com
 */

public class EWelcomePresenter extends RxPresenter<EWelcomeContract.View> implements EWelcomeContract.Presenter {

    protected static final String TAG = EWelcomePresenter.class.getSimpleName();

    private DataManager mDataManager;

    @Inject
    public EWelcomePresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(EWelcomeContract.View view) {
        super.attachView(view);
        registerEvent();
    }


    private void registerEvent() {

    }

}
