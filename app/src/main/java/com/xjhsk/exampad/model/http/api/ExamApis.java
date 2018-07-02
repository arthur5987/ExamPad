package com.xjhsk.exampad.model.http.api;

import com.xjhsk.exampad.app.SocketConfig;
import com.xjhsk.exampad.model.bean.CheckVerVO;
import com.xjhsk.exampad.model.bean.ExamTitleVO;
import com.xjhsk.exampad.model.bean.UserData;
import com.xjhsk.exampad.model.http.response.HttpResponse;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 作者：weidingqiang on 2018/1/11 14:58
 * 邮箱：dqwei@iflytek.com
 */

public interface ExamApis {

    //公司内网
    String HOST = "http://10.4.67.152:8083/xhk/";
//    String HOST = "http://"+ SocketConfig.BaseSocket+":8083/xhk/";
    //赵飞
//    String HOST = "http://192.168.18.7:18081/xhk/";
//    String HOST = "http://"+ SocketConfig.BaseSocket+":18081/xhk/";
    //外网地址
//    String HOST = "http://221.238.139.253:8083/xhk/";
//    String HOST = "http://"+ SocketConfig.BaseSocket+":8083/xhk/";

    //1.获取版本更新
    @Headers({"Domain-Name: xhk"})
    @FormUrlEncoded
    @POST("stu/app/checkVer")
    Flowable<HttpResponse<CheckVerVO>> checkVer(@Field("version") String version,
                                                 @Field("key") String key,
                                                 @Field("ip") String ip,
                                                 @Field("timestamp") String timestamp);

    //2.获取APP显示title
    @Headers({"Domain-Name: xhk"})
    @FormUrlEncoded
    @POST("stu/config/getExamTitle")
    Flowable<HttpResponse<ExamTitleVO>> getExamTitle(@Field("key") String key,
                                                     @Field("ip") String ip,
                                                     @Field("timestamp") String timestamp);

    //3.获取登录标识      status  50
    //10.获取开考命令     status  60
    //14.等待考试结束     status  70
    @Headers({"Domain-Name: xhk"})
    @FormUrlEncoded
    @POST("stu/message/getStatus")
    Flowable<HttpResponse<String>> getStatus(@Field("examNo") String examNo,
                                                  @Field("key") String key,
                                                     @Field("status") String status,
                                                     @Field("timestamp") String timestamp);

    //4.获取用户信息（登录）
    @Headers({"Domain-Name: xhk"})
    @FormUrlEncoded
    @POST("stu/user/login")
    Flowable<HttpResponse<UserData>> login(@Field("examNo") String examNo,
                                                  @Field("examIp") String examIp,
                                                  @Field("key") String key,
                                                  @Field("timestamp") String timestamp);

    //5.准备开始试音 status 100
    //6.反馈试音结果   101
    //7.准备下载试卷   110
    //9.反馈下载试卷结果 111
    //11.开始答题      120
    @Headers({"Domain-Name: xhk"})
    @FormUrlEncoded
    @POST("stu/message/setStatus")
    Flowable<HttpResponse<String>> setStatus(@Field("examNo") String examNo,
                                           @Field("status") String status,
                                           @Field("key") String key,
                                           @Field("timestamp") String timestamp);

    //12.保存考试进度  examStep  第1题
    @Headers({"Domain-Name: xhk"})
    @FormUrlEncoded
    @POST("stu/message/saveExamStep")
    Flowable<HttpResponse<String>> saveExamStep(@Field("examNo") String examNo,
                                             @Field("examStep") String examStep,
                                             @Field("key") String key,
                                             @Field("timestamp") String timestamp);

    //13.上传作答记录
    //公共上传
    @Headers({"Domain-Name: xhk"})
    @Multipart
    @POST("stu/exam/uploadExam")
    Flowable<HttpResponse<String>> uploadExam(@Part("timestamp") RequestBody timestamp,
                                              @Part("key") RequestBody key,
                                              @Part("examNo") RequestBody examNo,
                                              @Part("fileName") RequestBody fileName,
                                              @Part("md5") RequestBody md5,
                                              @Part MultipartBody.Part fileData);

    //16.退出登录
    @Headers({"Domain-Name: xhk"})
    @FormUrlEncoded
    @POST("stu/user/logout")
    Flowable<HttpResponse<String>> logout(@Field("examNo") String examNo,
                                                @Field("examIp") String examIp,
                                                @Field("key") String key,
                                                @Field("timestamp") String timestamp);
}
