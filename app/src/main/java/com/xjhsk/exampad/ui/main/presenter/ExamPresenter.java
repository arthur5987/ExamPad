package com.xjhsk.exampad.ui.main.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.xjhsk.exampad.api.Constants;
import com.xjhsk.exampad.app.AppContext;
import com.xjhsk.exampad.base.RxBus;
import com.xjhsk.exampad.base.RxPresenter;
import com.xjhsk.exampad.model.event.ActionEvent;
import com.xjhsk.exampad.model.http.DataManager;
import com.xjhsk.exampad.model.http.response.HttpResponse;
import com.xjhsk.exampad.ui.main.contract.ExamContract;
import com.xjhsk.exampad.ui.main.contract.MainContract;
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

public class ExamPresenter extends RxPresenter<ExamContract.View> implements ExamContract.Presenter {

    protected static final String TAG = ExamPresenter.class.getSimpleName();

    private DataManager mDataManager;

    @Inject
    public ExamPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(ExamContract.View view) {
        super.attachView(view);
        registerEvent();
    }


    private void registerEvent() {
        //下一页
        addSubscribe(RxBus.getDefault().toFlowable(ActionEvent.class)
                .compose(RxUtil.<ActionEvent>rxSchedulerHelper())
                .filter(new Predicate<ActionEvent>() {
                    @Override
                    public boolean test(@NonNull ActionEvent actionEvent) throws Exception {
                        return actionEvent.getType().equals(ActionEvent.NEXT);
                    }
                })
                .subscribeWith(new CommonSubscriber<ActionEvent>(mView) {
                    @Override
                    public void onNext(ActionEvent s) {
                        //需要判断是否有答案传过来，根据题号判断，录音的不会传题号
                        if(EmptyUtils.isNotEmpty(s.getAnswerVO())){
                            mView.saveAnswer(s.getAnswerVO());
                        }else {
                            mView.nextPage();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );

        //等待
        addSubscribe(RxBus.getDefault().toFlowable(ActionEvent.class)
                .compose(RxUtil.<ActionEvent>rxSchedulerHelper())
                .filter(new Predicate<ActionEvent>() {
                    @Override
                    public boolean test(@NonNull ActionEvent actionEvent) throws Exception {
                        return actionEvent.getType().equals(ActionEvent.WAIT);
                    }
                })
                .subscribeWith(new CommonSubscriber<ActionEvent>(mView) {
                    @Override
                    public void onNext(ActionEvent s) {
                        mView.waitSecond(s.getSecond());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );

        //录音
        addSubscribe(RxBus.getDefault().toFlowable(ActionEvent.class)
                .compose(RxUtil.<ActionEvent>rxSchedulerHelper())
                .filter(new Predicate<ActionEvent>() {
                    @Override
                    public boolean test(@NonNull ActionEvent actionEvent) throws Exception {
                        return actionEvent.getType().equals(ActionEvent.RECORD);
                    }
                })
                .subscribeWith(new CommonSubscriber<ActionEvent>(mView) {
                    @Override
                    public void onNext(ActionEvent s) {
                        mView.recordSecond(s.getSecond(),s.getQuestionNo());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );

        //进度条
        addSubscribe(RxBus.getDefault().toFlowable(ActionEvent.class)
                .compose(RxUtil.<ActionEvent>rxSchedulerHelper())
                .filter(new Predicate<ActionEvent>() {
                    @Override
                    public boolean test(@NonNull ActionEvent actionEvent) throws Exception {
                        return actionEvent.getType().equals(ActionEvent.PROGRESS_FINISH);
                    }
                })
                .subscribeWith(new CommonSubscriber<ActionEvent>(mView) {
                    @Override
                    public void onNext(ActionEvent s) {
                        mView.progessFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );
    }

    @Override
    public void setStatus(String status) {

        String time_mills = String.valueOf(TimeUtils.getNowTimeMills());
        String key = Sha1Util.getSha1(time_mills+ Constants.KEY);

        addSubscribe(mDataManager.setStatus(AppContext.getInstance().getUserVO().getExam_no(), status, key, time_mills)
                .compose(RxUtil.<HttpResponse<String>>rxSchedulerHelper())
                .compose(RxUtil.<String>handleTestResult())
                .subscribeWith(
                        new CommonSubscriber<String>(mView) {
                            @Override
                            public void onNext(String data) {

                                mView.setStatusSuccess();
                            }

                            @Override
                            public void onError(Throwable e) {
                                mView.responceError();
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
    public void saveExamStep(String examStep) {
        String time_mills = String.valueOf(TimeUtils.getNowTimeMills());
        String key = Sha1Util.getSha1(time_mills+ Constants.KEY);

        addSubscribe(mDataManager.saveExamStep(AppContext.getInstance().getUserVO().getExam_no(), examStep, key, time_mills)
                .compose(RxUtil.<HttpResponse<String>>rxSchedulerHelper())
                .compose(RxUtil.<String>handleTestResult())
                .subscribeWith(
                        new CommonSubscriber<String>(mView) {
                            @Override
                            public void onNext(String data) {

                                mView.saveExamStepSuccess();
                            }

                            @Override
                            public void onError(Throwable e) {
                                mView.responceError();
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
