package com.xjhsk.exampad.socket;


import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketResponsePacket;
import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.app.AppContext;
import com.xjhsk.exampad.app.SocketConfig;
import com.xjhsk.exampad.base.RxBus;
import com.xjhsk.exampad.model.event.SocketEvent;
import com.xjhsk.exampad.utils.RxTimerUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * 作者：weidingqiang on 2018/1/16 10:23
 * 邮箱：dqwei@iflytek.com
 *https://github.com/vilyever/AndroidSocketClient
 * https://github.com/vilyever/AndroidSocketClient/blob/master/app/src/main/java/com/vilyever/androidsocketclient/TestClient.java
 */

public class SocketManager {

    private static final String TAG = SocketManager.class.getSimpleName();

    private static final String HeartBeat ="0000";

    //发送的指令
    //获取允许登录状态
    public static final String ReadyLogin = "0100";

    //是否开始考试
    public static final String ReadyExam = "0200";

    //是否考试完毕
    public static final String ReadyExamEnd = "0300";

    //收到的指令
    //还没有允许登录
    private static final String ResponseNotAllowLogin = "0101";

    //允许登录
    private static final String ResponseAllowLogin = "0102";

    //已开始开始不允许登录
    private static final String ResponseNotAllowLogin2 = "0103";

    //没有开始考试请等待
    private static final String ResponseNotStartExam = "0201";

    //5秒后快开始考试
    private static final String ResponseStartExam = "0202";

    //没有考试完毕请等待
    private static final String ResponseNotExamEnd = "0301";

    //考试完毕
    private static final String ResponseExamEnd = "0302";

    //等待登录
    private static final String ResponseReadyLogin ="0800";

    //远程控制指令  需要root
    //退出登录
    private static final String ResponseLogout = "0400";

    //重启机器
    private static final String ResponseReStart ="0900";

    //关机
    private static final String ResponseShutDown = "0901";

    private static final String MessageEnd = "OVER";

    private SocketClient socketClient;

    /**
     * 需要增加断线重连
     * 心跳
     */

    public SocketManager(){
        socketClient = new SocketClient(AppContext.getInstance().getHostIp(), SocketConfig.SocketPort);
        initSocket();
    }

    private void initSocket(){
        socketClient.registerSocketDelegate(new SocketClient.SocketDelegate() {
            @Override
            public void onConnected(SocketClient client) {
                LogUtil.debug(TAG,"4.socket连接成功");
                RxBus.getDefault().post(new SocketEvent(SocketEvent.ConnectSuccess));

                //开启心跳
                startHeartBeat();
            }

            @Override
            public void onDisconnected(SocketClient client) {
                LogUtil.debug(TAG,"!!!!socket断了!!!!");

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(3 * 1000);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        socketClient.connect();

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                    }
                }.execute();
            }

            @Override
            public void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket) {
//                byte[] data = responsePacket.getData(); // 获取byte[]数据
//                String msg = responsePacket.getMessage(); // 使用默认编码获取String消息
                LogUtil.debug(TAG,"!!!response  接受到数据!!!    "+responsePacket.getMessage());

                //收到数据开始解析
                //可能收到多条数据 所以要分开解析
                //1.获取收到几条消息 以 #OVER 结尾
                String response = responsePacket.getMessage();
                String[] responses = response.split(MessageEnd);

                //2.遍历数据
                for (int i=0;i<responses.length;i++)
                {
                    String responseItem = responses[i];

                    switch (responseItem){
                        //登陆相关
                        //还没有允许登录
                        case ResponseNotAllowLogin:
                            //不做任何处理
                            break;
                        //已开始开始不允许登录
                        case ResponseNotAllowLogin2:
                            //不做任何处理
                            break;
                        //允许登录
                        case ResponseAllowLogin:
                            RxBus.getDefault().post(new SocketEvent(SocketEvent.AllowLogin));
                            break;
                        //没有开始考试请等待
                        case ResponseNotStartExam:
                            //不做任何处理
                            break;
                        case ResponseStartExam:
                            //5秒后快开始考试
                            RxBus.getDefault().post(new SocketEvent(SocketEvent.StartExam));
                            break;
                        //没有考试完毕请等待
                        case ResponseNotExamEnd:
                            //不做任何处理
                            break;
                        case ResponseExamEnd:
                            //考试完毕
                            RxBus.getDefault().post(new SocketEvent(SocketEvent.ExamEnd));
                            break;

                        //其他指令
                        case ResponseReadyLogin:
                            //需要跳转到 准备登录界面
                            RxBus.getDefault().post(new SocketEvent(SocketEvent.ReadyLogin));
                            break;
                        case ResponseLogout:
                            //需要跳转到 登录界面
                            RxBus.getDefault().post(new SocketEvent(SocketEvent.Logout));
                            break;
                        case ResponseReStart:
                            //重启机器
                            RxBus.getDefault().post(new SocketEvent(SocketEvent.ReStart));
                            break;
                        case ResponseShutDown:
                            //关机
                            RxBus.getDefault().post(new SocketEvent(SocketEvent.ShutDown));
                            break;

                    }

                }

            }
        });

        socketClient.setConnectionTimeout(1000 * 50); // 设置连接超时时长
    }

    /**
     * 开启连接服务器
     */
    public void connect(){

        LogUtil.debug(TAG,"3.连接socket");
        socketClient.connect();

    }

    public boolean isConnect(){
        return socketClient.isConnected();
    }

    /**
     * 发送数据
     */
    public void sendMessage(String data){
        LogUtil.debug(TAG,"socket 发送数据   "+data);
        socketClient.sendString(data+MessageEnd);
//        socketClient.sendString(data);
    }

    private void startHeartBeat(){
        interval(20 * 1000, new RxTimerUtil.IRxNext() {
            @Override
            public void doNext(long number) {
                if(socketClient.isConnected()){
                    sendMessage(HeartBeat);
                }else {
                    cancel();
                }
            }
        });
    }



    //**********************************************************************************************//
    private  Disposable mDisposable;
    //定时器
    /** 每隔milliseconds毫秒后执行next操作
        * @param milliseconds
        * @param next
        */
    public void interval(long milliseconds,final RxTimerUtil.IRxNext next){

        Observable.interval(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable disposable) {
                        mDisposable=disposable;
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Long number) {
                        if(next!=null){
                            next.doNext(number);
                        }
                    }
                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }
    /**
        * 取消订阅
        */
    public  void cancel(){

        if(mDisposable!=null&&!mDisposable.isDisposed()){
            mDisposable.dispose();
            LogUtil.debug("","====定时器取消======");
        }
    }
}
