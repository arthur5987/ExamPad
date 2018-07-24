package com.xjhsk.exampad.ui.login_stand_alone.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.weidingqiang.rxfiflibrary2.managers.AppManagers;
import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.R;
import com.xjhsk.exampad.base.RootActivity;
import com.xjhsk.exampad.model.bean.CheckVerVO;
import com.xjhsk.exampad.model.bean.ExamTitleVO;
import com.xjhsk.exampad.ui.login_stand_alone.contract.ReadyLoginContract_SA;
import com.xjhsk.exampad.ui.login_stand_alone.presenter.ReadyLoginPresenter_SA;
import com.xjhsk.exampad.utils.RxTimerUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 等待登陆
 * 主要功能为：
 * 1.检测更新
 * 2.开启socket服务，等待登陆指令
 */

/**
 * 1月16日（周二）
 1.集成socket服务
 2.完善登陆界面
 3.个人信息界面
 4.等待登陆界面
 */
public class ReadyLoginActivity_SA extends RootActivity<ReadyLoginPresenter_SA> implements ReadyLoginContract_SA.View{

    private static final String TAG = ReadyLoginActivity_SA.class.getSimpleName();

    @BindView(R.id.title_tv)
    TextView title_tv;

    private static final int REQUEST_CODE_UPGRADE = 10;

    @Override
    protected int getLayout() {
        return R.layout.activity_ready_login;
    }

    @Override
    protected void initEventAndData() {

        /**
         * 先请求app更新
         * 如果更新服务没打开或者检测有更新，则需要强制更新
         * 当没有更新时，需要开启socket服务
         */

        LogUtil.debug(TAG,"开始检测APK是否存在更新");

        mPresenter.getExamTitle();
    }

    @Override
    public void checkVerSuccess(CheckVerVO checkVerVO) {}

    @Override
    public void getExamTitleSuccess(ExamTitleVO examTitleVO) {
        title_tv.setText(examTitleVO.getTitle());

        //等待3秒跳转
        RxTimerUtil.timer(3000, new RxTimerUtil.IRxNext() {
            @Override
            public void doNext(long number) {
                jumpLogin();
            }
        });
    }

    @Override
    public void connectSuccess() {}

    @Override
    public void responseError(String message) {}

    @OnClick(R.id.title_tv)
    void onTestTitle(){
        //要删除的方法
//        jumpLogin();
//        AppContext.getInstance().getSocketManager().sendOrder(SocketManager.ReadyLogin);
//        AppContext.getInstance().getSocketManager().sendMessage(SocketManager.ReadyLogin);
    }

    @Override
    public void jumpLogin() {
        startActivity(LoginActivity_SA.newInstance(this));
        finish();
    }

    @Override
    public void reStart() {

    }

    @Override
    public void shutDown() {

        AppManagers.getInstance().killAllActivity();
        // 退出应用程序
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, ReadyLoginActivity_SA.class);
        return intent;
    }


}
