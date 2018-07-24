package com.xjhsk.exampad.app;

import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.EmptyUtils;
import com.squareup.picasso.Picasso;
import com.weidingqiang.rxfiflibrary2.app.AppConfig;
import com.weidingqiang.rxfiflibrary2.app.AppConstants;
import com.weidingqiang.rxfiflibrary2.app.BaseApplication;
import com.weidingqiang.rxfiflibrary2.app.CrashHandler;
import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.R;
import com.xjhsk.exampad.di.component.AppComponent;
import com.xjhsk.exampad.di.component.DaggerAppComponent;
import com.xjhsk.exampad.di.module.AppModule;
import com.xjhsk.exampad.di.module.HttpModule;
import com.xjhsk.exampad.model.bean.AnswerVO;
import com.xjhsk.exampad.model.bean.StringAnswer;
import com.xjhsk.exampad.model.bean.UserData;
import com.xjhsk.exampad.picasso.CustomRequestHandler;
import com.xjhsk.exampad.service.InitializeService;
import com.xjhsk.exampad.socket.SocketManager;

import java.util.ArrayList;
import java.util.Properties;

import skin.support.SkinCompatManager;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.content.res.SkinCompatUserThemeManager;
import skin.support.design.app.SkinMaterialViewInflater;

/**
 * 作者：weidingqiang on 2017/7/10 10:00
 * 邮箱：dqwei@iflytek.com
 */

public class AppContext extends BaseApplication {

    private static final String TAG = AppContext.class.getSimpleName();

    private static AppContext instance;

    public static AppComponent appComponent;

    private UserData userVO;
    private StringAnswer stringAnswer;

    private String ip;

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;

        //初始化socket
        LogUtil.debug(TAG,"实例化socket manager");
//        socketManager = new SocketManager();
    }

    private String hostIp;

    private boolean isNetWorking; // 是否是联网版

    public boolean isNetWorking() {
        return isNetWorking;
    }

    public void setNetWorking(boolean netWorking) {
        isNetWorking = netWorking;
    }

    public String getHeaderImgUrl() {
        return headerImgUrl;
    }

    public void setHeaderImgUrl(String headerImgUrl) {
        this.headerImgUrl = headerImgUrl;
    }

    private String headerImgUrl;

    public ArrayList<AnswerVO> getAnswerVOS() {
        return answerVOS;
    }

    private ArrayList<AnswerVO> answerVOS;

    public SocketManager getSocketManager() {
        return socketManager;
    }

    private SocketManager socketManager;

    public interface MsgDisplayListener {
        void handle(String msg);
    }

    public static MsgDisplayListener msgDisplayListener = null;
    public static StringBuilder cacheMsg = new StringBuilder();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        //在子线程中完成其他初始化
        LogUtil.debug(TAG,"在子线程中完成其他初始化");
        InitializeService.start(this);

        //初始化崩溃信息
        LogUtil.debug(TAG,"初始化崩溃信息");
        initCrash();

        answerVOS = new ArrayList<AnswerVO>();

        //初始化登陆
//        initLogin();
        instance = this;

        //注册Picasso特殊URL处理
        Picasso picasso = new Picasso.Builder(this).addRequestHandler(new CustomRequestHandler()).build();
        Picasso.setSingletonInstance(picasso);

        SkinCompatManager.withoutActivity(this)                         // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();

        SkinCompatManager.getInstance().loadSkin("skin_uighur.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
    }

    public boolean initLogin() {

        if (AppConstants.DOWNLOAD_PATH.length() < 3) {
            return false;
        }

        return true;
    }

    private void initCrash() {
        // 获取异常信息捕获类实例
        //        开发期间不要监听 稍后放开
        CrashHandler crashHandler = CrashHandler.getInstance(getApplicationContext());
//
        crashHandler.setICrashHandlerListener(this);
//        // 初始化
        crashHandler.init(getApplicationContext());
    }

    /**
     * 保存登录信息
     */
    @SuppressWarnings("serial")
    public void saveUserInfo(UserData userVO) {
        LogUtil.debug(TAG,"保存用户信息");
        this.userVO = userVO;
    }

    public UserData getUserVO() {
        return userVO;
    }

    /**
     * 保存登录信息
     */
    @SuppressWarnings("serial")
    public void saveStringAnswer(StringAnswer stringAnswer) {
        LogUtil.debug(TAG,"保存上传字符串");
        this.stringAnswer = stringAnswer;
    }

    public StringAnswer getStringAnswer() {
        return stringAnswer;
    }

    public static AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(instance))
                    .httpModule(new HttpModule())
                    .build();
        }
        return appComponent;
    }

    /**
     * 获得当前app运行的AppContext
     */
    public static AppContext getInstance() {
        return instance;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
