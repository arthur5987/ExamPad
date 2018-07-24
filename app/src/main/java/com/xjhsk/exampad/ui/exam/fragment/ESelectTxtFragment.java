package com.xjhsk.exampad.ui.exam.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.xjhsk.exampad.model.bean.AnswerVO;
import com.xjhsk.exampad.model.bean.PaperAction;
import com.xjhsk.exampad.model.bean.PaperActionOption;
import com.xjhsk.exampad.model.bean.PaperSection;
import com.xjhsk.exampad.model.bean.StringAnswer;
import com.xjhsk.exampad.model.event.ActionEvent;
import com.xjhsk.exampad.ui.exam.adapter.ESelectPicAdapter;
import com.xjhsk.exampad.ui.exam.adapter.ESelectTxtAdapter;
import com.xjhsk.exampad.ui.exam.contract.ERepeatContract;
import com.xjhsk.exampad.ui.exam.contract.ESelectTxtContract;
import com.xjhsk.exampad.ui.exam.presenter.ERepeatPresenter;
import com.xjhsk.exampad.ui.exam.presenter.ESelectPicPresenter;
import com.xjhsk.exampad.ui.exam.presenter.ESelectTxtPresenter;
import com.xjhsk.exampad.utils.RxTimerUtil;
import com.xjhsk.exampad.widget.ProgressImageView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者：weidingqiang on 2018/1/18 17:22
 * 邮箱：dqwei@iflytek.com
 * 听后重复  "questionType": "4"
 */

public class ESelectTxtFragment extends RootFragment<ESelectTxtPresenter> implements ESelectTxtContract.View  {

    private static final String TAG = ESelectTxtFragment.class.getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.iv_progress)
    ProgressImageView iv_progress;

    @BindView(R.id.problem_title)
    TextView problem_title;

    private AudioPlayManager _audioManager;// 音频播放管理

    private PaperSection paperSection;

    private ArrayList<PaperAction> allActionVOs;

    private String zipfilename;

    private ESelectTxtAdapter eSelectTxtAdapter;

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

        GridLayoutManager mgr = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mgr);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        iv_progress.setVisibility(View.INVISIBLE);

        doAction();
    }

    public void doAction(){
        if (!isSupportVisible())
            return;

        if(allActionVOs.size() == 0)
        {
            // ******************** //
            //录音的地方开始答题，开始拼字符串
            StringAnswer data = AppContext.getInstance().getStringAnswer();
            if (data.getStuAnswer() == null){
                data.setStuAnswer("");
            }
            String stuAnswer = data.getStuAnswer() + answer +"#";
            data.setStuAnswer(stuAnswer);
            AppContext.getInstance().saveStringAnswer(data);
            // ******************** //

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
                case PaperAction.QUESTION_OPTION:
                    allActionVOs.remove(paperAction);

                    eSelectTxtAdapter = new ESelectTxtAdapter(getContext(),paperAction.getPaperActionOptions());
                    recyclerView.setAdapter(eSelectTxtAdapter);

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
                    break;
            }
        }

        doAction();

    }

    @Override
    public void selectItem(int postion) {
        if (!isSupportVisible())
            return;

        for (int i=0;i<eSelectTxtAdapter.getPaperActionOptions().size();i++){
            PaperActionOption paperActionOption = eSelectTxtAdapter.getPaperActionOptions().get(i);
            if(postion == i){
                paperActionOption.setSelect(true);
                answer = paperActionOption.getKey();
            }
            else {
                paperActionOption.setSelect(false);
            }
        }

        eSelectTxtAdapter.notifyDataSetChanged();
        problem_title.setText(questionTitle + "  （ "+   answer +   " ）");
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
        return R.layout.fragment_e_select_txt;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    public static ESelectTxtFragment newInstance(String zipfilename ,PaperSection paperSection) {
        Bundle args = new Bundle();

        ESelectTxtFragment fragment = new ESelectTxtFragment();
        args.putParcelable("data",paperSection);
        args.putString("zipfilename",zipfilename);
        fragment.setArguments(args);
        return fragment;
    }
}
