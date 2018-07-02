package com.weidingqiang.rxfiflibrary2.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.weidingqiang.rxfiflibrary2.managers.AppManagers;
import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.weidingqiang.rxfiflibrary2.utils.RxTimerUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：weidingqiang on 2017/7/10 11:11
 * 邮箱：dqwei@iflytek.com
 */

public abstract class SimpleFragmentActivity extends SupportAutoActivity{

    //退出事件
    private long exitTime = 0;

    protected Activity mContext;
    private Unbinder mUnBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    // TODO: The system bars are visible. Make any desired
                    hideSystemUI();
                } else {
                    // TODO: The system bars are NOT visible. Make any desired
                }
            }
        });
        hideSystemUI();

        // 保持屏幕唤醒状态
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try {
            getSupportActionBar().hide();
        }catch (Exception ex){

        }

        setContentView(getLayout());
        mUnBinder = ButterKnife.bind(this);
        mContext = this;
        onViewCreated();
        AppManagers.getInstance().addActivity(this);
        initEventAndData();
    }

    protected void onViewCreated() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManagers.getInstance().killActivity(this);
        mUnBinder.unbind();
    }

    protected abstract int getLayout();
    protected abstract void initEventAndData();

    //-------------------------------------------------------------------//
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            AppManagers.getInstance().exit(this);
        }
    }

    /**
     * 开启隐藏
     */
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(getWindow().getDecorView().getSystemUiVisibility() !=3847 &&ev.getAction() == MotionEvent.ACTION_UP)
        {
            hideSystemUI();
        }
        return super.dispatchTouchEvent(ev);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if(getWindow().getDecorView().getSystemUiVisibility() !=3847 &&event.getAction() == MotionEvent.ACTION_UP)
//        {
//            hideSystemUI();
//        }
//        return super.onTouchEvent(event);
//    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus && getWindow().getDecorView().getSystemUiVisibility() !=3847 )
        {
            hideSystemUI();
        }
        super.onWindowFocusChanged(hasFocus);
    }

}
