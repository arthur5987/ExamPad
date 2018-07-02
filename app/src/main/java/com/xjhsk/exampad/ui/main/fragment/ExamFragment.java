package com.xjhsk.exampad.ui.main.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fifedu.record.media.record.RecordSettingAdapter;
import com.fifedu.record.recinbox.bl.dal.AacFileWriter;
import com.fifedu.record.recinbox.bl.dal.SpeexFileWriter;
import com.fifedu.record.recinbox.bl.record.IRecorderListener;
import com.fifedu.record.recinbox.bl.record.RecordParams;
import com.fifedu.record.recinbox.bl.record.RecorderManager;
import com.weidingqiang.rxfiflibrary2.app.AppConstants;
import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.R;
import com.xjhsk.exampad.app.AppContext;
import com.xjhsk.exampad.base.RootFragment;
import com.xjhsk.exampad.model.bean.AnswerVO;
import com.xjhsk.exampad.model.bean.PaperSection;
import com.xjhsk.exampad.model.bean.PaperSectionHeader;
import com.xjhsk.exampad.model.bean.PaperVO;
import com.xjhsk.exampad.ui.exam.fragment.EEndFragment;
import com.xjhsk.exampad.ui.exam.fragment.EHintFragment;
import com.xjhsk.exampad.ui.exam.fragment.EJudgePicFragment;
import com.xjhsk.exampad.ui.exam.fragment.ERepeatFragment;
import com.xjhsk.exampad.ui.exam.fragment.ESelectPicFragment;
import com.xjhsk.exampad.ui.exam.fragment.ESelectTxtFragment;
import com.xjhsk.exampad.ui.exam.fragment.ESortFragment;
import com.xjhsk.exampad.ui.exam.fragment.EWelcomeFragment;
import com.xjhsk.exampad.ui.main.contract.ExamContract;
import com.xjhsk.exampad.ui.main.presenter.ExamPresenter;
import com.xjhsk.exampad.ui.testsound.fragment.RecordFragment;
import com.xjhsk.exampad.widget.NoTouchViewPager;
import com.xjhsk.exampad.widget.WaveView;
import com.xjhsk.exampad.widget.topbar.XTopBar;
import com.zhy.autolayout.AutoLinearLayout;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

/**
 * 作者：weidingqiang on 2018/1/18 14:49
 * 邮箱：dqwei@iflytek.com
 *
 */

public class ExamFragment extends RootFragment<ExamPresenter> implements ExamContract.View ,IRecorderListener {

    private static final int MSG_START_RECORD = 10;
    private static final int MSG_STOP_RECORD = 11;
    private static final int MSG_VOLUME = 12;

    private static final int MSG_START_OK = 13;
    private static final int MSG_START_ERROR = 14;
    private static final int MSG_FINISH = 15;

    private double MAX_VOL = 20000;

    private static final String TAG = ExamFragment.class.getSimpleName();

    @BindView(R.id.x_topbar)
    XTopBar x_topbar;

    @BindView(R.id.la_viewPager)
    NoTouchViewPager viewPager;

    private PaperVO paperVO;

    //fragment 数组
    private ArrayList<Fragment> fragments;

    // 录音控件
    private RecorderManager mRecorder;
    private Handler mRecordHandler;

    // aac音频文件
    private AacFileWriter mAacFile;
    // speex 音频文件
    private SpeexFileWriter mSpeexFile;
    //用户文件夹
    private String userDir;
    //录音题号
    private String problemNO;

    @BindView(R.id.waveview)
    WaveView waveview;

    @BindView(R.id.record_view)
    AutoLinearLayout record_view;

