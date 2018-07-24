package com.xjhsk.exampad.ui.testsound.fragment_stand_alone;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.fifedu.record.media.record.AudioPlayManager;
import com.fifedu.record.media.record.AudioplayInterface;
import com.fifedu.record.media.record.IflyAudioPlay;
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
import com.xjhsk.exampad.base.RxBus;
import com.xjhsk.exampad.model.bean.PaperVO;
import com.xjhsk.exampad.model.event.CloseEvent;
import com.xjhsk.exampad.socket.SocketManager;
import com.xjhsk.exampad.ui.main.activity.MainActivity;
import com.xjhsk.exampad.ui.testsound.contract.RecordContract;
import com.xjhsk.exampad.ui.testsound.presenter.RecordPresenter;
import com.xjhsk.exampad.ui.testsound.presenter_stand_alone.RecordPresenter_SA;
import com.xjhsk.exampad.widget.ProgressImageView;
import com.xjhsk.exampad.widget.WaveView;
import com.xjhsk.exampad.widget.topbar.XTopBar;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：weidingqiang on 2018/1/17 11:11
 * 邮箱：dqwei@iflytek.com
 *
 * 流程为
 1.播放 RTStart
 2.点击开始button
 3.播放STinit
 4.播放 RecordTip
 5.录音
 6.发生错误 （相关提示）
 STVadDevError  没有音频
 STVadDevOther  噪声太大
 STVadNoSpeech  声音太小
 STVadOverflow  声音太大
 7.播放STRestart
 8.播放STInit
 9.播放 RecordTip
 10.录音
 11.成功后播放STPlay
 12.播放STConfirm
 13.播放STEnd
 */

public class RecordFragment_SA extends RootFragment<RecordPresenter_SA> implements RecordContract.View,IRecorderListener {

    private static final int MSG_START_RECORD = 10;
    private static final int MSG_STOP_RECORD = 11;
    private static final int MSG_VOLUME = 12;

    private static final int MSG_START_OK = 13;
    private static final int MSG_START_ERROR = 14;
    private static final int MSG_FINISH = 15;

    //音频文件
    private static final String STStart0 = "STStart0";
    private static final String STinit1 = "STinit1";
    private static final String RecordTip1 = "RecordTip1";
    private static final String Record = "record";
    private static final String PLAYRECORD = "playRecord";
    private static final String STRestart2 = "STRestart2";
    private static final String STInit2 = "STInit2";
    private static final String RecordTip2 = "RecordTip2";
    private static final String STPlay = "STPlay";
    private static final String STConfirm = "STConfirm";
    private static final String STEnd = "STEnd";

    private static final String TAG = RecordFragment_SA.class.getSimpleName();

    private double MAX_VOL = 20000;

    @BindView(R.id.x_topbar)
    XTopBar x_topbar;

    @BindView(R.id.start_record_img)
    ImageView start_record_img;

    @BindView(R.id.clear_layout)
    AutoLinearLayout clear_layout;

    @BindView(R.id.record_clear_img)
    ImageView record_clear_img;

    @BindView(R.id.record_unclear_img)
    ImageView record_unclear_img;

    @BindView(R.id.iv_progress)
    ProgressImageView iv_progress;

    @BindView(R.id.record_layout)
    AutoLinearLayout record_layout;

    @BindView(R.id.wait_layout)
    AutoRelativeLayout wait_layout;

    @BindView(R.id.waveview)
    WaveView waveview;

    @BindView(R.id.record_view)
    AutoLinearLayout record_view;

    @BindView(R.id.wait_tv)
    TextView wait_tv;

    // 录音控件
    private RecorderManager mRecorder;
    private Handler mRecordHandler;

    // aac音频文件
    private AacFileWriter mAacFile;
    // speex 音频文件
    private SpeexFileWriter mSpeexFile;

    private String fileName;

    private long mBeginTime;// 录制开始时间

    private String recordUrl = "";

    private AssetManager am;

    private AudioPlayManager _audioManager;// 音频播放管理
    //当前状态
    private String current;

    private String zipFile;

    private PaperVO paperVO;

