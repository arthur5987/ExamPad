package com.xjhsk.exampad.ui.login.presenter;

import com.blankj.utilcode.util.TimeUtils;
import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.api.Constants;
import com.xjhsk.exampad.app.AppContext;
import com.xjhsk.exampad.base.RxBus;
import com.xjhsk.exampad.base.RxPresenter;
import com.xjhsk.exampad.model.bean.CheckVerVO;
import com.xjhsk.exampad.model.bean.ExamTitleVO;
import com.xjhsk.exampad.model.event.SocketEvent;
import com.xjhsk.exampad.model.http.DataManager;
import com.xjhsk.exampad.model.http.response.HttpResponse;
import com.xjhsk.exampad.ui.login.contract.LoginContract;
import com.xjhsk.exampad.ui.login.contract.ReadyLoginContract;
import com.xjhsk.exampad.utils.CommonSubscriber;
import com.xjhsk.exampad.utils.RxUtil;
import com.xjhsk.exampad.utils.Sha1Util;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

/**
 * 作者：weidingqiang on 2018/1/12 14:53
 * 邮箱：dqwei@iflytek.com
 */

public class ReadyLoginPresenter extends RxPresenter<ReadyLoginContract.View> implements ReadyLoginContract.Presenter {

    protected static final String TAG = ReadyLoginPresenter.class.getSimpleName();

    private DataManager mDataManager;

    @Inject
    public ReadyLoginPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(ReadyLoginContract.View view) {
        super.attachView(view);
        registerEvent();
    }


    private void registerEvent() {
        //接受到登录指令
        addSubscribe(RxBus.getDefault().toFlowable(SocketEvent.class)
                .compose(RxUtil.<SocketEvent>rxSchedulerHelper())
                .filter(new Predicate<SocketEvent>() {
                    @Override
                    public boolean test(@NonNull SocketEvent socketEvent) throws Exception {
                        return socketEvent.getType().equals(SocketEvent.ConnectSuccess);
                    }
                })
                .subscribeWith(new CommonSubscriber<SocketEvent>(mView) {
                    @Override
                    public void onNext(SocketEvent s) {
                        LogUtil.debug("事件","socket连接成功指令");
                        mView.connectSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.debug("事件","#ERROR# socket收到错误的准备登陆指令");
                    }
                })
        );

        //接受到登录指令
        addSubscribe(RxBus.getDefault().toFlowable(SocketEvent.class)
                .compose(RxUtil.<SocketEvent>rxSchedulerHelper())
                .filter(new Predicate<SocketEvent>() {
                    @Override
                    public boolean test(@NonNull SocketEvent socketEvent) throws Exception {
                        return socketEvent.getType().equals(SocketEvent.AllowLogin);
                    }
                })
                .subscribeWith(new CommonSubscriber<SocketEvent>(mView) {
                    @Override
                    public void onNext(SocketEvent s) {
                        LogUtil.debug("事件","socket收到登陆指令");
                        mView.jumpLogin();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.debug(TAG,"#ERROR# 收到错误的准备登陆指令");
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
                        LogUtil.debug("事件","socket收到重启指令");
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
                        LogUtil.debug("事件","socket收到重启指令");
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
    public void checkVer(String version) {

        String time_mills = String.valueOf(TimeUtils.getNowTimeMills());
        String key = Sha1Util.getSha1(time_mills+ Constants.KEY);

        addSubscribe(mDataManager.checkVer(version, key, AppContext.getInstance().getIp(), time_mills)
                .compose(RxUtil.<HttpResponse<CheckVerVO>>rxSchedulerHelper())
                .compose(RxUtil.<CheckVerVO>handleTestResult())
                .subscribeWith(
                        new CommonSubscriber<CheckVerVO>(mView) {
                            @Override
                            public void onNext(CheckVerVO data) {
                                LogUtil.debug(TAG,"检查版本成功");
                                mView.checkVerSuccess(data);
                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtil.debug(TAG,"检查版本失败");
                                mView.responseError(e.getMessage());
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
    public void getStatus() {

        String time_mills = String.valueOf(TimeUtils.getNowTimeMills());
        String key = Sha1Util.getSha1(time_mills+ Constants.KEY);

        addSubscribe(mDataManager.getStatus(null, key, "50", time_mills)
                .compose(RxUtil.<HttpResponse<String>>rxSchedulerHelper())
                .compose(RxUtil.<String>handleTestResult())
                .subscribeWith(
                        new CommonSubscriber<String>(mView) {
                            @Override
                            public void onNext(String data) {
                                LogUtil.debug(TAG,"获取状态成功");
                                mView.jumpLogin();
                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtil.debug(TAG,"获取状态失败");
                                mView.responseError(e.getMessage());
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
    public void getExamTitle() {

        String time_mills = String.valueOf(TimeUtils.getNowTimeMills());
        String key = Sha1Util.getSha1(time_mills+ Constants.KEY);

        addSubscribe(mDataManager.getExamTitle(key, AppContext.getInstance().getIp(), time_mills)
                .compose(RxUtil.<HttpResponse<ExamTitleVO>>rxSchedulerHelper())
                .compose(RxUtil.<ExamTitleVO>handleTestResult())
                .subscribeWith(
                        new CommonSubscriber<ExamTitleVO>(mView) {
                            @Override
                            public void onNext(ExamTitleVO data) {
                                LogUtil.debug(TAG,"获取考试题目成功");
                                mView.getExamTitleSuccess(data);
                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtil.debug(TAG,"获取考试题目失败");
                                mView.responseError(e.getMessage());
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