    @Override
    protected void initEventAndData() {

        //1.需要创建录音文件夹
        userDir = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "xjhk"
                + File.separator + TimeUtils.millis2String(AppContext.getInstance().getUserVO().getTime_stamp(),"yyyy-MM-dd HH:mm")
                + File.separator + AppContext.getInstance().getUserVO().getExam_no();
        FileUtils.createOrExistsDir(userDir);

        paperVO = getArguments().getParcelable("paperVO");

        // 获取录音组件实例
        mRecorder = RecorderManager.getInstance();
        mRecordHandler = new ExamFragment.RecordHandler(this);

        x_topbar.hideTimeLayout();

        fragments = new ArrayList<Fragment>();

        // 每个类型 只要一个题型
        // *******************
//        boolean a0= false;boolean a1= false;boolean a2= false;
//        boolean a3= false;boolean a4= false;boolean a5= false;boolean a6= false;
        // *******************

        for (int i=0;i<paperVO.getPaperSections().size();i++){

            PaperSection paperSection = paperVO.getPaperSections().get(i);
            PaperSectionHeader paperSectionHeader = paperSection.getPaperSectionHeader();

            switch (paperSectionHeader.getQuestionType()){
                case "0":
//                    // *******************
//                    if(a0)
//                    {
//                        break;
//                    }
//                    a0 = true;
//                    // *******************
                    //题目 提示 欢迎
                    fragments.add(EWelcomeFragment.newInstance(paperVO.getZipfilename(),paperSection));
                    break;
                case "1":
//                    // *******************
//                    if(a1)
//                    {
//                        break;
//                    }
//                    a1 = true;
//                    // *******************
                    //题目 提示 欢迎
                    fragments.add(EHintFragment.newInstance(paperVO.getZipfilename(),paperSection));
                    break;
                case "2":
//                    // *******************
//                    if(a2)
//                    {
//                        break;
//                    }
//                    a2 = true;
//                    // *******************
                    //重复
                    fragments.add(ERepeatFragment.newInstance(paperVO.getZipfilename(),paperSection));
                    break;
                case "3":
//                    // *******************
//                    if(a3)
//                    {
//                        break;
//                    }
//                    a3 = true;
//                    // *******************
                    //选择图片
                    fragments.add(ESelectPicFragment.newInstance(paperVO.getZipfilename(),paperSection));
                    break;
                case "4":
//                    // *******************
//                    if(a4)
//                    {
//                        break;
//                    }
//                    a4 = true;
//                    // *******************
                    //选择文字
                    fragments.add(ESelectTxtFragment.newInstance(paperVO.getZipfilename(),paperSection));
                    break;
                case "5":
//                    // *******************
//                    if(a5)
//                    {
//                        break;
//                    }
//                    a5 = true;
//                    // *******************
                    //看图和表述判断表述的内容是否正确
                    fragments.add(EJudgePicFragment.newInstance(paperVO.getZipfilename(),paperSection));
                    break;
                case "6":
                    // *******************
//                    if(a6)
//                    {
//                        break;
//                    }
//                    a6 = true;
                    // *******************
                    //排序题
                    fragments.add(ESortFragment.newInstance(paperVO.getZipfilename(),paperSection));
                    break;

            }
        }

        //添加上传
        fragments.add(EEndFragment.newInstance(userDir,paperVO.getPagerInfo()));

        //初始化
        viewPager.setNoScroll(true);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getFragmentManager(), fragments));

        //11.开始答题
        mPresenter.setStatus("120");

