package com.xjhsk.exampad.ui.main.contract;

import com.weidingqiang.rxfiflibrary2.base.BasePresenter;
import com.weidingqiang.rxfiflibrary2.base.BaseView;

/**
 * 作者：weidingqiang on 2018/1/12 14:52
 * 邮箱：dqwei@iflytek.com
 */

public interface MainContract {

    interface View extends BaseView {
        void JumpReadyLogin();

        void JumpLogin();

        void reStart();

        void shutDown();
    }

    interface Presenter extends BasePresenter<View> {

    }
}
