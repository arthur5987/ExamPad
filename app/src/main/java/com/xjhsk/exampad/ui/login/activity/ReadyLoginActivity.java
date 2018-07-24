package com.xjhsk.exampad.ui.login.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.weidingqiang.rxfiflibrary2.managers.AppManagers;
import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.R;
import com.xjhsk.exampad.app.AppContext;
import com.xjhsk.exampad.base.RootActivity;
import com.xjhsk.exampad.model.bean.CheckVerVO;
import com.xjhsk.exampad.model.bean.ExamTitleVO;
import com.xjhsk.exampad.socket.SocketManager;
import com.xjhsk.exampad.ui.login.contract.ReadyLoginContract;
import com.xjhsk.exampad.ui.login.presenter.ReadyLoginPresenter;
import com.xjhsk.exampad.ui.main.activity.SoftwareUpdateActivity;
import com.xjhsk.exampad.utils.RxTimerUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xjhsk.exampad.utils.RxTimerUtil.interval;

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
public class ReadyLoginActivity extends RootActivity<ReadyLoginPresenter> implements ReadyLoginContract.View{

    private static final String TAG = ReadyLoginActivity.class.getSimpleName();

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

        //1.检测更新
        mPresenter.checkVer(AppUtils.getAppVersionName(this));


    }

    @Override
    public void checkVerSuccess(CheckVerVO checkVerVO) {
        //判断是否要启动升级
//        Intent intent = new Intent(ReadyLoginActivity_SA.this, SoftwareUpdateActivity.class);
//        intent.putExtra("update_model", checkVerVO);
//        startActivityForResult(intent, REQUEST_CODE_UPGRADE);

        LogUtil.debug(TAG,"开始获取考试标题");
        //2.获取考试标题
        mPresenter.getExamTitle();
    }

    @Override
    public void getExamTitleSuccess(ExamTitleVO examTitleVO) {
        title_tv.setText(examTitleVO.getTitle());

        mPresenter.getStatus();
        RxTimerUtil.interval(10 * 1000, new RxTimerUtil.IRxNext() {
            @Override
            public void doNext(long number) {
                mPresenter.getStatus();
            }
        });


//        LogUtil.debug(TAG,"判断socket是否已经连接");
//        //2.socket是否已经启动
//        if (AppContext.getInstance().getSocketManager().isConnect()){
//            Log.e("==========","isConnect");
//            LogUtil.debug(TAG,"socket已经连接 发送状态码");
//            mPresenter.getStatus();
//        }
//        else {
//            Log.e("==========","nononono");
//            LogUtil.debug(TAG,"开始连接socket服务");
//            AppContext.getInstance().getSocketManager().connect();
//        }
    }

    @Override
    public void connectSuccess() {
        LogUtil.debug(TAG,"socket已经连接 发送状态码");
        mPresenter.getStatus();
    }

    @Override
    public void responseError(String message) {
        ToastUtils.showShortToast(message);
        //发送socket请求 准备登陆
//        AppContext.getInstance().getSocketManager().sendMessage(SocketManager.ReadyLogin); //  去掉socket 注掉的东西
    }

    @OnClick(R.id.title_tv)
    void onTestTitle(){
        //要删除的方法
//        jumpLogin();
//        AppContext.getInstance().getSocketManager().sendOrder(SocketManager.ReadyLogin);
//        AppContext.getInstance().getSocketManager().sendMessage(SocketManager.ReadyLogin);
    }

    @Override
    public void jumpLogin() {
        startActivity(LoginActivity.newInstance(this));
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
        Intent intent = new Intent(context, ReadyLoginActivity.class);
        return intent;
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        switch (arg0) {
            case REQUEST_CODE_UPGRADE:
                if (arg1 == RESULT_OK) {
                    finish();
                } else if (arg1 == RESULT_CANCELED) {
                    // 区分强制更新和非强制更新的取消效果
                    finish();
                } else if (arg1 == SoftwareUpdateActivity.RESULT_DISMISS) {
//                    LogUtil.debug(TAG,"取消更新");
                }
                break;
            default:
                break;
        }
    }
}
