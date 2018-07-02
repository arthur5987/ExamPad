package com.xjhsk.exampad.ui.login.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fifedu.record.media.record.AudioPlayManager;
import com.fifedu.record.media.record.AudioplayInterface;
import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.R;
import com.xjhsk.exampad.api.Constants;
import com.xjhsk.exampad.app.AppContext;
import com.xjhsk.exampad.base.RootActivity;
import com.xjhsk.exampad.model.bean.UserData;
import com.xjhsk.exampad.ui.login.contract.LoginContract;
import com.xjhsk.exampad.ui.login.presenter.LoginPresenter;
import com.xjhsk.exampad.ui.testsound.activity.TestSoundActivity;
import com.xjhsk.exampad.ui.wifi.activity.WifiListActivity;
import com.xjhsk.exampad.ui.wifi.contract.WifiListContract;
import com.xjhsk.exampad.ui.wifi.presenter.WifiListPresenter;
import com.xjhsk.exampad.utils.Sha1Util;
import com.xjhsk.exampad.widget.login.LoginUserInfoView;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登陆模块
 * 主要功能为：
 * 1.登陆账号
 * 2.随机下载试卷，解压Zip操作(不在这里下载了 修改到 在试音的地方)
 * 3.登陆成功后 会连接socket 等待统一做题
 *
 */
public class LoginActivity extends RootActivity<LoginPresenter> implements LoginContract.View{

    private static final String TAG = LoginActivity.class.getSimpleName();

    //登陆界面
    @BindView(R.id.login_layout)
    AutoLinearLayout login_layout;

    //输入的考号
    @BindView(R.id.exam_code_tv)
    TextView exam_code_tv;

    //登陆
    @BindView(R.id.login_img)
    ImageView login_img;

    //键盘组件
    @BindView(R.id.img_keyboard_1)
    ImageView img_keyboard_1;
    @BindView(R.id.img_keyboard_2)
    ImageView img_keyboard_2;
    @BindView(R.id.img_keyboard_3)
    ImageView img_keyboard_3;
    @BindView(R.id.img_keyboard_4)
    ImageView img_keyboard_4;
    @BindView(R.id.img_keyboard_5)
    ImageView img_keyboard_5;
    @BindView(R.id.img_keyboard_6)
    ImageView img_keyboard_6;
    @BindView(R.id.img_keyboard_7)
    ImageView img_keyboard_7;
    @BindView(R.id.img_keyboard_8)
    ImageView img_keyboard_8;
    @BindView(R.id.img_keyboard_9)
    ImageView img_keyboard_9;
    @BindView(R.id.img_keyboard_0)
    ImageView img_keyboard_0;
    @BindView(R.id.img_keyboard_delete)
    ImageView img_keyboard_delete;
    @BindView(R.id.img_keyboard_clear)
    ImageView img_keyboard_clear;
    @BindView(R.id.point_layout)
    AutoLinearLayout point_layout;

    //用户信息
    @BindView(R.id.login_user_info_view)
    LoginUserInfoView login_user_info_view;

    private AssetManager am;

    private AudioPlayManager _audioManager;// 音频播放管理

