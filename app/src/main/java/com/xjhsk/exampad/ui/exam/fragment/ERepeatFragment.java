package com.xjhsk.exampad.ui.exam.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.bumptech.glide.Glide;
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
import com.xjhsk.exampad.ui.exam.contract.ERepeatContract;
import com.xjhsk.exampad.ui.exam.presenter.EHintPresenter;
import com.xjhsk.exampad.ui.exam.presenter.ERepeatPresenter;
import com.xjhsk.exampad.utils.RxTimerUtil;
import com.xjhsk.exampad.widget.ProgressImageView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者：weidingqiang on 2018/1/18 17:22
 * 邮箱：dqwei@iflytek.com
 * 听后重复  "questionType": "2"
 */

public class ERepeatFragment extends RootFragment<ERepeatPresenter> implements ERepeatContract.View  {

    private static final String TAG = ERepeatFragment.class.getSimpleName();

    private AudioPlayManager _audioManager;// 音频播放管理

    private PaperSection paperSection;

    private ArrayList<PaperAction> allActionVOs;

    private String zipfilename;

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.iv_progress)
    ProgressImageView iv_progress;

    @BindView(R.id.problem_title)
    TextView problem_title;

    @BindView(R.id.problem_img)
    ImageView problem_img;

    @Override
    protected void initEventAndData() {
        //获取数据
        paperSection = getArguments().getParcelable("data");

        zipfilename = getArguments().getString("zipfilename");

        //把所有action放到数据中
        allActionVOs = new ArrayList<PaperAction>();
        allActionVOs.addAll(paperSection.getPaperActions());

        problem_img.setVisibility(View.INVISIBLE);
        iv_progress.setVisibility(View.INVISIBLE);

        doAction();
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
                case PaperAction.TITLE:
                    allActionVOs.remove(paperAction);
                    title_tv.setText(paperAction.getActionText());
                    break;
                case PaperAction.QUESTION_TITLE:
                    allActionVOs.remove(paperAction);
                    problem_title.setText(paperAction.getActionText());
                    break;
                case PaperAction.QUESTION_IMG:
                    allActionVOs.remove(paperAction);
                    problem_img.setVisibility(View.VISIBLE);
                    Glide.with(getContext())
                            .load(zipfilename+paperAction.getActionImgPath())
                            .into(problem_img);
                    break;
                case PaperAction.AUDIO:
                    //为了适配这个pad
                    RxTimerUtil.timer(1000, new RxTimerUtil.IRxNext() {
                        @Override
                        public void doNext(long number) {
                            playAudio(zipfilename+paperAction.getActionAudioPath());
                        }
                    });
                    allActionVOs.remove(paperAction);

                    iv_progress.setVisibility(View.VISIBLE);
                    iv_progress.start();

                    return;
                case PaperAction.WAIT:
                    //等待时间
                    allActionVOs.remove(paperAction);
                    RxBus.getDefault().post(new ActionEvent(ActionEvent.WAIT,Integer.valueOf(paperAction.getActionWaitSecond())));
                    return;
                case PaperAction.RECORD:
                    //等待时间
                    allActionVOs.remove(paperAction);
//                    RxBus.getDefault().post(new ActionEvent(ActionEvent.RECORD,Integer.valueOf(paperAction.getActionRecordSecond()),paperSection.getPaperSectionHeader().getQuestionNo()));//Integer.valueOf(paperAction.getActionRecordSecond())));
                    RxBus.getDefault().post(new ActionEvent(ActionEvent.RECORD,Integer.valueOf(paperAction.getActionRecordSecond()),paperSection.getPaperSectionHeader().getQuestionNo()));//Integer.valueOf(paperAction.getActionRecordSecond())));
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
            iv_progress.setVisibility(View.INVISIBLE);
            doAction();
        }

        @Override
        public void interupt() {
            LogUtil.debug(TAG,"");
        }

    };

    @Override
    public void progessFinish() {
        doAction();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_e_repeat;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    public static ERepeatFragment newInstance(String zipfilename ,PaperSection paperSection) {
        Bundle args = new Bundle();

        ERepeatFragment fragment = new ERepeatFragment();
        args.putParcelable("data",paperSection);
        args.putString("zipfilename",zipfilename);
        fragment.setArguments(args);
        return fragment;
    }
}
