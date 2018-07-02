package com.weidingqiang.rxfiflibrary2.base;

/**
 * Created by weidingqiang on 2016/8/2.
 * View基类
 */
public interface BaseView {

    void showErrorMsg(String msg);

    //=======  State  =======
    void stateError();

    void stateEmpty();

    void stateLoading();

    void stateMain();

}
