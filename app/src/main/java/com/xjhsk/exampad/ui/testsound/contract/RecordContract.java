package com.xjhsk.exampad.ui.testsound.contract;

import com.weidingqiang.rxfiflibrary2.base.BasePresenter;
import com.weidingqiang.rxfiflibrary2.base.BaseView;
import com.xjhsk.exampad.model.bean.PaperVO;

/**
 * 作者：weidingqiang on 2018/1/12 14:52
 * 邮箱：dqwei@iflytek.com
 */

public interface RecordContract {

    interface View extends BaseView {
        void analyzeSuccess(PaperVO paperVO);

        void setStatusSuccess();

        void responceError(String message);

        void startExamSuccess();

        void progessFinish();

        void startExam();
    }

    interface Presenter extends BasePresenter<View> {
        void loadZipData(String zipfilename);

        void setStatus(String status);

        void startExam(String status);
    }
}