    @Override
    protected void initEventAndData() {
        am = getAssets();
        point_layout.setVisibility(View.INVISIBLE);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    //************************************************************************************************//
    @OnClick(R.id.login_img)
    void onLoginBut(){
        LogUtil.debug(TAG,"点击登陆按钮");
        String username = exam_code_tv.getText().toString();

        if(username.equals("请输入考号") || username.length() < 3 || username.length() > 10)
        {
            ToastUtils.showShortToast("请输入正确的考号");
            LogUtil.debug(TAG,"输入的考号不正确");
            return;
        }

        String time_mills = String.valueOf(TimeUtils.getNowTimeMills());
        String key = Sha1Util.getSha1(time_mills+ Constants.KEY);

        LogUtil.debug(TAG,"请求登陆  用户名 "+ username+" ip "+AppContext.getInstance().getIp());
        mPresenter.login(username, AppContext.getInstance().getIp(),key,time_mills);
    }

    @Override
    public void loginSuccess(UserData userData) {
        LogUtil.debug(TAG,"验证用户信息成功  播放确认登陆语音文件");
        login_user_info_view.setData(userData);
        login_user_info_view.setVisibility(View.VISIBLE);
        login_layout.setVisibility(View.GONE);
        try {
            AssetFileDescriptor assetFileDescriptor= am.openFd("UserConfirm.wav");
            playAudio(assetFileDescriptor);
        }catch (Exception ex){

        }

    }

    @Override
    public void loginError(String message) {
        LogUtil.debug(TAG,"请求收到的错误信息 "+message);
        ToastUtils.showShortToast(message);
    }

    //************************************************************************************************//
    //键盘操作
    @OnClick(R.id.img_keyboard_1)
    void onImgKeyboard_1(){
        inputKeyWord("1");
    }
    @OnClick(R.id.img_keyboard_2)
    void onImgKeyboard_2(){
        inputKeyWord("2");
    }
    @OnClick(R.id.img_keyboard_3)
    void onImgKeyboard_3(){
        inputKeyWord("3");
    }
    @OnClick(R.id.img_keyboard_4)
    void onImgKeyboard_4(){
        inputKeyWord("4");
    }
    @OnClick(R.id.img_keyboard_5)
    void onImgKeyboard_5(){
        inputKeyWord("5");
    }
    @OnClick(R.id.img_keyboard_6)
    void onImgKeyboard_6(){
        inputKeyWord("6");
    }
    @OnClick(R.id.img_keyboard_7)
    void onImgKeyboard_7(){
        inputKeyWord("7");
    }
    @OnClick(R.id.img_keyboard_8)
    void onImgKeyboard_8(){
        inputKeyWord("8");
    }
    @OnClick(R.id.img_keyboard_9)
    void onImgKeyboard_9(){
        inputKeyWord("9");
    }
    @OnClick(R.id.img_keyboard_0)
    void onImgKeyboard_0(){
        inputKeyWord("0");
    }
    @OnClick(R.id.img_keyboard_delete)
    void onImgKeyboard_Delete(){
        if(exam_code_tv.getText().toString().equals("请输入考号")){
            return;
        }

        int length = exam_code_tv.getText().toString().length();
        if(length>0){
            exam_code_tv.setText(exam_code_tv.getText().toString().substring(0,length-1));
        }

        if(EmptyUtils.isEmpty(exam_code_tv.getText().toString())){
            exam_code_tv.setText("请输入考号");
        }
    }
    @OnClick(R.id.img_keyboard_clear)
    void onImgKeyboard_Clear(){
        exam_code_tv.setText("请输入考号");
    }

    private void inputKeyWord(String code){
        LogUtil.debug(TAG,"键盘操作输入 "+code);
        if(exam_code_tv.getText().toString().equals("请输入考号")){
            exam_code_tv.setText(code);
        }
        else {
            exam_code_tv.setText(exam_code_tv.getText()+code);
        }
    }

    //************************************************************************************************//
    //用户信息界面
    @Override
    public void loginSure() {
        //直接跳转到试音阶段
        LogUtil.debug(TAG,"登陆成功跳转到试音界面 ");
        startActivity(TestSoundActivity.newInstance(this));
        finish();
    }

    @Override
    public void loginCancel() {
        LogUtil.debug(TAG,"点击取消登陆按钮 ");
        String time_mills = String.valueOf(TimeUtils.getNowTimeMills());
        String key = Sha1Util.getSha1(time_mills+ Constants.KEY);

        mPresenter.logout(exam_code_tv.getText().toString(), AppContext.getInstance().getIp(),key,time_mills);
    }

    //************************************************************************************************//
    //退出登录

    @Override
    public void logoutSuccess() {
        //清空之前数据
        exam_code_tv.setText("");

        login_layout.setVisibility(View.VISIBLE);
        login_user_info_view.setVisibility(View.GONE);

        //关闭提示音
        if(EmptyUtils.isNotEmpty(_audioManager) && _audioManager.isPlaying()){
            _audioManager.stop();
        }
    }

    @Override
    public void logoutError(String message) {
        ToastUtils.showShortToast(message);
    }
    //************************************************************************************************//

    private void playAudio(AssetFileDescriptor assetFileDescriptor) {

        if (_audioManager == null)
            _audioManager = AudioPlayManager.getManager();

        _audioManager.startPlay(assetFileDescriptor, audioPlayListen, null);
    }

    private AudioplayInterface audioPlayListen = new AudioplayInterface() {

        @Override
        public void onerror(String str) {
            LogUtil.debug(TAG,"音频播放错误 ");
            ToastUtils.showShortToast("音频播放错误");
        }

        @Override
        public void complete() {
            LogUtil.debug(TAG,"音频播放完毕 ");
        }

        @Override
        public void interupt() {
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if(EmptyUtils.isNotEmpty(_audioManager) && _audioManager.isPlaying()){
            _audioManager.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void reStart() {

    }

    @Override
    public void shutDown() {

    }

    @Override
    public void goReadyLogin() {
        startActivity(ReadyLoginActivity.newInstance(this));
        finish();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }
}
