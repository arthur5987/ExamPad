package com.xjhsk.exampad.ui.exam.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EmptyUtils;
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
import com.xjhsk.exampad.ui.exam.adapter.ESelectSortAdapter;
import com.xjhsk.exampad.ui.exam.adapter.ESelectTxtAdapter;
import com.xjhsk.exampad.ui.exam.contract.EHintContract;
import com.xjhsk.exampad.ui.exam.contract.ESortContract;
import com.xjhsk.exampad.ui.exam.presenter.EHintPresenter;
import com.xjhsk.exampad.ui.exam.presenter.ESortPresenter;
import com.xjhsk.exampad.utils.RxTimerUtil;
import com.xjhsk.exampad.widget.ProgressImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：weidingqiang on 2018/1/18 17:22
 * 邮箱：dqwei@iflytek.com
 */

public class ESortFragment extends RootFragment<ESortPresenter> implements ESortContract.View  {

    private static final String TAG = ESortFragment.class.getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.iv_progress)
    ProgressImageView iv_progress;

    @BindView(R.id.problem_title)
    TextView problem_title;

    @BindView(R.id.sort_tv)
    TextView sort_tv;

    @BindView(R.id.sort_clear_img)
    ImageView sort_clear_img;

    private AudioPlayManager _audioManager;// 音频播放管理

    private PaperSection paperSection;

    private ArrayList<PaperAction> allActionVOs;

    private String zipfilename;

    private ESelectSortAdapter eSelectSortAdapter;

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

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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

                    eSelectSortAdapter = new ESelectSortAdapter(getContext(),paperAction.getPaperActionOptions());
                    recyclerView.setAdapter(eSelectSortAdapter);

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
                default:
                    //如果没有匹配到则 不执行
                    allActionVOs.remove(paperAction);
                    return;
            }
        }

        doAction();

    }

    @Override
    public void selectItem(int postion) {
        if (!isSupportVisible())
        return;

        for (int i=0;i<eSelectSortAdapter.getPaperActionOptions().size();i++){
            PaperActionOption paperActionOption = eSelectSortAdapter.getPaperActionOptions().get(i);
            if(postion == i && !paperActionOption.isSelect()){
                paperActionOption.setSelect(true);
                if(EmptyUtils.isEmpty(answer)){
                    answer += paperActionOption.getKey();
                }
                else {
                    answer += "_"+paperActionOption.getKey();
                }
                sort_tv.setText(sort_tv.getText()+paperActionOption.getContent()+" ");
            }
        }

        eSelectSortAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.sort_clear_img)
    void onSortClear(){
        for (int i=0;i<eSelectSortAdapter.getPaperActionOptions().size();i++){
            PaperActionOption paperActionOption = eSelectSortAdapter.getPaperActionOptions().get(i);
            paperActionOption.setSelect(false);
        }
        answer = "";
        sort_tv.setText("");
        eSelectSortAdapter.notifyDataSetChanged();
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
        return R.layout.fragment_e_sort;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    public static ESortFragment newInstance(String zipfilename , PaperSection paperSection) {
        Bundle args = new Bundle();
        ESortFragment fragment = new ESortFragment();
        args.putParcelable("data",paperSection);
        args.putString("zipfilename",zipfilename);
        fragment.setArguments(args);
        return fragment;
    }


}
