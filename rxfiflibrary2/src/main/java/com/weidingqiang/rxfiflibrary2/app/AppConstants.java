package com.weidingqiang.rxfiflibrary2.app;

import java.io.File;

/**
 * 常用的变量值
 *
 * @author weidingqiang
 *
 *         2015年6月2日
 */
public class AppConstants {

    public static String PARENT_FOLD_PATH = "";
    //缓存相关
    /**
     * 缓存地址
     */
    public static String CACHE_PATH = "";

    /**
     * 本地录音文件缓存地址
     */
//    public static String RECORD_PATH = "";

    /**
     * logs 文件
     * @param string
     */
    public static String LOGS_PATH = "";

    public static void initPath(String string){
        PARENT_FOLD_PATH = string+ File.separator;
        CACHE_PATH = PARENT_FOLD_PATH;

//        RECORD_PATH = PARENT_FOLD_PATH + "audio" + File.separator;
        DOWNLOAD_PATH = PARENT_FOLD_PATH + "download" + File.separator;
        LOGS_PATH = PARENT_FOLD_PATH + "logs" + File.separator;

        //download
//        PIC_DOWNLOAD_PATH = DOWNLOAD_PATH + "image" + File.separator;
        RECORD_DOWNLOAD_PATH = DOWNLOAD_PATH + "audio" + File.separator;
//        VIDEO_DOWNLOAD_PATH = DOWNLOAD_PATH + "video" + File.separator;
        FILE_DOWNLOAD_PATH = DOWNLOAD_PATH + "files" + File.separator;
//        UNFILE_DOWNLOAD_PATH = DOWNLOAD_PATH + "unzip" + File.separator;
        APK_DOWNLOAD_PATH = DOWNLOAD_PATH + "apk" + File.separator;

    }

    //--------------------------------------------------------------------------//
    //下载相关
    /**
     * 下载文件的存储地址
     */
    public static String DOWNLOAD_PATH = "";
    /**
     * 图片下载地址
     */
//    public static String PIC_DOWNLOAD_PATH = "";
    /**
     * 录音文件下载存储地址
     */
    public static String RECORD_DOWNLOAD_PATH = "";
    /**
     * 视频文件下载地址
     */
//    public static String VIDEO_DOWNLOAD_PATH = "";
    /**
     * 普通文件下载存储地址
     */
    public static String FILE_DOWNLOAD_PATH = "";
    /**
     * 解压文件存储地址
     */
//    public static String UNFILE_DOWNLOAD_PATH = "";

    //-----------------------------------------------//
    /**
     * APK文件下载地址
     */
    public static String APK_DOWNLOAD_PATH = "";

    // -----------------文件后缀名-------------
    /**
     * 音频文件后缀名
     */
    public static final String AUDIO_FILE_SUFFIX = ".mp3";
    /**
     * 音频文件后缀名
     */
    public static final String SPEECHS_FILE_SUFFIX = ".spx";

    //------------------------------------------------------//
}
