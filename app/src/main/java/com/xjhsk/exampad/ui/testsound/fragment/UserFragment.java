package com.xjhsk.exampad.ui.testsound.fragment;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.R;
import com.xjhsk.exampad.api.Constants;
import com.xjhsk.exampad.app.AppContext;
import com.xjhsk.exampad.base.RootFragment;
import com.xjhsk.exampad.model.bean.UserData;
import com.xjhsk.exampad.model.http.api.ExamApis;
import com.xjhsk.exampad.picasso.CircleTransform;
import com.xjhsk.exampad.ui.testsound.contract.UserFragmentContract;
import com.xjhsk.exampad.ui.testsound.presenter.UserFragmentPresenter;
import com.xjhsk.exampad.utils.GlideCircleTransform;
import com.xjhsk.exampad.utils.Sha1Util;

import butterknife.BindView;

/**
 * 作者：weidingqiang on 2018/1/16 20:43
 * 邮箱：dqwei@iflytek.com
 */

public class UserFragment extends RootFragment<UserFragmentPresenter> implements UserFragmentContract.View {

    private static final String TAG = UserFragment.class.getSimpleName();

    @BindView(R.id.head_img)
    ImageView head_img;

    @BindView(R.id.user_name_tv)
    TextView user_name_tv;

    @BindView(R.id.code_tv)
    TextView code_tv;

    @BindView(R.id.grade_tv)
    TextView grade_tv;

    @BindView(R.id.sex_tv)
    TextView sex_tv;

    @BindView(R.id.seek_bar)
    SeekBar seek_bar;

    private AudioManager mAudioManager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    protected void initEventAndData() {

        LogUtil.debug(TAG,"设置用户信息");

        UserData userData = AppContext.getInstance().getUserVO();

//        Glide.with(getContext())
//                .load(AppContext.getInstance().getHeaderImgUrl())
//                .placeholder(R.drawable.icon_default_header_img)
//                .transform(new GlideCircleTransform(getContext()))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(head_img);

        Picasso.with(getContext())
                .load(getImageUrlForPicasso())
                .transform(new CircleTransform(getContext()))
                .into(head_img);

        user_name_tv.setText(userData.getExam_stu_name().replace("·","·\n"));

        if(EmptyUtils.isEmpty(userData.getExam_stu_sex())){
            sex_tv.setVisibility(View.INVISIBLE);
        }
        else {
            if(userData.getExam_stu_sex() == 1 ){
                sex_tv.setText("性别 : 男");
            }else {
                sex_tv.setText("性别 : 女");
            }
            sex_tv.setVisibility(View.VISIBLE);
        }

        code_tv.setText("考号 : "+userData.getExam_no());

        if(EmptyUtils.isEmpty(userData.getExam_level())){
            grade_tv.setVisibility(View.GONE);
        }
        else {
            switch (userData.getExam_level())
            {
                case 1:
                    grade_tv.setText("级别 : 一级");
                    break;
                case 2:
                    grade_tv.setText("级别 : 二级");
                    break;
                case 3:
                    grade_tv.setText("级别 : 三级");
                    break;
                case 4:
                    grade_tv.setText("级别 : 四级");
                    break;
            }
            grade_tv.setVisibility(View.VISIBLE);
        }

        //注册滑动事件
        initEvent();

        //设置声音
        setVolume();

    }

    private void setVolume(){
        LogUtil.debug(TAG,"设置音量管理");
        //初始化音频管理器
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        //获取系统最大音量
        int maxVolume=mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 获取设备当前音量
        int currentVolume =mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        // 设置seekbar的最大值
        seek_bar.setMax(maxVolume);
        // 显示音量
        seek_bar.setProgress(currentVolume);

//        mAudioManager
    }

    private void initEvent(){
        //SeekBar的监听事件
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            //监听滑动时
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                LogUtil.debug(TAG,"监听到音量滑块变动 数值为 "+progress);

                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);

//                //通话音量
//                int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL );
//                //系统音量
//                current = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM );
//                //铃声音量
//                current = mAudioManager.getStreamVolume(AudioManager.STREAM_RING );
//                //音乐音量
//                current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC );
//                //提示声音音量
//                current = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM );
            }

            //监听点击时
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                tv_sb.setText("开始");
            }

            //监听停止时
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                tv_sb.setText("结束");
            }
        });
    }

    @Override
    public void volumeChange() {
        // 获取设备当前音量
//        int currentVolume =mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
//        // 显示音量
//        seek_bar.setProgress(currentVolume);
    }

    public String getImageUrlForPicasso() {
        return "encrypt_"+"http://"+AppContext.getInstance().getHostIp()+":8083/xhk/res/examstu/"
                +AppContext.getInstance().getUserVO().getExam_batch()+"/"+AppContext.getInstance().getUserVO().getExam_no()+".jpg";
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }


    public static UserFragment newInstance() {
        Bundle args = new Bundle();

        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
