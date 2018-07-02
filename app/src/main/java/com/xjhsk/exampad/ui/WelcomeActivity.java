package com.xjhsk.exampad.ui;

import android.os.Environment;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.blankj.utilcode.util.EncryptUtils;
import com.weidingqiang.rxfiflibrary2.app.AppConstants;
import com.weidingqiang.rxfiflibrary2.base.SimpleActivity;
import com.weidingqiang.rxfiflibrary2.utils.FileUtil;
import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.R;
import com.xjhsk.exampad.app.AppContext;
import com.xjhsk.exampad.ui.login.activity.LoginActivity;
import com.xjhsk.exampad.ui.login.activity.ReadyLoginActivity;
import com.xjhsk.exampad.ui.testsound.activity.TestSoundActivity;
import com.xjhsk.exampad.ui.wifi.activity.WifiDefaultActivity;
import com.xjhsk.exampad.ui.wifi.activity.WifiListActivity;
import com.xjhsk.exampad.utils.CompressUtils;
import com.xjhsk.exampad.utils.EncryptUtil;
import com.xjhsk.exampad.utils.RxTimerUtil;
import com.zhy.autolayout.AutoRelativeLayout;

import net.lingala.zip4j.exception.ZipException;

import java.io.File;


public class WelcomeActivity extends SimpleActivity {

    private static final String TAG = WelcomeActivity.class.getSimpleName();

    @Override
    protected int getLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initEventAndData() {
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LogUtil.debug(TAG,"欢迎来到新疆汉语考试APP");

        delay();
    }

    private void delay(){
        RxTimerUtil.interval(2000, new RxTimerUtil.IRxNext() {
            @Override
            public void doNext(long number) {
                if(!AppContext.getInstance().initLogin())
                {
                    return;
                }
//                CompressUtils compressUtils = new CompressUtils();
//                compressUtils.zip(Environment.getExternalStorageDirectory()+"/35001"
//                        ,Environment.getExternalStorageDirectory()+"/35001.zip",
//                        false
//                ,"123456");
//                try {
//                    compressUtils.unZip(new File(Environment.getExternalStorageDirectory()+"/35001.zip"),
//                            Environment.getExternalStorageDirectory()+"/3500101",
//                            "123456");
//                }catch (ZipException e){
//
//                }
//                EncryptUtil.encryptImageFile(Environment.getExternalStorageDirectory()+"/test.jpg",
//                        Environment.getExternalStorageDirectory()+"/test01.jpg");

                //先加密
//                EncryptUtils.encryptMD5File(Environment.getExternalStorageDirectory()+"/35001.zip");



                RxTimerUtil.cancel();

                //因为每次登陆的人员都不同，所以需要
                //打开选择wifi界面
//                startActivity(WifiListActivity.newInstance(getBaseContext()));
                startActivity(WifiDefaultActivity.newInstance(getBaseContext()));
//                startActivity(ReadyLoginActivity.newInstance(getBaseContext()));
//                startActivity(LoginActivity.newInstance(getBaseContext()));
//                startActivity(TestSoundActivity.newInstance(getBaseContext()));
                finish();
            }
        });
    }



}
