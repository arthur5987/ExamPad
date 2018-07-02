package com.xjhsk.exampad.ui.exam.fragment;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.fifedu.record.media.record.AudioPlayManager;
import com.fifedu.record.media.record.AudioplayInterface;
import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.R;
import com.xjhsk.exampad.base.RootFragment;
import com.xjhsk.exampad.base.RxBus;
import com.xjhsk.exampad.model.bean.PaperAction;
import com.xjhsk.exampad.model.bean.PaperSection;
import com.xjhsk.exampad.model.event.ActionEvent;
import com.xjhsk.exampad.ui.exam.contract.EHintContract;
import com.xjhsk.exampad.ui.exam.contract.EWelcomeContract;
import com.xjhsk.exampad.ui.exam.presenter.EHintPresenter;
import com.xjhsk.exampad.ui.exam.presenter.EWelcomePresenter;
import com.xjhsk.exampad.utils.RxTimerUtil;
import com.xjhsk.exampad.widget.ETextView;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者：weidingqiang on 2018/1/18 17:22
 * 邮箱：dqwei@iflytek.com
 */

public class EHintFragment extends RootFragment<EHintPresenter> implements EHintContract.View  {

    private static final String TAG = EHintFragment.class.getSimpleName();

    private AudioPlayManager _audioManager;// 音频播放管理

    private PaperSection paperSection;

    private ArrayList<PaperAction> allActionVOs;

    private String zipfilename;

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.txt_layout)
    AutoLinearLayout txt_layout;

    @Override
    protected void initEventAndData() {

        //获取数据
        paperSection = getArguments().getParcelable("data");

        zipfilename = getArguments().getString("zipfilename");

        //把所有action放到数据中
        allActionVOs = new ArrayList<PaperAction>();
        allActionVOs.addAll(paperSection.getPaperActions());

        doAction();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_e_hint;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    public static EHintFragment newInstance(String zipfilename ,PaperSection paperSection) {
        Bundle args = new Bundle();
        EHintFragment fragment = new EHintFragment();
        args.putParcelable("data",paperSection);
        args.putString("zipfilename",zipfilename);
        fragment.setArguments(args);
        return fragment;
    }

    public void doAction(){
        if (!isSupportVisible())
            return;

        if(allActionVOs.size() == 0)
        {
            //做题的最后一个fragment 需要上传相关数据
            //发送事件下一页
            RxBus.getDefault().post(new ActionEvent(ActionEvent.NEXT));
            return;
        }

        for (int i=0;i<allActionVOs.size();i++){

            final PaperAction paperAction = allActionVOs.get(0);

            switch (paperAction.getActionType()){
                case PaperAction.TITLE1:
                    allActionVOs.remove(paperAction);
                    title_tv.setText(paperAction.getActionText());
                    break;
                case PaperAction.TXT:
                    ETextView eTextView = new ETextView(getContext());
                    eTextView.setData(paperAction.getActionText());
                    txt_layout.addView(eTextView);
                    allActionVOs.remove(paperAction);
                    break;
                case PaperAction.AUDIO:
                    allActionVOs.remove(paperAction);
                    //为了适配这个pad
                    RxTimerUtil.timer(1000, new RxTimerUtil.IRxNext() {
                        @Override
                        public void doNext(long number) {
                            playAudio(zipfilename+paperAction.getActionAudioPath());
                        }
                    });
                    return;
                default:
                    //如果没有匹配到则 不执行
                    allActionVOs.remove(paperAction);
                    break;
            }
        }

        doAction();

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
            doAction();
        }

        @Override
        public void interupt() {
            LogUtil.debug(TAG,"");
        }

    };
}
