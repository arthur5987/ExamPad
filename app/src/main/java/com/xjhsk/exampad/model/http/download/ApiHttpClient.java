package com.xjhsk.exampad.model.http.download;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xjhsk.exampad.app.AppContext;

/**
 * 网络连接客户端
 * 用于文件上传
 * @author HongYu
 *
 * 2015年7月9日
 */
public class ApiHttpClient {

    private static ApiHttpClient _instance;

    private AsyncHttpClient _client;//网络连接客户端

    private static final int TIME_OUT = 25 * 1000;// 默认超时时间

    private static final int CONNECT_RETRY_TIMES = 3;// 默认连接次数
    
    private Context _context;

    public static ApiHttpClient getInstance(){
        if(_instance == null)
        {
            _instance = new ApiHttpClient();
        }
        return _instance;
    }
    private ApiHttpClient(){
        _client = new AsyncHttpClient(true, 80, 443);

        _client.setMaxRetriesAndTimeout(CONNECT_RETRY_TIMES, TIME_OUT);
        _context = AppContext.getInstance();
        // 据说可以加快上传速度
        System.setProperty("http.keepAlive", "false");
    }
    /**
     * 获取网络客户端
     * @return
     */
    public AsyncHttpClient getClient(){
        return _client;
    }

    /**
     * 外部设置网络客户端
     * @param client
     */
    public void setClient(AsyncHttpClient client){
        this._client = client;
    }
    /**
     * 带参数的post外部调用context
     * @param context
     * @param url
     * @param params
     */
    public void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        _client.post(context, url, params, responseHandler);
    }
    /**
     * 带参数的post
     * @param url
     * @param params
     */
    public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        _client.post(_context, url, params, responseHandler);
    }
    /**
     * 不带参数post
     * @param url
     */
    public void post(String url, AsyncHttpResponseHandler responseHandler){
        _client.post(url, responseHandler);
    }
    /**
     * 下载文件,异步处理
     * @param url
     * @param responseHandler
     */
    public synchronized void get(String url, BinaryHttpResponseHandler responseHandler){
        _client.get(url, responseHandler);
    }
    
    /**
     * 超时取消请求
     * @param isCancelRunning
     */
    public void cancelAllRequest(boolean isCancelRunning){
        _client.cancelAllRequests(isCancelRunning);
    }
}
