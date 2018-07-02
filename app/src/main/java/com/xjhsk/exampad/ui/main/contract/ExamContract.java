package com.xjhsk.exampad.ui.main.contract;

import com.weidingqiang.rxfiflibrary2.base.BasePresenter;
import com.weidingqiang.rxfiflibrary2.base.BaseView;
import com.xjhsk.exampad.model.bean.AnswerVO;

/**
 * 作者：weidingqiang on 2018/1/12 14:52
 * 邮箱：dqwei@iflytek.com
 */

public interface ExamContract {

    interface View extends BaseView {
        void nextPage();

        void saveAnswer(AnswerVO answerVO);

        void waitSecond(int second);

        void recordSecond(int second,String problemNO);

        void setStatusSuccess();

        void responceError();

        void saveExamStepSuccess();

        void progessFinish();
    }

    interface Presenter extends BasePresenter<View> {
        void setStatus(String status);

        void saveExamStep(String examStep);
    }
}
