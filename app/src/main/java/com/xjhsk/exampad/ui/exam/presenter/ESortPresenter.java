package com.xjhsk.exampad.ui.exam.presenter;

import com.xjhsk.exampad.base.RxBus;
import com.xjhsk.exampad.base.RxPresenter;
import com.xjhsk.exampad.model.event.ActionEvent;
import com.xjhsk.exampad.model.event.SelectEvent;
import com.xjhsk.exampad.model.http.DataManager;
import com.xjhsk.exampad.ui.exam.contract.EHintContract;
import com.xjhsk.exampad.ui.exam.contract.ESortContract;
import com.xjhsk.exampad.utils.CommonSubscriber;
import com.xjhsk.exampad.utils.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

/**
 * 作者：weidingqiang on 2018/1/12 14:53
 * 邮箱：dqwei@iflytek.com
 */

public class ESortPresenter extends RxPresenter<ESortContract.View> implements ESortContract.Presenter {

    protected static final String TAG = ESortPresenter.class.getSimpleName();

    private DataManager mDataManager;

    @Inject
    public ESortPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(ESortContract.View view) {
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

        //进度条
        addSubscribe(RxBus.getDefault().toFlowable(SelectEvent.class)
                .compose(RxUtil.<SelectEvent>rxSchedulerHelper())
                .filter(new Predicate<SelectEvent>() {
                    @Override
                    public boolean test(@NonNull SelectEvent selectEvent) throws Exception {
                        return selectEvent.getType().equals(SelectEvent.SELECT_ITEM);
                    }
                })
                .subscribeWith(new CommonSubscriber<SelectEvent>(mView) {
                    @Override
                    public void onNext(SelectEvent s) {
                        mView.selectItem(s.getPostion());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );
    }

}
