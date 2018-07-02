package com.xjhsk.exampad.ui.wifi.presenter;

import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.base.RxBus;
import com.xjhsk.exampad.base.RxPresenter;
import com.xjhsk.exampad.model.bean.ExamTitleVO;
import com.xjhsk.exampad.model.event.SocketEvent;
import com.xjhsk.exampad.model.event.WifiViewEvent;
import com.xjhsk.exampad.model.http.DataManager;
import com.xjhsk.exampad.model.http.response.HttpResponse;
import com.xjhsk.exampad.ui.wifi.contract.WifiListContract;
import com.xjhsk.exampad.utils.CommonSubscriber;
import com.xjhsk.exampad.utils.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

/**
 * 作者：weidingqiang on 2018/1/12 14:53
 * 邮箱：dqwei@iflytek.com
 */

public class WifiListPresenter extends RxPresenter<WifiListContract.View> implements WifiListContract.Presenter {

    protected static final String TAG = WifiListPresenter.class.getSimpleName();

    private DataManager mDataManager;

    @Inject
    public WifiListPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(WifiListContract.View view) {
        super.attachView(view);
        registerEvent();
    }


    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(WifiViewEvent.class)
                .compose(RxUtil.<WifiViewEvent>rxSchedulerHelper())
                .filter(new Predicate<WifiViewEvent>() {
                    @Override
                    public boolean test(@NonNull WifiViewEvent wifiViewEvent) throws Exception {
                        return wifiViewEvent.getType().equals(WifiViewEvent.SELECT_ITEM);
                    }
                })
                .subscribeWith(new CommonSubscriber<WifiViewEvent>(mView) {
                    @Override
                    public void onNext(WifiViewEvent s) {
                        LogUtil.debug("事件","收到点击wifi列表item的事件");
                        mView.selectWifiItem(s.getBssid());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })
        );

        //跳转到 重启
        addSubscribe(RxBus.getDefault().toFlowable(SocketEvent.class)
                .compose(RxUtil.<SocketEvent>rxSchedulerHelper())
                .filter(new Predicate<SocketEvent>() {
                    @Override
                    public boolean test(@NonNull SocketEvent socketEvent) throws Exception {
                        return socketEvent.getType().equals(SocketEvent.ReStart);
                    }
                })
                .subscribeWith(new CommonSubscriber<SocketEvent>(mView) {
                    @Override
                    public void onNext(SocketEvent s) {
                        LogUtil.debug("事件","收到重启的事件");
                        mView.reStart();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );

        //跳转到 关机
        addSubscribe(RxBus.getDefault().toFlowable(SocketEvent.class)
                .compose(RxUtil.<SocketEvent>rxSchedulerHelper())
                .filter(new Predicate<SocketEvent>() {
                    @Override
                    public boolean test(@NonNull SocketEvent socketEvent) throws Exception {
                        return socketEvent.getType().equals(SocketEvent.ShutDown);
                    }
                })
                .subscribeWith(new CommonSubscriber<SocketEvent>(mView) {
                    @Override
                    public void onNext(SocketEvent s) {
                        LogUtil.debug("事件","收到关机的事件");
                        mView.shutDown();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );
    }


}
