package com.xjhsk.exampad.ui.testsound.presenter;

import com.xjhsk.exampad.base.RxBus;
import com.xjhsk.exampad.base.RxPresenter;
import com.xjhsk.exampad.model.event.LoginEvent;
import com.xjhsk.exampad.model.event.VolumeEvent;
import com.xjhsk.exampad.model.http.DataManager;
import com.xjhsk.exampad.ui.testsound.contract.TestSoundContract;
import com.xjhsk.exampad.ui.testsound.contract.UserFragmentContract;
import com.xjhsk.exampad.utils.CommonSubscriber;
import com.xjhsk.exampad.utils.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

/**
 * 作者：weidingqiang on 2018/1/12 14:53
 * 邮箱：dqwei@iflytek.com
 */

public class UserFragmentPresenter extends RxPresenter<UserFragmentContract.View> implements UserFragmentContract.Presenter {

    protected static final String TAG = UserFragmentPresenter.class.getSimpleName();

    private DataManager mDataManager;

    @Inject
    public UserFragmentPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(UserFragmentContract.View view) {
        super.attachView(view);
        registerEvent();
    }


    private void registerEvent() {
        //确认登陆
        addSubscribe(RxBus.getDefault().toFlowable(VolumeEvent.class)
                .compose(RxUtil.<VolumeEvent>rxSchedulerHelper())
                .filter(new Predicate<VolumeEvent>() {
                    @Override
                    public boolean test(@NonNull VolumeEvent volumeEvent) throws Exception {
                        return volumeEvent.getType().equals(VolumeEvent.KEYCODE_VOLUME);
                    }
                })
                .subscribeWith(new CommonSubscriber<VolumeEvent>(mView) {
                    @Override
                    public void onNext(VolumeEvent s) {
                        mView.volumeChange();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })
        );
    }

}
