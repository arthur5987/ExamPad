package com.xjhsk.exampad.di.component;

import android.app.Activity;

import com.xjhsk.exampad.di.module.ActivityModule;
import com.xjhsk.exampad.di.scope.ActivityScope;
import com.xjhsk.exampad.ui.login.activity.LoginActivity;
import com.xjhsk.exampad.ui.login.activity.ReadyLoginActivity;
import com.xjhsk.exampad.ui.login_stand_alone.activity.LoginActivity_SA;
import com.xjhsk.exampad.ui.login_stand_alone.activity.ReadyLoginActivity_SA;
import com.xjhsk.exampad.ui.main.activity.MainActivity;
import com.xjhsk.exampad.ui.testsound.activity.TestSoundActivity;
import com.xjhsk.exampad.ui.wifi.activity.WifiDefaultActivity;
import com.xjhsk.exampad.ui.wifi.activity.WifiListActivity;

import dagger.Component;

/**
 * 作者：weidingqiang on 2017/7/10 16:04
 * 邮箱：dqwei@iflytek.com
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class})
public interface ActivityComponent {

    Activity getActivity();

    void inject(WifiListActivity wifiListActivity);

    void inject(LoginActivity loginActivity);

    void inject(LoginActivity_SA loginActivity);

    void inject(ReadyLoginActivity readyLoginActivity);

    void inject(ReadyLoginActivity_SA readyLoginActivity);

    void inject(TestSoundActivity testSoundActivity);

    void inject(MainActivity mainActivity);

    void inject(WifiDefaultActivity wifiDefaultActivity);
}
