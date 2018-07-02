package com.xjhsk.exampad.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.fifedu.record.recinbox.bl.record.RecorderManager;
import com.weidingqiang.rxfiflibrary2.app.AppConfig;
import com.weidingqiang.rxfiflibrary2.app.AppConstants;
import com.weidingqiang.rxfiflibrary2.utils.LogToFileUtil;
import com.weidingqiang.rxfiflibrary2.utils.LogUtil;

public class InitializeService extends IntentService {

    private static final String TAG = InitializeService.class.getSimpleName();

    private static final String ACTION_INIT = "initApplication";

    public InitializeService() {
        super("InitializeService");
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT.equals(action)) {
                initApplication();
            }
        }
    }

    private void initApplication(){
        //初始化需要的文件夹
        initFinder();

        //初始化log
        initlog();

        //初始化其他信息
        init();

    }

    private void initFinder(){
        //设置缓存路径
        String cachePath;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            // /storage/emulated/0/Android/data/com.fifedu.mhk/cache

            try {
                cachePath = getExternalCacheDir().getPath();
            }catch (Exception ex)
            {
                cachePath = getCacheDir().getPath();
            }

        } else {
            // /data/data/com.fifedu.mhk/cache
            cachePath = getCacheDir().getPath();
        }
        AppConstants.initPath(cachePath);
    }

    private void initlog(){
        // 初始化日志写文件的工具
        LogToFileUtil.init();
        LogUtil.debug("TAG", "AppContext Created");
        LogUtil.debug("TAG", "Product Model: " + android.os.Build.MODEL + "\nApi Level: "
                + android.os.Build.VERSION.SDK + "\nVersion: " + android.os.Build.VERSION.RELEASE);
    }

    private void init() {

//        LogUtil.debug(TAG,"配置多态布局");
//        PageStateLayout.Builder.setErrorView(R.layout.page_state_error)
//                .setEmptyView(R.layout.page_state_empty);
        //初始化volley

        // 初始化音频录制工具
        LogUtil.debug(TAG,"初始化录音模块");
        RecorderManager.createInstance(this);

        AppConfig.getAppConfig(this);

        LogToFileUtil.clearOldFiles();

    }

}
