package com.xjhsk.exampad.ui.testsound.presenter_stand_alone;

import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.api.Constants;
import com.xjhsk.exampad.app.AppContext;
import com.xjhsk.exampad.base.RxBus;
import com.xjhsk.exampad.base.RxPresenter;
import com.xjhsk.exampad.model.bean.PaperVO;
import com.xjhsk.exampad.model.bean.StringAnswer;
import com.xjhsk.exampad.model.event.ActionEvent;
import com.xjhsk.exampad.model.event.SocketEvent;
import com.xjhsk.exampad.model.http.DataManager;
import com.xjhsk.exampad.ui.testsound.contract.RecordContract;
import com.xjhsk.exampad.ui.testsound.presenter.RecordPresenter;
import com.xjhsk.exampad.utils.CommonSubscriber;
import com.xjhsk.exampad.utils.JsonUtils;
import com.xjhsk.exampad.utils.RxUtil;

import org.json.JSONObject;

import java.io.FileInputStream;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：weidingqiang on 2018/1/12 14:53
 * 邮箱：dqwei@iflytek.com
 */

public class RecordPresenter_SA extends RxPresenter<RecordContract.View> implements RecordContract.Presenter {

    protected static final String TAG = RecordPresenter_SA.class.getSimpleName();

    private DataManager mDataManager;

    @Inject
    public RecordPresenter_SA(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(RecordContract.View view) {
        super.attachView(view);
        registerEvent();
    }


    private void registerEvent() {
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
                        mView.startExam();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );
    }

    @Override
    public void loadZipData(final String zipfilename) {

        Observable.just(zipfilename+ Constants.PAPER_JSON)
                .map(new Function<String, JSONObject>() {
                    @Override
                    public JSONObject apply(@NonNull String s) throws Exception {
                        FileInputStream inputStream =  new FileInputStream(s);

                        String data = JsonUtils.readTextFromSDcard(inputStream);

                        JSONObject jsondata = new JSONObject(data);

                        return jsondata;
                    }
                })
                .map(new Function<JSONObject, PaperVO>() {
                    @Override
                    public PaperVO apply(JSONObject jsonObject) throws Exception {
                        PaperVO paperVO = JsonUtils.analyzeJson(jsonObject);
                        return paperVO;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Observer<PaperVO>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.debug(TAG,"开始解析");
                        addSubscribe(d);
                    }

                    @Override
                    public void onNext(@NonNull PaperVO paperVO) {
                        paperVO.setZipfilename(zipfilename);

                        // ****************** //
                        StringAnswer data = new StringAnswer();
//                        data.setExamStuNo(AppContext.getInstance().getUserVO().getExam_no());

                        String realAnswer = "";
                        String questionScore = "";
                        for (int i = 0; i < paperVO.getPaperSections().size(); i++) {
                            if (paperVO.getPaperSections().get(i).getPaperSectionHeader().getQuestionNo() != null){
                                if (i != paperVO.getPaperSections().size()-1 ){
                                    realAnswer += paperVO.getPaperSections().get(i).getPaperSectionHeader().getRealAnswer()+"#";
                                    questionScore += paperVO.getPaperSections().get(i).getPaperSectionHeader().getQuestionScore()+"#";
                                }else {
                                    realAnswer += paperVO.getPaperSections().get(i).getPaperSectionHeader().getRealAnswer();
                                    questionScore += paperVO.getPaperSections().get(i).getPaperSectionHeader().getQuestionScore();
                                }
                            }
                        }
                        data.setRealAnswer(realAnswer);
                        data.setQuestionScore(questionScore);
                        AppContext.getInstance().saveStringAnswer(data);
                        // ****************** //

                        mView.analyzeSuccess(paperVO);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.debug(TAG,"解析出错");
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.debug(TAG,"解析完成");
                    }
                });

    }

    @Override
    public void setStatus(String status) {

//        String time_mills = String.valueOf(TimeUtils.getNowTimeMills());
//        String key = Sha1Util.getSha1(time_mills+ Constants.KEY);
//
//        addSubscribe(mDataManager.setStatus(AppContext.getInstance().getUserVO().getExam_no(), status, key, time_mills)
//                        .compose(RxUtil.<HttpResponse<String>>rxSchedulerHelper())
//                        .compose(RxUtil.<String>handleTestResult())
//                        .subscribeWith(
//                                new CommonSubscriber<String>(mView) {
//                                    @Override
//                                    public void onNext(String data) {
//
//                                        mView.setStatusSuccess();
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
////                                        mView.responceError(e.getMessage());
//                                    }
//
//                                    @Override
//                                    public void onComplete() {
//                                        super.onComplete();
//                                    }
//                                }
//                        )
//        );
    }

    @Override
    public void startExam(String status) {

        mView.startExamSuccess();

//        String time_mills = String.valueOf(TimeUtils.getNowTimeMills());
//        String key = Sha1Util.getSha1(time_mills+ Constants.KEY);
//
//        addSubscribe(mDataManager.getStatus(AppContext.getInstance().getUserVO().getExam_no(), key, status, time_mills)
//                .compose(RxUtil.<HttpResponse<String>>rxSchedulerHelper())
//                .compose(RxUtil.<String>handleTestResult())
//                .subscribeWith(
//                        new CommonSubscriber<String>(mView) {
//                            @Override
//                            public void onNext(String data) {
//
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                mView.responceError(e.getMessage());
//                            }
//
//                            @Override
//                            public void onComplete() {
//                                super.onComplete();
//                            }
//                        }
//                )
//        );
    }
}
