package com.xjhsk.exampad.ui.login_stand_alone.contract;

import com.weidingqiang.rxfiflibrary2.base.BasePresenter;
import com.weidingqiang.rxfiflibrary2.base.BaseView;
import com.xjhsk.exampad.model.bean.UserData;

/**
 * 作者：weidingqiang on 2018/1/12 14:52
 * 邮箱：dqwei@iflytek.com
 */

public interface LoginContract_SA {

    interface View extends BaseView {
        void loginSuccess(UserData userData);
        void loginError(String message);

        void loginSure();
        void loginCancel();

        void logoutSuccess();
        void logoutError(String message);

        void reStart();

        void shutDown();

        void goReadyLogin();
    }

    interface Presenter extends BasePresenter<View> {
        void login(String examNo, String examIp, String key, String timestamp);

        void logout(String examNo, String examIp, String key, String timestamp);
    }
}
