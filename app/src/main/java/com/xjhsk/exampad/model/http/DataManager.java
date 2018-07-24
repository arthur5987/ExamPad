package com.xjhsk.exampad.model.http;

import com.xjhsk.exampad.model.bean.CheckVerVO;
import com.xjhsk.exampad.model.bean.ExamTitleVO;
import com.xjhsk.exampad.model.bean.UserData;
import com.xjhsk.exampad.model.http.response.HttpResponse;
import com.xjhsk.exampad.model.prefs.PreferencesHelper;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 作者：weidingqiang on 2017/7/11 09:55
 * 邮箱：dqwei@iflytek.com
 */

public class DataManager implements HttpHelper, PreferencesHelper {

    HttpHelper mHttpHelper;

    PreferencesHelper mPreferencesHelper;

    public DataManager(HttpHelper httpHelper, PreferencesHelper mPreferencesHelper) {
        mHttpHelper = httpHelper;
        this.mPreferencesHelper = mPreferencesHelper;
    }

    @Override
    public Flowable<HttpResponse<CheckVerVO>> checkVer(String version, String key, String ip, String timestamp) {
        return mHttpHelper.checkVer(version, key, ip, timestamp);
    }

    @Override
    public Flowable<HttpResponse<ExamTitleVO>> getExamTitle(String key, String ip, String timestamp) {
        return mHttpHelper.getExamTitle(key, ip, timestamp);
    }

    @Override
    public Flowable<HttpResponse<String>> getStatus(String examNo, String key, String status, String timestamp) {
        return mHttpHelper.getStatus(examNo, key, status, timestamp);
    }

    @Override
    public Flowable<HttpResponse<UserData>> login(String examNo, String examIp, String key, String timestamp) {
        return mHttpHelper.login(examNo, examIp, key, timestamp);
    }

    @Override
    public Flowable<HttpResponse<String>> setStatus(String examNo, String status, String key, String timestamp) {
        return mHttpHelper.setStatus(examNo, status, key, timestamp);
    }

    @Override
    public Flowable<HttpResponse<String>> saveExamStep(String examNo, String examStep, String key, String timestamp) {
        return mHttpHelper.saveExamStep(examNo, examStep, key, timestamp);
    }

    @Override
    public Flowable<HttpResponse<String>> uploadExam(RequestBody timestamp, RequestBody key, RequestBody examNo, RequestBody fileName,RequestBody md5,RequestBody answerStr, MultipartBody.Part fileData) {
        return mHttpHelper.uploadExam(timestamp, key, examNo, fileName,md5,answerStr, fileData);
    }

    @Override
    public Flowable<HttpResponse<String>> logout(String examNo, String examIp, String key, String timestamp) {
        return mHttpHelper.logout(examNo, examIp, key, timestamp);
    }

    @Override
    public boolean getNightModeState() {
        return false;
    }

    @Override
    public void setNightModeState(boolean state) {

    }
}
