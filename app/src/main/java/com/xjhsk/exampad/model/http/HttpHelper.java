package com.xjhsk.exampad.model.http;

import com.xjhsk.exampad.model.bean.CheckVerVO;
import com.xjhsk.exampad.model.bean.ExamTitleVO;
import com.xjhsk.exampad.model.bean.UserData;
import com.xjhsk.exampad.model.http.response.HttpResponse;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 作者：weidingqiang on 2017/7/11 09:56
 * 邮箱：dqwei@iflytek.com
 */

public interface HttpHelper {


    //1.获取版本更新
    Flowable<HttpResponse<CheckVerVO>> checkVer(String version, String key, String ip, String timestamp);

    //2.获取APP显示title
    Flowable<HttpResponse<ExamTitleVO>> getExamTitle(String key, String ip, String timestamp);

    //3.获取登录标识      status  50
    //10.获取开考命令     status  60
    //14.等待考试结束     status  70
    Flowable<HttpResponse<String>> getStatus(String examNo, String key, String status, String timestamp);

    //4.获取用户信息（登录）
    Flowable<HttpResponse<UserData>> login(String examNo, String examIp, String key, String timestamp);

    //5.准备开始试音 status 100
    //6.反馈试音结果   101
    //7.准备下载试卷   110
    //9.反馈下载试卷结果 111
    //11.开始答题      120
    Flowable<HttpResponse<String>> setStatus(String examNo, String status, String key, String timestamp);

    //12.保存考试进度  examStep  第1题
    Flowable<HttpResponse<String>> saveExamStep(String examNo, String examStep, String key, String timestamp);

    //13.上传作答记录
    //公共上传
    Flowable<HttpResponse<String>> uploadExam(RequestBody timestamp, RequestBody key, RequestBody examNo,
                                              RequestBody fileName, RequestBody md5,MultipartBody.Part fileData);

    //16.退出登录
    Flowable<HttpResponse<String>> logout(String examNo, String examIp, String key, String timestamp);
}