    //下载的重新连接次数
    private int errorConnectNum = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_record;
    }

    @Override
    protected void initEventAndData() {

        am = getContext().getAssets();

        x_topbar.hideTimeLayout();

        LogUtil.debug(TAG,"停止所有录音和播音");
        IflyAudioPlay.getInstansce(getContext()).stopPlay();
        AudioPlayManager.getManager().stop();

        // 获取录音组件实例
        mRecorder = RecorderManager.getInstance();
        mRecordHandler = new RecordHandler(this);

        zipFile = AppContext.getInstance().getUserVO().getPaper_name();//"xhk_1_001.zip";//xhk_2_001.zip    xhk_3_001.zip
//        zipFile = "xhk_1_001.zip";//xhk_2_001.zip    xhk_3_001.zip


        //*********************************************//  为去掉试音修改的部分
//        //准备开始试音
//        LogUtil.debug(TAG,"发送准备开始试音请求 100");
//        mPresenter.setStatus("100");
//
//        //1.进入后播放RTStart0
//        playTipSound(STStart0);

        playTipSound(STEnd);                    // 暂时去掉试音的步骤（只修改了这一段）
        clear_layout.setVisibility(View.INVISIBLE);

        //*********************************************//
    }

    private void playTipSound(String current){
        this.current = current;
        AssetFileDescriptor assetFileDescriptor;

        try{
            switch (current){
                //正常流程
                case STStart0:
                    LogUtil.debug(TAG,"播放STStart音频  现在开始话筒和耳机测试");
                    assetFileDescriptor = am.openFd("STStart.wav");
                    playAudio(assetFileDescriptor);
                    break;
                case STinit1:
                    LogUtil.debug(TAG,"播放STInit音频  现在开始录音测试");
                    assetFileDescriptor = am.openFd("STInit.wav");
                    playAudio(assetFileDescriptor);
                    break;
                case RecordTip1:
                    LogUtil.debug(TAG,"播放RecordTip音频  开始录音");
                    assetFileDescriptor = am.openFd("RecordTip.wav");
                    playAudio(assetFileDescriptor);
                    break;
                case STPlay:
                    LogUtil.debug(TAG,"播放STPlay音频  播放刚才的录音");
                    assetFileDescriptor = am.openFd("STPlay.wav");
                    playAudio(assetFileDescriptor);
                    break;
                case STConfirm:
                    LogUtil.debug(TAG,"播放STConfirm音频  录音是否清晰完整");
                    assetFileDescriptor = am.openFd("STConfirm.wav");
                    playAudio(assetFileDescriptor);
                    break;
                case STEnd:
                    LogUtil.debug(TAG,"播放STEnd音频  测试完毕");
                    assetFileDescriptor = am.openFd("STEnd.wav");
                    playAudio(assetFileDescriptor);
                    break;
                //第二次流程
                case STRestart2:
                    LogUtil.debug(TAG,"播放STRestart音频  调整话筒耳机 再次开始");
                    assetFileDescriptor = am.openFd("STRestart.wav");
                    playAudio(assetFileDescriptor);
            }


        }catch (IOException ex){
            LogUtil.debug(TAG,"没找到音频文件"+current);
        }
    }

    private void onSoundComplete(){
        switch (current){
            case STStart0:
                //显示开始试音按钮
                start_record_img.setVisibility(View.VISIBLE);
                break;
            case STinit1:
                playTipSound(RecordTip1);
                break;
            case RecordTip1:
                //开启录音
                current = Record;
                mRecordHandler.sendEmptyMessage(MSG_START_RECORD);
                waitSecond(20);
                waveview.set_recordlength(20);
                record_view.setVisibility(View.VISIBLE);
                break;
            case Record:
                playTipSound(STPlay);
                break;
            case STPlay:
                //播放自己的录音
                current = PLAYRECORD;
                playAudio(recordUrl);
                break;
            case PLAYRECORD:
                //需要显示 清楚和不清楚的按钮
                playTipSound(STConfirm);
                break;
            case STConfirm:
                clear_layout.setVisibility(View.VISIBLE);
//                playTipSound(STEnd);
                break;
            case STEnd:
                //101试音成功
                mPresenter.setStatus("101");

                record_layout.setVisibility(View.GONE);
                wait_layout.setVisibility(View.VISIBLE);
                iv_progress.start();
                //开启下载模式
                downloadZip(zipFile);

                break;
            case STRestart2:
                start_record_img.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void waitSecond(int second) {
        x_topbar.init();
        x_topbar.setTotal(second);
        x_topbar.start();
    }

    private void playAudio(AssetFileDescriptor assetFileDescriptor) {

        if (_audioManager == null)
            _audioManager = AudioPlayManager.getManager();

        _audioManager.startPlay(assetFileDescriptor, audioPlayListen, null);
    }

    @OnClick(R.id.start_record_img)
    void onStartRecordBut(){

        start_record_img.setVisibility(View.INVISIBLE);
        playTipSound(RecordTip1);


//        downloadZip(zipFile);
//        downloadZip(zipFile);
//        mPresenter.loadZipData(AppConstants.FILE_DOWNLOAD_PATH+zipFileName.split("\\.")[0]+File.separator);
    }

    @OnClick(R.id.record_clear_img)
    void onClearBut(){
        playTipSound(STEnd);
        clear_layout.setVisibility(View.INVISIBLE);

        record_view.setVisibility(View.GONE);
        waveview.clear();
    }

    @OnClick(R.id.record_unclear_img)
    void onUnclearBut(){
        playTipSound(STRestart2);
        clear_layout.setVisibility(View.INVISIBLE);

        record_view.setVisibility(View.GONE);
        waveview.clear();
    }

    @Override
    public void analyzeSuccess(PaperVO paperVO) {
        //数据解压成功
        this.paperVO = paperVO;

        wait_tv.setText("等待开始考试（解压成功）..");

        //隐藏转圈
        iv_progress.stop();
        iv_progress.setVisibility(View.GONE);

        //111下载试卷成功
        mPresenter.setStatus("111");

        //获取开考指令
        mPresenter.startExam("60");

//        startActivity(MainActivity.newInstance(getContext(),paperVO));
//        RxBus.getDefault().post(new CloseEvent(CloseEvent.CLOSE_TEST_SOUND));
    }

    @Override
    public void progessFinish() {
        mRecordHandler.sendEmptyMessage(MSG_STOP_RECORD);
    }

    //****************************************************************************************************//
    //录音部分

    public static class RecordHandler extends Handler{
        private RecordFragment_SA mForm;

        public RecordHandler(RecordFragment_SA activity) {
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
//        LogUtil.debug(TAG, "#####volume######  :  " + volume);
        // 话筒高70dp
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
        onSoundComplete();

    }

    public void onMsgError(int arg1) {
        LogUtil.debug(TAG, "onMsgError", "---->onMsgError");
        onStopRecord("开始录音出错", false);
        ToastUtils.showShortToast("录音发生错误");

        //102 硬件原因试音失败
        mPresenter.setStatus("102");
    }

    public void onMsgStart(RecordParams params) {
        mBeginTime = System.currentTimeMillis();
    }


    @Override
    public boolean onStart(RecordParams params) {
        Message msg = mRecordHandler.obtainMessage(MSG_START_OK);
        try {
            if (null != mAacFile) {
                mAacFile.close();
            }
            mAacFile = new AacFileWriter();
            String file = getAudioFile();
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
    //播放音频部分
    /**
     * 播放
     * @param audioname
     */
    private void playAudio(String audioname){

        if(_audioManager == null)
            _audioManager = AudioPlayManager.getManager();

        if (FileUtils.isFileExists(audioname)) {
            _audioManager.startPlay(audioname, audioname, audioPlayListen, null);
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
            onSoundComplete();
        }

        @Override
        public void interupt() {
        }
    };


    public void stopPlaying() {
        if(_audioManager!=null)
        {
            _audioManager.stop();
        }
    }

    private String getAudioFile() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("_yyyyMMdd_HHmmss", Locale.CHINESE);
        fileName = "record_" + simpleDateFormat.format(date) + AppConstants.AUDIO_FILE_SUFFIX;
        String ret = AppConstants.RECORD_DOWNLOAD_PATH+ fileName;
        recordUrl = ret;
        return ret;
    }

    @Override
    public void onStop() {
        AudioPlayManager.getManager().stop();
        iv_progress.stop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        LogUtil.debug(TAG,"onDestroy","关闭口语界面 TestSpeakActivity");
        mRecorder.destroy();
        super.onDestroy();
    }

    //************************************************************************************************//
    //下载模块
    //开始下载
    private void downloadZip(String zipFileName) {

        errorConnectNum++;

        //110准备下载试卷
        mPresenter.setStatus("110");

        //清空文件夹
        FileUtils.deleteFilesInDir(AppConstants.FILE_DOWNLOAD_PATH);

        //下载文件的位置
        String filePath = AppConstants.FILE_DOWNLOAD_PATH+zipFileName;
        //http://192.168.0.6:8080/

        //        String url = "http://"+AppContext.getInstance().getHostIp()+":8083/xhk/down/"+zipFileName;
        String url = "http://"+ AppContext.getInstance().getHostIp()+":8083/xhk/res/paper/"+zipFileName;

//        String url = "http://10.4.67.193/"+zipFileName;
//        http://{{host}}/down/xhk_1_001.zip

        try {
            FileUtils.writeFileFromIS(filePath,am.open("xhk_1_001.zip"),true);

            wait_tv.setText("检查试卷（解压)..");

            UnZipAsyncTask unZipAsyncTask = new UnZipAsyncTask(filePath);
            unZipAsyncTask.execute();

        }catch (IOException ex){

        }

        //复制文件
//        FileUtils.copyFile(,filePath);


//        //1.开始下载文件
//        ApiHttpManager.getManager().downloadAudioFile(url, filePath, new DownloadFileInterface() {
//
//            @Override
//            public void onSuccess(String filePath) {
//                //2.文件下载成功
//                //2.1开始解压文件
//                //unZip(filePath,AppConstants.UNFILE_DOWNLOAD_PATH);
//
//                wait_tv.setText("检查试卷（解压)..");
//
//                UnZipAsyncTask unZipAsyncTask = new UnZipAsyncTask(filePath);
//                unZipAsyncTask.execute();
//            }
//
//            @Override
//            public void onFailure(String des) {
//
//                if(errorConnectNum > 4){
//                    //3.文件下载失败
//                    Toast.makeText(getContext(), "网络连接超时", Toast.LENGTH_LONG).show();
//
//                    //112 下载试卷失败
//                    mPresenter.setStatus("112");
//                }
//                else {
//                    downloadZip(zipFile);
//                }
//            }
//
//            @Override
//            public void onProgress(long bytesWritten, long totalSize) {
////                downtextview.setText("正在下载 " + bytesWritten + " / " + totalSize);
////                arcProgress.setProgress((int) ((bytesWritten * 100) / totalSize));
//            }
//        });
    }

    private class UnZipAsyncTask extends AsyncTask<Void, Integer, Void> {
        private String filePath;

        public UnZipAsyncTask(String filePath) {
            this.filePath = filePath;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //执行完毕
//            downtextview.setText("解压完成，即将跳转");
            mPresenter.loadZipData(AppConstants.FILE_DOWNLOAD_PATH+zipFile.split("\\.")[0]+File.separator);
        }

        @Override
        protected Void doInBackground(Void... params) {
            //解压地址
            //unZip(filePath, AppConstants.FILE_DOWNLOAD_PATH+zipFile.split("\\.")[0]);//AppContext.getInstance().getUserVO().getExam_no()

//            decryptUnzip(filePath, AppConstants.FILE_DOWNLOAD_PATH+zipFile.split("\\.")[0]);

            try {
//                CompressUtils.unZip(new File(filePath),AppConstants.FILE_DOWNLOAD_PATH+zipFile.split("\\.")[0],CompressUtils.KEY_ZIP_PAPER);
                ZipUtils.unzipFile(filePath, AppConstants.FILE_DOWNLOAD_PATH+zipFile.split("\\.")[0]);
            }catch (Exception e){
                LogUtil.debug(TAG,"解压zip失败");
            }

            return null;
        }
    }

//    public  int decryptUnzip(String srcfile, String destfile){
//        File temp = new File(AppConstants.FILE_DOWNLOAD_PATH+ UUID.randomUUID().toString() + ".zip");
////        FileUtils.createOrExistsFile(temp);
//        int result=1;
////        temp.deleteOnExit();
//
//        try{
//            // 先对文件解密
//            XhkCrypto.decrypt(srcfile, temp.getAbsolutePath());
//        }catch(Exception e){
//            result=2;
//        }
//        try{
//            // 解压缩
////            unZip(temp.getAbsolutePath(),destfile);
//            ZipUtils.unzipFile(temp.getAbsolutePath(),destfile);
//        } catch(Exception e){
//            result=0;
//        }
//
//        temp.delete();
//        return result;
//    }
//
//    //开始解压文件
//    public void unZip(String zipFileName, String outputDirectory) {
//        try {
//            ZipFile zipFile = new ZipFile(zipFileName);
//            Enumeration e = zipFile.getEntries();
//            ZipEntry zipEntry = null;
//            createDirectory(outputDirectory, "");
//            while (e.hasMoreElements()) {
//                zipEntry = (ZipEntry) e.nextElement();
//                if (zipEntry.isDirectory()) {
//                    String name = zipEntry.getName();
//                    name = name.substring(0, name.length() - 1);
//                    File f = new File(outputDirectory + File.separator + name);
//                    f.mkdir();
//                } else {
//                    String fileName = zipEntry.getName();
//                    fileName = fileName.replace('\\', '/');
//                    if (fileName.indexOf("/") != -1) {
//                        createDirectory(outputDirectory, fileName.substring(0,
//                                fileName.lastIndexOf("/")));
//                        fileName = fileName.substring(
//                                fileName.lastIndexOf("/") + 1,
//                                fileName.length());
//                    }
//
//                    File f = new File(outputDirectory + File.separator
//                            + zipEntry.getName());
//
//                    f.createNewFile();
//                    InputStream in = zipFile.getInputStream(zipEntry);
//                    FileOutputStream out = new FileOutputStream(f);
//
//                    byte[] by = new byte[1024];
//                    int c;
//                    while ((c = in.read(by)) != -1) {
//                        out.write(by, 0, c);
//                    }
//                    out.close();
//                    in.close();
//                }
//            }
//        } catch (Exception e) {
//            if(errorConnectNum == 4){
//                // 解压出现异常
//                Toast.makeText(getContext(), "解压出现异常", Toast.LENGTH_LONG).show();
//
//                //112 下载试卷失败
//                mPresenter.setStatus("112");
//            }
//            else {
//                downloadZip(zipFile);
//            }
////            ToastUtil.showToastCustom(KyxlApplication.context, 0,
////                    "预览生成失败", "请检查网络后再试");
////            tv_load.setText("预览");
////            ap_load.setProgress(0);
////            ap_load.setBackgroundResource(R.drawable.bt_yulan2);
////            rl_load.setClickable(true);
//        }
//    }
//    private void createDirectory(String directory, String subDirectory) {
//        String dir[];
//        File fl = new File(directory);
//        try {
//            if (subDirectory == "" && fl.exists() != true)
//                fl.mkdir();
//            else if (subDirectory != "") {
//                dir = subDirectory.replace('\\', '/').split("/");
//                for (int i = 0; i < dir.length; i++) {
//                    File subFile = new File(directory + File.separator + dir[i]);
//                    if (subFile.exists() == false)
//                        subFile.mkdir();
//                    directory += File.separator + dir[i];
//                }
//            }
//        } catch (Exception ex) {
//            if(errorConnectNum == 4){
//                // 解压出现异常
//                Toast.makeText(getContext(), "创建文件夹出现异常", Toast.LENGTH_LONG).show();
//
//                //112 下载试卷失败
//                mPresenter.setStatus("112");
//            }
//            else {
//                downloadZip(zipFile);
//            }
//
////            ToastUtil.showToastCustom(KyxlApplication.context, 0,
////                    "预览生成失败", "请检查网络后再试");
////            tv_load.setText("预览");
////            ap_load.setProgress(0);
////            ap_load.setBackgroundResource(R.drawable.bt_yulan2);
////            rl_load.setClickable(true);
//            return;
//        }
//    }

    //************************************************************************************************//
    @Override
    public void startExamSuccess() {
        //直接开始做题
        startActivity(MainActivity.newInstance(getContext(),paperVO));
        RxBus.getDefault().post(new CloseEvent(CloseEvent.CLOSE_TEST_SOUND));
    }

    @Override
    public void setStatusSuccess() {

    }

    @Override
    public void responceError(String message) {
        ToastUtils.showShortToast(message);
        //发送socket指令
        AppContext.getInstance().getSocketManager().sendMessage(SocketManager.ReadyExam);
    }

    @Override
    public void startExam() {
//        int random= (int)(1+Math.random()*5);
//
//        RxTimerUtil.timer(1000 * random, new RxTimerUtil.IRxNext() {
//            @Override
//            public void doNext(long number) {
//
//            }
//        });

        startExamSuccess();
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    public static RecordFragment_SA newInstance() {
        Bundle args = new Bundle();

        RecordFragment_SA fragment = new RecordFragment_SA();
        fragment.setArguments(args);
        return fragment;
    }
}
