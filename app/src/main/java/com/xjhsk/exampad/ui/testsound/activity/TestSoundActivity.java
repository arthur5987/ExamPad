package com.xjhsk.exampad.ui.testsound.activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.R;
import com.xjhsk.exampad.base.RootFragmentActivity;
import com.xjhsk.exampad.ui.login.activity.LoginActivity;
import com.xjhsk.exampad.ui.testsound.contract.TestSoundContract;
import com.xjhsk.exampad.ui.testsound.fragment.RecordFragment;
import com.xjhsk.exampad.ui.testsound.fragment.UserFragment;
import com.xjhsk.exampad.ui.testsound.fragment_stand_alone.RecordFragment_SA;
import com.xjhsk.exampad.ui.testsound.fragment_stand_alone.UserFragment_SA;
import com.xjhsk.exampad.ui.testsound.presenter.TestSoundPresenter;

/**
 * 试音界面
 * 主要功能为
 * 1.测试耳机声音 和 录制声音
 * 2.下载试卷zip包
 * 3.等待考试
 */
public class TestSoundActivity extends RootFragmentActivity<TestSoundPresenter> implements TestSoundContract.View{

    private static final String TAG = TestSoundActivity.class.getSimpleName();

    @Override
    protected void initEventAndData() {
        if (isNetWorking()){
            loadRootFragment(R.id.user_container, UserFragment.newInstance());
            loadRootFragment(R.id.main_container, RecordFragment.newInstance());
        }else {
            loadRootFragment(R.id.user_container, UserFragment_SA.newInstance());
            loadRootFragment(R.id.main_container, RecordFragment_SA.newInstance());
        }

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_test_sound;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, TestSoundActivity.class);
        return intent;
    }

    @Override
    public void JumpLogin() {
        LogUtil.debug(TAG,"退出登录");
        startActivity(LoginActivity.newInstance(getBaseContext()));
        finish();
    }

    @Override
    public void reStart() {

    }

    @Override
    public void shutDown() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_MUTE:
//                RxBus.getDefault().post(new VolumeEvent(VolumeEvent.KEYCODE_VOLUME));
                //不能通过声音键，因会出现菜单条
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void closeSoundActivity() {
        finish();
    }
}
