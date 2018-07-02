package com.xjhsk.exampad.model.http;

import com.xjhsk.exampad.model.bean.CheckVerVO;
import com.xjhsk.exampad.model.bean.ExamTitleVO;
import com.xjhsk.exampad.model.bean.UserData;
import com.xjhsk.exampad.model.http.api.ExamApis;
import com.xjhsk.exampad.model.http.response.HttpResponse;

import javax.inject.Inject;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 */
public class RetrofitHelper implements HttpHelper {

    private ExamApis examApis;

    @Inject
    public RetrofitHelper(ExamApis examApis) {
        this.examApis = examApis;
    }

    @Override
    public Flowable<HttpResponse<CheckVerVO>> checkVer(String version, String key, String ip, String timestamp) {
        return examApis.checkVer(version, key, ip, timestamp);
    }

    @Override
    public Flowable<HttpResponse<ExamTitleVO>> getExamTitle(String key, String ip, String timestamp) {
        return examApis.getExamTitle(key, ip, timestamp);
    }

    @Override
    public Flowable<HttpResponse<String>> getStatus(String examNo, String key, String status, String timestamp) {
        return examApis.getStatus(examNo, key, status, timestamp);
    }

    @Override
    public Flowable<HttpResponse<UserData>> login(String examNo, String examIp, String key, String timestamp) {
        return examApis.login(examNo, examIp, key, timestamp);
    }

    @Override
    public Flowable<HttpResponse<String>> setStatus(String examNo, String status, String key, String timestamp) {
        return examApis.setStatus(examNo, status, key, timestamp);
    }

    @Override
    public Flowable<HttpResponse<String>> saveExamStep(String examNo, String examStep, String key, String timestamp) {
        return examApis.saveExamStep(examNo, examStep, key, timestamp);
    }

    @Override
    public Flowable<HttpResponse<String>> uploadExam(RequestBody timestamp, RequestBody key, RequestBody examNo, RequestBody fileName,RequestBody md5, MultipartBody.Part fileData) {
        return examApis.uploadExam(timestamp, key, examNo, fileName,md5, fileData);
    }

    @Override
    public Flowable<HttpResponse<String>> logout(String examNo, String examIp, String key, String timestamp) {
        return examApis.logout(examNo, examIp, key, timestamp);
    }
}
