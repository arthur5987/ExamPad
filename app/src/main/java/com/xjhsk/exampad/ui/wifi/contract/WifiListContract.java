package com.xjhsk.exampad.ui.wifi.contract;

import com.weidingqiang.rxfiflibrary2.base.BasePresenter;
import com.weidingqiang.rxfiflibrary2.base.BaseView;
import com.xjhsk.exampad.model.bean.ExamTitleVO;

/**
 * 作者：weidingqiang on 2018/1/12 14:52
 * 邮箱：dqwei@iflytek.com
 */

public interface WifiListContract {

    interface View extends BaseView {
        void selectWifiItem(String bssid);

        void reStart();

        void shutDown();
    }

    interface Presenter extends BasePresenter<View> {

    }
}
