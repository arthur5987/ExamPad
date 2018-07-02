package com.xjhsk.exampad.ui.exam.contract;

import com.weidingqiang.rxfiflibrary2.base.BasePresenter;
import com.weidingqiang.rxfiflibrary2.base.BaseView;
import com.xjhsk.exampad.model.bean.PagerInfo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 作者：weidingqiang on 2018/1/12 14:52
 * 邮箱：dqwei@iflytek.com
 */

public interface EEndContract {

    interface View extends BaseView {
        void uploadExamSuccess();

        void uploadExamError();

        void endExam();
    }

    interface Presenter extends BasePresenter<View> {
        void createJsonFileAndZip(String userDir ,PagerInfo pagerInfo);
    }
}
