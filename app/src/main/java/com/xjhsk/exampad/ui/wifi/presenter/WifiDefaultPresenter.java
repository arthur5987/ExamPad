package com.xjhsk.exampad.ui.wifi.presenter;

import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.base.RxBus;
import com.xjhsk.exampad.base.RxPresenter;
import com.xjhsk.exampad.model.event.SocketEvent;
import com.xjhsk.exampad.model.event.WifiViewEvent;
import com.xjhsk.exampad.model.http.DataManager;
import com.xjhsk.exampad.ui.wifi.contract.WifiDefaultContract;
import com.xjhsk.exampad.utils.CommonSubscriber;
import com.xjhsk.exampad.utils.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

/**
 * 作者：weidingqiang on 2018/1/12 14:53
 * 邮箱：dqwei@iflytek.com
 */

public class WifiDefaultPresenter extends RxPresenter<WifiDefaultContract.View> implements WifiDefaultContract.Presenter {

    protected static final String TAG = WifiDefaultContract.class.getSimpleName();

    private DataManager mDataManager;

    @Inject
    public WifiDefaultPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(WifiDefaultContract.View view) {
        super.attachView(view);
    }



}
