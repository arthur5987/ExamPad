package com.xjhsk.exampad.ui.login_stand_alone.contract;

import com.weidingqiang.rxfiflibrary2.base.BasePresenter;
import com.weidingqiang.rxfiflibrary2.base.BaseView;
import com.xjhsk.exampad.model.bean.CheckVerVO;
import com.xjhsk.exampad.model.bean.ExamTitleVO;

/**
 * 作者：weidingqiang on 2018/1/12 14:52
 * 邮箱：dqwei@iflytek.com
 */

public interface ReadyLoginContract_SA {

    interface View extends BaseView {
        //跳转到登录页面
        void jumpLogin();

        void checkVerSuccess(CheckVerVO checkVerVO);

        void responseError(String message);

        void getExamTitleSuccess(ExamTitleVO examTitleVO);

        void connectSuccess();

        void reStart();

        void shutDown();
    }

    interface Presenter extends BasePresenter<View> {
        void checkVer(String version);

        void getStatus();

        void getExamTitle();
    }
}
