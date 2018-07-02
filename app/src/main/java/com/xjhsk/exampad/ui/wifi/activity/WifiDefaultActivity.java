package com.xjhsk.exampad.ui.wifi.activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kongqw.wifilibrary.WiFiManager;
import com.kongqw.wifilibrary.listener.OnWifiConnectListener;
import com.kongqw.wifilibrary.listener.OnWifiEnabledListener;
import com.kongqw.wifilibrary.listener.OnWifiScanResultsListener;
import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.R;
import com.xjhsk.exampad.app.AppContext;
import com.xjhsk.exampad.base.RootActivity;
import com.xjhsk.exampad.model.bean.WifiVO;
import com.xjhsk.exampad.ui.login.activity.ReadyLoginActivity;
import com.xjhsk.exampad.ui.wifi.adapter.WifiListAdapter;
import com.xjhsk.exampad.ui.wifi.contract.WifiDefaultContract;
import com.xjhsk.exampad.ui.wifi.contract.WifiListContract;
import com.xjhsk.exampad.ui.wifi.presenter.WifiDefaultPresenter;
import com.xjhsk.exampad.ui.wifi.presenter.WifiListPresenter;
import com.xjhsk.exampad.utils.RxTimerUtil;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;

/**
 * 演示版本
 * 这里没有设置wifi的地方
 * 只能输入 服务器地址
 */
