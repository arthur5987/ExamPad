package com.xjhsk.exampad.ui.testsound.presenter;

import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.base.RxBus;
import com.xjhsk.exampad.base.RxPresenter;
import com.xjhsk.exampad.model.event.CloseEvent;
import com.xjhsk.exampad.model.event.SocketEvent;
import com.xjhsk.exampad.model.event.WifiViewEvent;
import com.xjhsk.exampad.model.http.DataManager;
import com.xjhsk.exampad.ui.login.contract.LoginContract;
import com.xjhsk.exampad.ui.testsound.contract.TestSoundContract;
import com.xjhsk.exampad.utils.CommonSubscriber;
import com.xjhsk.exampad.utils.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

/**
 * 作者：weidingqiang on 2018/1/12 14:53
 * 邮箱：dqwei@iflytek.com
 */

public class TestSoundPresenter extends RxPresenter<TestSoundContract.View> implements TestSoundContract.Presenter {

    protected static final String TAG = TestSoundPresenter.class.getSimpleName();

    private DataManager mDataManager;

    @Inject
    public TestSoundPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(TestSoundContract.View view) {
        super.attachView(view);
        registerEvent();
    }


    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(CloseEvent.class)
                .compose(RxUtil.<CloseEvent>rxSchedulerHelper())
                .filter(new Predicate<CloseEvent>() {
                    @Override
                    public boolean test(@NonNull CloseEvent closeEvent) throws Exception {
                        return closeEvent.getType().equals(CloseEvent.CLOSE_TEST_SOUND);
                    }
                })
                .subscribeWith(new CommonSubscriber<CloseEvent>(mView) {
                    @Override
                    public void onNext(CloseEvent s) {
                        LogUtil.debug("事件","接受到关闭试音界面");
                        mView.closeSoundActivity();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })
        );

        //跳转到 登陆页面
        addSubscribe(RxBus.getDefault().toFlowable(SocketEvent.class)
                .compose(RxUtil.<SocketEvent>rxSchedulerHelper())
                .filter(new Predicate<SocketEvent>() {
                    @Override
                    public boolean test(@NonNull SocketEvent socketEvent) throws Exception {
                        return socketEvent.getType().equals(SocketEvent.Logout);
                    }
                })
                .subscribeWith(new CommonSubscriber<SocketEvent>(mView) {
                    @Override
                    public void onNext(SocketEvent s) {
                        LogUtil.debug("事件","接受到退出登录事件");
                        mView.JumpLogin();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
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
                        LogUtil.debug("事件","接受到重启事件");
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
                        LogUtil.debug("事件","接受到关机事件");
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
