package com.xjhsk.exampad.ui.login_stand_alone.presenter;

import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.app.AppContext;
import com.xjhsk.exampad.base.RxBus;
import com.xjhsk.exampad.base.RxPresenter;
import com.xjhsk.exampad.model.bean.UserData;
import com.xjhsk.exampad.model.event.LoginEvent;
import com.xjhsk.exampad.model.event.SocketEvent;
import com.xjhsk.exampad.model.http.DataManager;
import com.xjhsk.exampad.model.http.response.HttpResponse;
import com.xjhsk.exampad.ui.login_stand_alone.contract.LoginContract_SA;
import com.xjhsk.exampad.utils.CommonSubscriber;
import com.xjhsk.exampad.utils.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

/**
 * 作者：weidingqiang on 2018/1/12 14:53
 * 邮箱：dqwei@iflytek.com
 */

public class LoginPresenter_SA extends RxPresenter<LoginContract_SA.View> implements LoginContract_SA.Presenter {

    protected static final String TAG = LoginPresenter_SA.class.getSimpleName();

    private DataManager mDataManager;

    @Inject
    public LoginPresenter_SA(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(LoginContract_SA.View view) {
        super.attachView(view);
        registerEvent();
    }


    private void registerEvent() {
        //确认登陆
        addSubscribe(RxBus.getDefault().toFlowable(LoginEvent.class)
                .compose(RxUtil.<LoginEvent>rxSchedulerHelper())
                .filter(new Predicate<LoginEvent>() {
                    @Override
                    public boolean test(@NonNull LoginEvent loginEvent) throws Exception {
                        return loginEvent.getType().equals(LoginEvent.LOGIN_SURE);
                    }
                })
                .subscribeWith(new CommonSubscriber<LoginEvent>(mView) {
                    @Override
                    public void onNext(LoginEvent s) {
                        LogUtil.debug("事件","收到确认登陆事件");
                        mView.loginSure();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })
        );

        //取消登陆
        addSubscribe(RxBus.getDefault().toFlowable(LoginEvent.class)
                .compose(RxUtil.<LoginEvent>rxSchedulerHelper())
                .filter(new Predicate<LoginEvent>() {
                    @Override
                    public boolean test(@NonNull LoginEvent loginEvent) throws Exception {
                        return loginEvent.getType().equals(LoginEvent.LOGIN_CANCEL);
                    }
                })
                .subscribeWith(new CommonSubscriber<LoginEvent>(mView) {
                    @Override
                    public void onNext(LoginEvent s) {
                        LogUtil.debug("事件","收到取消登陆事件");
                        mView.loginCancel();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })
        );

        //开始考试
        addSubscribe(RxBus.getDefault().toFlowable(SocketEvent.class)
                .compose(RxUtil.<SocketEvent>rxSchedulerHelper())
                .filter(new Predicate<SocketEvent>() {
                    @Override
                    public boolean test(@NonNull SocketEvent socketEvent) throws Exception {
                        return socketEvent.getType().equals(SocketEvent.StartExam);
                    }
                })
                .subscribeWith(new CommonSubscriber<SocketEvent>(mView) {
                    @Override
                    public void onNext(SocketEvent s) {
                        mView.goReadyLogin();
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
                        LogUtil.debug("事件","收到重启事件");
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
                        LogUtil.debug("事件","收到关机事件");
                        mView.shutDown();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );
    }

    @Override
    public void login(String examNo, String examIp, String key, String timestamp) {
        addSubscribe(mDataManager.login(examNo, examIp, key, timestamp)
                .compose(RxUtil.<HttpResponse<UserData>>rxSchedulerHelper())
                .compose(RxUtil.<UserData>handleTestResult())
                .subscribeWith(
                        new CommonSubscriber<UserData>(mView) {
                            @Override
                            public void onNext(UserData data) {
                                LogUtil.debug(TAG,"获取用户信息成功");
                                AppContext.getInstance().saveUserInfo(data);
                                mView.loginSuccess(data);
                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtil.debug(TAG,"获取用户信息失败");
                                mView.loginError(e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                super.onComplete();
                            }
                        }
                )
        );
    }

    @Override
    public void logout(String examNo, String examIp, String key, String timestamp) {
        addSubscribe(mDataManager.logout(examNo, examIp, key, timestamp)
                .compose(RxUtil.<HttpResponse<String>>rxSchedulerHelper())
                .compose(RxUtil.<String>handleTestResult())
                .subscribeWith(
                        new CommonSubscriber<String>(mView) {
                            @Override
                            public void onNext(String data) {
                                LogUtil.debug(TAG,"退出登录");
                                mView.logoutSuccess();
                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtil.debug(TAG,"退出登录失败");
                                mView.logoutError(e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                super.onComplete();
                            }
                        }
                )
        );
    }
}
