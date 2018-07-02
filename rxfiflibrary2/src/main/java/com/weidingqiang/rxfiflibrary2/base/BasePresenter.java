package com.weidingqiang.rxfiflibrary2.base;

/**
 * Created by weidingqiang on 2016/8/2.
 * Presenter基类
 */
public interface BasePresenter<T extends BaseView>{

    void attachView(T view);

    void detachView();
}
