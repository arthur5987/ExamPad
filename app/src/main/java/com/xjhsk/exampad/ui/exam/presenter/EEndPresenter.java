package com.xjhsk.exampad.ui.exam.presenter;

import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.google.gson.Gson;
import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.api.Constants;
import com.xjhsk.exampad.app.AppContext;
import com.xjhsk.exampad.base.RxBus;
import com.xjhsk.exampad.base.RxPresenter;
import com.xjhsk.exampad.model.bean.AnswerHeaderVO;
import com.xjhsk.exampad.model.bean.AnswerVO;
import com.xjhsk.exampad.model.bean.PagerInfo;
import com.xjhsk.exampad.model.event.SocketEvent;
import com.xjhsk.exampad.model.http.DataManager;
import com.xjhsk.exampad.model.http.response.HttpResponse;
import com.xjhsk.exampad.ui.exam.contract.EEndContract;
import com.xjhsk.exampad.ui.exam.contract.EHintContract;
import com.xjhsk.exampad.utils.CommonSubscriber;
import com.xjhsk.exampad.utils.CompressUtils;
import com.xjhsk.exampad.utils.Md5CaculateUtil;
import com.xjhsk.exampad.utils.RxUtil;
import com.xjhsk.exampad.utils.Sha1Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 作者：weidingqiang on 2018/1/12 14:53
 * 邮箱：dqwei@iflytek.com
 */

public class EEndPresenter extends RxPresenter<EEndContract.View> implements EEndContract.Presenter {

    protected static final String TAG = EEndPresenter.class.getSimpleName();

    private DataManager mDataManager;

    @Inject
    public EEndPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(EEndContract.View view) {
        super.attachView(view);
        registerEvent();
    }


    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(SocketEvent.class)
                .compose(RxUtil.<SocketEvent>rxSchedulerHelper())
                .filter(new Predicate<SocketEvent>() {
                    @Override
                    public boolean test(@NonNull SocketEvent socketEvent) throws Exception {
                        return socketEvent.getType().equals(SocketEvent.ExamEnd);
                    }
                })
                .subscribeWith(new CommonSubscriber<SocketEvent>(mView) {
                    @Override
                    public void onNext(SocketEvent s) {
                        mView.endExam();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );
    }

    private void uploadExam(String md5 , String filename) {

        File zipFile = new File(filename);

        //设置Content-Type:application/octet-stream
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), zipFile);
        //设置Content-Disposition:form-data; name="photo"; filename="xuezhiqian.png"
        MultipartBody.Part fileData = MultipartBody.Part.createFormData("fileData", zipFile.getName(), fileRequestBody);

        String time_mills = String.valueOf(TimeUtils.getNowTimeMills());
        String key = Sha1Util.getSha1(time_mills+ Constants.KEY);

        RequestBody fileName = RequestBody.create(MediaType.parse("text/plain"), zipFile.getName());

        RequestBody time_mills_body = RequestBody.create(MediaType.parse("text/plain"), time_mills);
        RequestBody key_body = RequestBody.create(MediaType.parse("text/plain"), key);
        RequestBody exam_no_body = RequestBody.create(MediaType.parse("text/plain"), AppContext.getInstance().getUserVO().getExam_no());

        RequestBody md5Str = RequestBody.create(MediaType.parse("text/plain"), md5);

        addSubscribe(mDataManager.uploadExam(time_mills_body, key_body,exam_no_body, fileName,md5Str,fileData)
                .compose(RxUtil.<HttpResponse<String>>rxSchedulerHelper())
                .compose(RxUtil.<String>handleTestResult())
                .subscribeWith(
                        new CommonSubscriber<String>(mView) {
                            @Override
                            public void onNext(String data) {

                                mView.uploadExamSuccess();
                            }

                            @Override
                            public void onError(Throwable e) {
                                mView.uploadExamError();
                            }

                            @Override
                            public void onComplete() {
                                super.onComplete();
                            }
                        }
                )
        );
    }

    @Override
    public void createJsonFileAndZip(final String userDir, final PagerInfo pagerInfo) {

        Observable.just(userDir)
                //生成json
                .map(new Function<String, JSONObject>() {
                    @Override
                    public JSONObject apply(@NonNull String s) throws Exception {

                        Gson gson = new Gson();

                        ArrayList<AnswerVO> answerVOS = AppContext.getInstance().getAnswerVOS();

                        JSONArray accountArray = new JSONArray();
                        for (int i = 0; i < answerVOS.size(); i++) {
                            String answervo = gson.toJson(answerVOS.get(i));
                            JSONObject answerObject;
                            try {
                                answerObject = new JSONObject(answervo);
                                accountArray.put(i, answerObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        AnswerHeaderVO answerHeaderVO =
                                new AnswerHeaderVO(AppContext.getInstance().getUserVO().getExam_no(),pagerInfo.getPagerNo());

                        String answerHeaderVOStr = gson.toJson(answerHeaderVO);

                        JSONObject jsonObject = new JSONObject(answerHeaderVOStr);
                        jsonObject.put("answer", accountArray);


//                        File file = new File(s, "answers.json");
//                        FileOutputStream fos = new FileOutputStream(file);
//                        fos.write(jsonObject.toString().getBytes());
//                        fos.close();
                        return jsonObject;
                    }
                })
                .map(new Function<JSONObject, String>() {
                    @Override
                    public String apply(JSONObject jsonObject) throws Exception {
                        File file = new File(userDir, "answers.json");
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(jsonObject.toString().getBytes());
                        fos.close();
                        return userDir;
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {
//                        File file = new File(s);
//
//                        Collection<File> list = new ArrayList<File>();
//                        for (int i=0;i<file.listFiles().length;i++)
//                        {
//                            File fileChild  = file.listFiles()[i];
//                            list.add(fileChild);
//                        }
                        String zipfile = s+".zip";
//                        ZipUtils.zipFiles(list,zipfile);
//                        return zipfile;

                        //采用 zip加密方式
                        CompressUtils.zip(s,zipfile,false,CompressUtils.KEY_ZIP_ANS);

                        return zipfile;

                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return Md5CaculateUtil.getHash(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscribe(d);
                    }

                    @Override
                    public void onNext(@NonNull String md5) {
                        LogUtil.debug(TAG,"4.压缩Zip文件完成");
                        uploadExam(md5,userDir+".zip");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.debug(TAG,"解析文件失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
