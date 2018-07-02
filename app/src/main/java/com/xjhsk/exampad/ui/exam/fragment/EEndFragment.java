package com.xjhsk.exampad.ui.exam.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.fifedu.record.media.record.AudioPlayManager;
import com.fifedu.record.media.record.AudioplayInterface;
import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.R;
import com.xjhsk.exampad.app.AppContext;
import com.xjhsk.exampad.base.RootFragment;
import com.xjhsk.exampad.base.RxBus;
import com.xjhsk.exampad.model.bean.PagerInfo;
import com.xjhsk.exampad.model.bean.PaperAction;
import com.xjhsk.exampad.model.bean.PaperSection;
import com.xjhsk.exampad.model.event.ActionEvent;
import com.xjhsk.exampad.socket.SocketManager;
import com.xjhsk.exampad.ui.exam.contract.EEndContract;
import com.xjhsk.exampad.ui.exam.contract.EHintContract;
import com.xjhsk.exampad.ui.exam.presenter.EEndPresenter;
import com.xjhsk.exampad.ui.exam.presenter.EHintPresenter;
import com.xjhsk.exampad.utils.RxTimerUtil;
import com.xjhsk.exampad.widget.ETextView;
import com.xjhsk.exampad.widget.ProgressImageView;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：weidingqiang on 2018/1/18 17:22
 * 邮箱：dqwei@iflytek.com
 */

public class EEndFragment extends RootFragment<EEndPresenter> implements EEndContract.View  {

    private static final String TAG = EEndFragment.class.getSimpleName();

    @BindView(R.id.load_layout)
    AutoLinearLayout load_layout;

    @BindView(R.id.success_layout)
    AutoLinearLayout success_layout;

    @BindView(R.id.error_layout)
    AutoLinearLayout error_layout;

    @BindView(R.id.iv_progress)
    ProgressImageView iv_progress;

    @BindView(R.id.upload_tv)
    TextView upload_tv;

    @BindView(R.id.success_info_tv)
    TextView success_info_tv;

    private String userDir;

    private PagerInfo pagerInfo;

    private AudioPlayManager _audioManager;// 音频播放管理

    @Override
    protected void initEventAndData() {
        iv_progress.start();

        //获取数据
        userDir = getArguments().getString("userDir");
        pagerInfo = getArguments().getParcelable("pagerInfo");

        //需要异步压缩 打包  上传
        int random= (int)(1+Math.random()*5);

        RxTimerUtil.timer(1000 * random, new RxTimerUtil.IRxNext() {
            @Override
            public void doNext(long number) {
                mPresenter.createJsonFileAndZip(userDir,pagerInfo);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_e_end;
    }


    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    public static EEndFragment newInstance(String userDir,PagerInfo pagerInfo) {
        Bundle args = new Bundle();
        EEndFragment fragment = new EEndFragment();
        args.putString("userDir",userDir);
        args.putParcelable("pagerInfo",pagerInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void uploadExamSuccess() {
        success_layout.setVisibility(View.VISIBLE);
        load_layout.setVisibility(View.GONE);

        AppContext.getInstance().getSocketManager().sendMessage(SocketManager.ReadyExamEnd);
    }

    @Override
    public void endExam() {
        success_info_tv.setText("考试完毕，请大家有序退场");
    }

    @Override
    public void uploadExamError() {
        error_layout.setVisibility(View.VISIBLE);
        load_layout.setVisibility(View.GONE);
    }

    @OnClick(R.id.upload_tv)
    void onUploadButton(){
        mPresenter.createJsonFileAndZip(userDir,pagerInfo);
        load_layout.setVisibility(View.VISIBLE);
        error_layout.setVisibility(View.GONE);
    }

    /**
     * 播放
     * @param audioname
     */
    private void playAudio(String audioname){

        if(_audioManager == null)
            _audioManager = AudioPlayManager.getManager();

        if (FileUtils.isFileExists(audioname)) {
            _audioManager.startPlay(audioname, audioname, audioPlayListen, getView());
        }
        else{
            LogUtil.debug(TAG,"playAudio","播放文件不存在 "+audioname);
        }
    }


    private AudioplayInterface audioPlayListen = new AudioplayInterface() {

        @Override
        public void onerror(String str) {
            Toast.makeText(getContext(), "音频播放错误", Toast.LENGTH_LONG).show();
        }

        @Override
        public void complete() {

        }

        @Override
        public void interupt() {
            LogUtil.debug(TAG,"");
        }

    };
}
