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
import com.xjhsk.exampad.model.bean.AnswerVO;
import com.xjhsk.exampad.model.bean.PaperAction;
import com.xjhsk.exampad.model.bean.PaperSection;
import com.xjhsk.exampad.model.event.ActionEvent;
import com.xjhsk.exampad.ui.exam.contract.EJudgePicContract;
import com.xjhsk.exampad.ui.exam.contract.ESelectPicContract;
import com.xjhsk.exampad.ui.exam.presenter.EJudgePicPresenter;
import com.xjhsk.exampad.ui.exam.presenter.ESelectPicPresenter;
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
 * 听后重复  "questionType": "5"
 */

public class EJudgePicFragment extends RootFragment<EJudgePicPresenter> implements EJudgePicContract.View  {

    private static final String TAG = EJudgePicFragment.class.getSimpleName();

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

    @BindView(R.id.txt_layout)
    AutoLinearLayout txt_layout;

    @BindView(R.id.true_img)
    ImageView true_img;

    @BindView(R.id.false_img)
    ImageView false_img;

    private String answer = "";

    private String questionTitle = "";

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
            RxBus.getDefault().post(new ActionEvent(ActionEvent.NEXT,new AnswerVO(answer,paperSection.getPaperSectionHeader().getQuestionNo())));
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
                    questionTitle = paperAction.getActionText();
                    problem_title.setText(paperAction.getActionText());
                    break;
                case PaperAction.QUESTION_IMG:
                    allActionVOs.remove(paperAction);
                    problem_img.setVisibility(View.VISIBLE);
                    Glide.with(getContext())
                            .load(zipfilename+paperAction.getActionImgPath())
                            .into(problem_img);
//                    Glide.with(getContext())
//                            .load(zipfilename+paperAction.getActionAudioPath())
//                            .into(problem_img);
                    break;
                case PaperAction.QUESTION_TXT:
                    ETextView eTextView = new ETextView(getContext());
                    eTextView.setData(paperAction.getActionText());
                    txt_layout.addView(eTextView);
                    allActionVOs.remove(paperAction);
                    break;
                case PaperAction.QUESTION_OPTION:
                    allActionVOs.remove(paperAction);
                    break;
                case PaperAction.AUDIO:
                    //为了适配这个pad
                    RxTimerUtil.timer(2000, new RxTimerUtil.IRxNext() {
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
                default:
                    //如果没有匹配到则 不执行
                    allActionVOs.remove(paperAction);
                    return;
            }
        }

        doAction();

    }

    @OnClick(R.id.false_img)
    void onFalseImage(){
        answer = "F";
        problem_title.setText(questionTitle + "  （ 错误 ）");
        false_img.setSelected(true);
        true_img.setSelected(false);
    }

    @OnClick(R.id.true_img)
    void onTrueImage(){
        answer = "T";
        problem_title.setText(questionTitle + "  （ 正确 ）");
        false_img.setSelected(false);
        true_img.setSelected(true);
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
            iv_progress.stop();
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
        return R.layout.fragment_e_judge_pic;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    public static EJudgePicFragment newInstance(String zipfilename ,PaperSection paperSection) {
        Bundle args = new Bundle();

        EJudgePicFragment fragment = new EJudgePicFragment();
        args.putParcelable("data",paperSection);
        args.putString("zipfilename",zipfilename);
        fragment.setArguments(args);
        return fragment;
    }
}