//        mPresenter.saveExamStep("0");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_exam;
    }

    @Override
    public void saveAnswer(AnswerVO answerVO) {
        AppContext.getInstance().getAnswerVOS().add(answerVO);
        nextPage();
    }

    @Override
    public void nextPage() {
        if(viewPager.getCurrentItem() != fragments.size()-1)
        {
            int step = viewPager.getCurrentItem()+1;

//            mPresenter.saveExamStep(String.valueOf(step));
            //自己添加了一个end页面
            try {
                String stepStr = paperVO.getPagerInfo().getPagerStep().get(step);
                mPresenter.saveExamStep(stepStr);
            }catch (Exception ex){

            }
//            if(EmptyUtils.isNotEmpty(dialog_btn_play)){
//                dialog_btn_play.setImageResource(R.drawable.btn_play_stop_now);
//                isPause = false;
//            }
            //最后一页不能 下一步
            viewPager.setCurrentItem(step);
        }
    }

    @Override
    public void waitSecond(int second) {
        x_topbar.init();
        x_topbar.setTotal(second);
        x_topbar.start();
    }

    @Override
    public void recordSecond(int second, String problemNO) {
        this.problemNO = problemNO;
        waitSecond(second);

        waveview.set_recordlength(second);
        record_view.setVisibility(View.VISIBLE);

        //开启录音
        mRecordHandler.sendEmptyMessage(MSG_START_RECORD);
    }

    @Override
    public void progessFinish() {
        mRecordHandler.sendEmptyMessage(MSG_STOP_RECORD);
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    public static ExamFragment newInstance(PaperVO paperVO) {
        Bundle args = new Bundle();

        ExamFragment fragment = new ExamFragment();
        args.putParcelable("paperVO",paperVO);
        fragment.setArguments(args);
        return fragment;
    }

    class MyFragmentPagerAdapter<E extends Fragment> extends FragmentPagerAdapter {

        // 要显示的页面
        private List<E> frgList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<E> frgList) {
            super(fm);
            this.frgList = frgList;
        }

        @Override
        public E getItem(int arg0) {
            return frgList.get(arg0);
        }

        @Override
        public int getCount() {
            return frgList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }

    //******************************************************************************************************//

    @Override
    public void setStatusSuccess() {

    }

    @Override
    public void responceError() {

    }

    @Override
    public void saveExamStepSuccess() {

    }

    //****************************************************************************************************//
    //录音部分

    public static class RecordHandler extends Handler{
        private ExamFragment mForm;

        public RecordHandler(ExamFragment activity) {
            mForm = activity;
        }
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case MSG_START_RECORD:
                    mForm.onStartRecord();
                    break;
                case MSG_STOP_RECORD:
                    mForm.onStopRecord("录音停止", true);
                    break;
                case MSG_VOLUME:
                    mForm.onVolumeUi(msg.arg1);
                    break;
                case MSG_FINISH:
                    mForm.onMsgFinish();
                    break;
                case MSG_START_ERROR:
                    mForm.onMsgError(msg.arg1);
                    break;
                case MSG_START_OK:
                    mForm.onMsgStart((RecordParams) msg.obj);
                    break;
            }
        }
    }

    /**
     * 录音开启
     */
    private void onStartRecord() {
        // 创建Recorder
        if (!mRecorder.isRecording()) {
            RecordParams params = new RecordParams();
            mRecorder.startRecord(this, params, RecordSettingAdapter.getInstance());
        }
    }
    /**
     * 停止录音
     * @param msg
     * @param isClick
     */
    private void onStopRecord(String msg, boolean isClick) {
        if (isClick) {
            mRecorder.stopRecord(this);
        } else {
            mRecorder.stopRecord(null);
        }
    }
    /**
     * 音量变化
     * @param volume
     */
    private void onVolumeUi(int volume) {
        double rate = volume / MAX_VOL;
        if (rate > 1) {
            rate = 1;
        }
//        LayoutParams lp = (LayoutParams) recordImgBg.getLayoutParams();
//        LayoutParams lpCopy = (LayoutParams) recordImgBgCopy.getLayoutParams();
//        lp.height = (int) (lpCopy.height * rate);
//        recordImgBg.setLayoutParams(lp);

        waveview.setHeightRect(rate);
    }
    /**
     * 录音结束
     */
    public void onMsgFinish() {
        record_view.setVisibility(View.GONE);
        waveview.clear();
    }

    public void onMsgError(int arg1) {
        LogUtil.debug(TAG, "onMsgError", "---->onMsgError");
        onStopRecord("开始录音出错", false);
        ToastUtils.showShortToast("录音发生错误");

        //102 硬件原因试音失败
        mPresenter.setStatus("102");
    }

    public void onMsgStart(RecordParams params) {
//        mBeginTime = System.currentTimeMillis();
    }


    @Override
    public boolean onStart(RecordParams params) {
        Message msg = mRecordHandler.obtainMessage(MSG_START_OK);
        try {
            if (null != mAacFile) {
                mAacFile.close();
            }
            mAacFile = new AacFileWriter();
//            String file = getAudioFile();
            String file = userDir + File.separator + problemNO+ AppConstants.AUDIO_FILE_SUFFIX;
            mAacFile.open(file);
            mAacFile.init(params.getSampleRate());

            if (null != mSpeexFile) {
                mSpeexFile.close();
            }

            params.setFileId(file);
            msg.obj = params;
            mRecordHandler.sendMessage(msg);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public void onError(RecordParams params, int errorCode) {
        if (null != mAacFile) {
            try {
                mAacFile.close();
            } catch (IOException e) {
                LogUtil.debug("", "", e.toString());
            }
            mAacFile = null;
        }

        if (null != mSpeexFile) {
            try {
                mSpeexFile.close();
            } catch (IOException e) {
                LogUtil.debug("", "", e.toString());
            }
            mSpeexFile = null;
        }
        Message msg = mRecordHandler.obtainMessage(MSG_START_ERROR);
        msg.arg1 = errorCode;
        mRecordHandler.sendMessage(msg);
    }
    @Override
    public int onRecordData(byte[] data) {
        if (null != mAacFile) {
            mAacFile.appendPcmData(data, data.length);
        }
        if(null != mSpeexFile)
        {
            mSpeexFile.appendPcmData(data, data.length, false);
        }

        //add weidingqiang 将byte[] 转成 short[]  类型
        short[] shorts = new short[data.length/2];
        ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
        //进行显示
//        waveformView.setSamples(shorts);

        Message msg = mRecordHandler.obtainMessage(MSG_VOLUME);
        msg.arg1 = getDataVolume(data);
        mRecordHandler.sendMessage(msg);
        return data.length;
    }
    @Override
    public void onRecordInterrupt() {

    }
    @Override
    public void onFinished(RecordParams params) {
        if (null != mAacFile) {
            try {
                mAacFile.close();
            } catch (IOException e) {
                LogUtil.debug("", "", e.toString());
            }
            mAacFile = null;
        }
        if (null != mSpeexFile) {
            try {
                // 结束录音
                byte[] data = {};
                mSpeexFile.appendPcmData(data, 0, true);
                mSpeexFile.close();
            } catch (IOException e) {
                LogUtil.debug("", "", e.toString());
            }
            mSpeexFile = null;
        }
        Message msg = mRecordHandler.obtainMessage(MSG_FINISH);
        mRecordHandler.sendMessage(msg);
    }

    private int getDataVolume(byte[] data) {
        // 计算最大值
        short tmpMax = 0;
        for (int i = 0; i < data.length - 1; i += 2) {
            short value = (short) ((data[i + 1] << 8) + data[i]);
            if (tmpMax < value) {
                tmpMax = value;
            }
        }
        return tmpMax;
    }


    //************************************************************************************************//
}
