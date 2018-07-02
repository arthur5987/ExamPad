package com.xjhsk.exampad.di.component;

import com.xjhsk.exampad.app.AppContext;
import com.xjhsk.exampad.di.module.AppModule;
import com.xjhsk.exampad.di.module.HttpModule;
import com.xjhsk.exampad.model.http.DataManager;
import com.xjhsk.exampad.model.http.RetrofitHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 作者：weidingqiang on 2017/7/10 14:40
 * 邮箱：dqwei@iflytek.com
 */
@Singleton
@Component(modules = {AppModule.class, HttpModule.class})
public interface AppComponent {

    AppContext getContext();  // 提供App的Context

    DataManager getDataManager(); //数据中心

    RetrofitHelper retrofitHelper();  //提供http的帮助类
}
