package com.xjhsk.exampad.ui.main.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.xjhsk.exampad.R;
import com.xjhsk.exampad.base.RootActivity;
import com.xjhsk.exampad.base.RootFragmentActivity;
import com.xjhsk.exampad.model.bean.PaperVO;
import com.xjhsk.exampad.ui.login.activity.LoginActivity;
import com.xjhsk.exampad.ui.login.activity.ReadyLoginActivity;
import com.xjhsk.exampad.ui.login.contract.LoginContract;
import com.xjhsk.exampad.ui.login.presenter.LoginPresenter;
import com.xjhsk.exampad.ui.main.contract.MainContract;
import com.xjhsk.exampad.ui.main.fragment.ExamFragment;
import com.xjhsk.exampad.ui.main.presenter.MainPresenter;
import com.xjhsk.exampad.ui.testsound.activity.TestSoundActivity;
import com.xjhsk.exampad.ui.testsound.fragment.RecordFragment;
import com.xjhsk.exampad.ui.testsound.fragment.UserFragment;

public class MainActivity extends RootFragmentActivity<MainPresenter> implements MainContract.View {

    private static final String TAG = MainActivity.class.getSimpleName();

    private PaperVO paperVO;

    @Override
    protected void initEventAndData() {

        Intent intent = getIntent();
        paperVO = intent.getParcelableExtra("paperVO");

        loadRootFragment(R.id.user_container, UserFragment.newInstance());
        loadRootFragment(R.id.exam_container, ExamFragment.newInstance(paperVO));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    public static Intent newInstance(Context context, PaperVO paperVO) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("paperVO",paperVO);
        return intent;
    }

    @Override
    public void JumpReadyLogin() {
        startActivity(ReadyLoginActivity.newInstance(getBaseContext()));
        finish();
    }

    @Override
    public void JumpLogin() {
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
}