public class WifiDefaultActivity extends RootActivity<WifiDefaultPresenter> implements WifiDefaultContract.View,
        OnWifiScanResultsListener, OnWifiConnectListener, OnWifiEnabledListener {

    private static final String TAG = WifiDefaultActivity.class.getSimpleName();

    //wifi 管理
    private WiFiManager mWiFiManager;

    @BindView(R.id.wifi_name_tv)
    TextView wifi_name_tv;

    @BindView(R.id.ip_layout)
    AutoLinearLayout ip_layout;

    @BindView(R.id.ip_tv)
    TextView ip_tv;

    @BindView(R.id.control_layout)
    AutoLinearLayout control_layout;

    @BindView(R.id.connect_img)
    ImageView connect_img;

    @BindView(R.id.sure_img)
    ImageView sure_img;

    @BindView(R.id.refresh_img)
    ImageView refresh_img;

    @BindView(R.id.login_keyboard)
    AutoLinearLayout login_keyboard;

    //键盘组件
    @BindView(R.id.img_keyboard_1)
    ImageView img_keyboard_1;
    @BindView(R.id.img_keyboard_2)
    ImageView img_keyboard_2;
    @BindView(R.id.img_keyboard_3)
    ImageView img_keyboard_3;
    @BindView(R.id.img_keyboard_4)
    ImageView img_keyboard_4;
    @BindView(R.id.img_keyboard_5)
    ImageView img_keyboard_5;
    @BindView(R.id.img_keyboard_6)
    ImageView img_keyboard_6;
    @BindView(R.id.img_keyboard_7)
    ImageView img_keyboard_7;
    @BindView(R.id.img_keyboard_8)
    ImageView img_keyboard_8;
    @BindView(R.id.img_keyboard_9)
    ImageView img_keyboard_9;
    @BindView(R.id.img_keyboard_0)
    ImageView img_keyboard_0;
    @BindView(R.id.img_keyboard_delete)
    ImageView img_keyboard_delete;
    @BindView(R.id.img_keyboard_clear)
    ImageView img_keyboard_clear;
    @BindView(R.id.img_keyboard_point)
    ImageView img_keyboard_point;

    //临时的wifivo
    private WifiVO _wifiVO;

    @Override
    protected int getLayout() {
        return R.layout.activity_wifi_default;
    }

    private String defaultIp;

    @Override
    protected void initEventAndData() {
        mWiFiManager = WiFiManager.getInstance(this);

        //不显示多余按钮
        sure_img.setVisibility(View.INVISIBLE);
        ip_layout.setVisibility(View.INVISIBLE);

        //首先判断wifi是否打开
        LogUtil.debug(TAG,"判断WIFI是否开启");
        if(mWiFiManager.isWifiEnabled()){
            LogUtil.debug(TAG,"WIFI已经开启");
            List<ScanResult> scanResults = mWiFiManager.getScanResults();
            refreshData(scanResults);
        }
        else {
            //没打开wifi  需要手动打开
            LogUtil.debug(TAG,"手动打开WIFI");
            mWiFiManager.openWiFi();
        }


//        connect_img.setVisibility(View.INVISIBLE);
    }


    @OnClick(R.id.refresh_img)
    void onRefreshBut(){
        LogUtil.debug(TAG,"Button 点击刷新按钮");
        List<ScanResult> scanResults = mWiFiManager.getScanResults();
        refreshData(scanResults);

        //刷新网络时 不显示键盘
        login_keyboard.setVisibility(View.GONE);
    }

    @OnClick(R.id.sure_img)
    void onSureBut(){
        LogUtil.debug(TAG,"Button 点击确定按钮");
        if(checkIp()){

//            String host = "221.238.139.253";// ip_tv.getText().toString();
            String host = ip_tv.getText().toString();
            AppContext.getInstance().setHostIp(host);

            RetrofitUrlManager.getInstance().putDomain("xhk", "http://"+host+":8083/xhk/");

            LogUtil.debug(TAG,"IP符合规则，打开准备登陆界面");
            startActivity(ReadyLoginActivity.newInstance(this));
            finish();
        }
        else {
            LogUtil.debug(TAG,"IP格式输入错误");
            ToastUtils.showShortToast("IP格式输入错误");
        }
    }

    @OnClick(R.id.connect_img)
    void onConnectBut(){
        LogUtil.debug(TAG,"Button 点击连接按钮");
        ScanResult scanResult = _wifiVO.getScanResult();
        boolean isconnect = false;

//        String password = "v0unpvlq";
        String password = "iFlytek1234";
//        String password = "89756197";
//        switch (ExamApis.HOST){
//            case "http://10.4.67.152:8083/xhk/":
//                password = "12345678";
//                break;
//            case "http://192.168.18.7:18081/xhk/":
//                password = "v0unpvlq";
//                break;
//            case "http://221.238.139.253:8083/xhk/":
//                password = "89756197";
//                break;
//        }

        switch (mWiFiManager.getSecurityMode(scanResult)) {
            case WPA:
            case WPA2:
                LogUtil.debug(TAG,"正在准备 WPA或者WPA2 连接WIFI");
                isconnect = mWiFiManager.connectWPA2Network(scanResult.SSID, password);//12345678   v0unpvlq
                break;
            case WEP:
                LogUtil.debug(TAG,"正在准备 WEP 连接WIFI");
                isconnect = mWiFiManager.connectWEPNetwork(scanResult.SSID, password);//
                break;
            case OPEN: // 开放网络
                LogUtil.debug(TAG,"正在准备 OPEN 连接WIFI");
                isconnect = mWiFiManager.connectOpenNetwork(scanResult.SSID);
                break;
        }

        if(!isconnect){
            ToastUtils.showShortToast("WIFI连接失败，请确认密码是否为"+password);
        }
    }

    @OnClick(R.id.ip_layout)
    void onIpLayout(){
        LogUtil.debug(TAG,"显示IP文本内容");
        login_keyboard.setVisibility(View.VISIBLE);
    }

    private void showIpLayout(){
        //优化显示IP
        defaultIp = NetworkUtils.getIPAddress(true);
        LogUtil.debug(TAG,"本机的IP地址为 "+defaultIp);

        AppContext.getInstance().setIp(defaultIp);
        LogUtil.debug(TAG,"把IP地址（"+defaultIp+"）设置到 AppContext中");

        String ip = "";
        if(EmptyUtils.isEmpty(defaultIp)){
            LogUtil.debug(TAG,"这是一个空的IP");
            return;
        }else {
            String[] ips = defaultIp.split("\\.");
            ip = defaultIp.replace(defaultIp.substring(defaultIp.length()-ips[ips.length-1].length(),defaultIp.length()),"1");
        }
//        str.replace(str.substring(beginIndex, endIndex), newChar);
        LogUtil.debug(TAG,"设置WIFI路由的根地址为 "+ip);
        ip_tv.setText(ip);
        ip_layout.setVisibility(View.VISIBLE);
        sure_img.setVisibility(View.VISIBLE);
    }


    private boolean checkIp(){
        /*正则表达式*/
        String ip = "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|[1-9])\\."
                + "(25[0-5]|2[0-4]\\d|1\\d{1,2}|\\d{2}|\\d)\\."
                + "(25[0-5]|2[0-4]\\d|1\\d{1,2}|\\d{2}|\\d)\\."
                + "(25[0-5]|2[0-4]\\d|1\\d{1,2}|\\d{2}|\\d)";//限定输入格式

        Pattern p = Pattern.compile(ip);
        Matcher m = p.matcher(ip_tv.getText().toString());

        return m.matches();
    }
    //************************************************************************************************//
    //键盘操作
    @OnClick(R.id.img_keyboard_1)
    void onImgKeyboard_1(){
        LogUtil.debug(TAG,"点击键盘 1 ");
        ip_tv.setText(ip_tv.getText()+"1");
    }
    @OnClick(R.id.img_keyboard_2)
    void onImgKeyboard_2(){
        LogUtil.debug(TAG,"点击键盘 2 ");
        ip_tv.setText(ip_tv.getText()+"2");
    }
    @OnClick(R.id.img_keyboard_3)
    void onImgKeyboard_3(){
        LogUtil.debug(TAG,"点击键盘 3 ");
        ip_tv.setText(ip_tv.getText()+"3");
    }
    @OnClick(R.id.img_keyboard_4)
    void onImgKeyboard_4(){
        LogUtil.debug(TAG,"点击键盘 4 ");
        ip_tv.setText(ip_tv.getText()+"4");
    }
    @OnClick(R.id.img_keyboard_5)
    void onImgKeyboard_5(){
        LogUtil.debug(TAG,"点击键盘 5 ");
        ip_tv.setText(ip_tv.getText()+"5");
    }
    @OnClick(R.id.img_keyboard_6)
    void onImgKeyboard_6(){
        LogUtil.debug(TAG,"点击键盘 6 ");
        ip_tv.setText(ip_tv.getText()+"6");
    }
    @OnClick(R.id.img_keyboard_7)
    void onImgKeyboard_7(){
        LogUtil.debug(TAG,"点击键盘 7 ");
        ip_tv.setText(ip_tv.getText()+"7");
    }
    @OnClick(R.id.img_keyboard_8)
    void onImgKeyboard_8(){
        LogUtil.debug(TAG,"点击键盘 8 ");
        ip_tv.setText(ip_tv.getText()+"8");
    }
    @OnClick(R.id.img_keyboard_9)
    void onImgKeyboard_9(){
        LogUtil.debug(TAG,"点击键盘 9 ");
        ip_tv.setText(ip_tv.getText()+"9");
    }
    @OnClick(R.id.img_keyboard_0)
    void onImgKeyboard_0(){
        LogUtil.debug(TAG,"点击键盘 0 ");
        ip_tv.setText(ip_tv.getText()+"0");
    }
    @OnClick(R.id.img_keyboard_delete)
    void onImgKeyboard_Delete(){
        LogUtil.debug(TAG,"点击键盘 删除一位 ");
        int length = ip_tv.getText().toString().length();
        if(length>0){
            ip_tv.setText(ip_tv.getText().toString().substring(0,length-1));
        }
    }
    @OnClick(R.id.img_keyboard_clear)
    void onImgKeyboard_Clear(){
        LogUtil.debug(TAG,"点击键盘 清空 ");
        ip_tv.setText("");
    }

    @OnClick(R.id.img_keyboard_point)
    void onImgKeyboard_Point(){
        LogUtil.debug(TAG,"点击键盘 . 符号 ");
        ip_tv.setText(ip_tv.getText()+".");
    }

    //***********************************************************************************************//
    @Override
    protected void onResume() {
        super.onResume();
        // 添加监听
        LogUtil.debug(TAG,"程序进入onResume开始监听wifi");
        mWiFiManager.setOnWifiEnabledListener(this);
        mWiFiManager.setOnWifiScanResultsListener(this);
        mWiFiManager.setOnWifiConnectListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 移除监听
        LogUtil.debug(TAG,"程序进入onPause移除wifi监听");
        mWiFiManager.removeOnWifiEnabledListener();
        mWiFiManager.removeOnWifiScanResultsListener();
        mWiFiManager.removeOnWifiConnectListener();
    }

    /**
     * WIFI连接的Log得回调
     *
     * @param log log
     */
    @Override
    public void onWiFiConnectLog(String log) {
        LogUtil.debug(TAG,"wifi连接的回调"+log);
//        ToastUtils.showShortToast("wifi连接的回调 "+log);
    }

    /**
     * WIFI连接成功的回调
     *
     * @param SSID 热点名
     */
    @Override
    public void onWiFiConnectSuccess(String SSID) {
        ToastUtils.showShortToast(SSID + "连接成功");

        LogUtil.debug(TAG,"wifi连接成功"+SSID);

        RxTimerUtil.timer(1000, new RxTimerUtil.IRxNext() {
            @Override
            public void doNext(long number) {
                //只有不是第一次进入才显示
                List<ScanResult> scanResults = mWiFiManager.getScanResults();
                refreshData(scanResults);
            }
        });

    }

    /**
     * WIFI连接失败的回调
     *
     * @param SSID 热点名
     */
    @Override
    public void onWiFiConnectFailure(String SSID) {
        ToastUtils.showShortToast(SSID + "连接失败！请重新连接！");

        LogUtil.debug(TAG,"wifi连接失败"+SSID);

        RxTimerUtil.timer(1000, new RxTimerUtil.IRxNext() {
            @Override
            public void doNext(long number) {
                //只有不是第一次进入才显示
                List<ScanResult> scanResults = mWiFiManager.getScanResults();
                refreshData(scanResults);
            }
        });
    }

    /**
     * WIFI开关状态的回调
     *
     * @param enabled true 可用 false 不可用
     */
    @Override
    public void onWifiEnabled(boolean enabled) {
//        ToastUtils.showShortToast(  "  WIFI开关状态的回调");
    }

    /**
     * WIFI列表刷新后的回调
     * @param scanResults 扫描结果
     */
    @Override
    public void onScanResults(List<ScanResult> scanResults) {
//        refreshData(scanResults);
    }

    /**
     * 刷新页面
     *
     * @param scanResults WIFI数据
     */
    public void refreshData(List<ScanResult> scanResults) {

//        connect_img.setVisibility(View.INVISIBLE);

        LogUtil.debug(TAG,"判断wifi是否连接");
        //如果有wifi连接 则显示ip
        if(NetworkUtils.isConnected()){
            LogUtil.debug(TAG,"wifi已连接");

            wifi_name_tv.setText(WiFiManager.getInstance(mContext).getConnectionInfo().getSSID());

            showIpLayout();
        }else {
            LogUtil.debug(TAG,"wifi未连接");
            wifi_name_tv.setText("网络未连接，请点击刷新WIFI按钮");
            ip_layout.setVisibility(View.INVISIBLE);
            sure_img.setVisibility(View.INVISIBLE);
        }

        _wifiVO = null;


    }


    //***********************************************************************************************//

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, WifiDefaultActivity.class);
        return intent;
    }



}


