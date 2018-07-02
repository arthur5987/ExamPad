package com.weidingqiang.rxfiflibrary2.utils;

import com.weidingqiang.rxfiflibrary2.app.AppConstants;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import de.mindpipe.android.logging.log4j.LogConfigurator;


/**
 * 写日志到文件的工具类
 * 
 * @author weidingqiang
 * 
 */
public class LogToFileUtil {

    /**
     * 输出到日志文件开关
     */
    private static final boolean isLogToFile = true;

    /**
     * log文件存放路径
     * 路径为 /storage/emulated/0/Android/data/com.fifedu.mhk/cache/logs/
     */
//    private static final String LOG_PATH;

    /**
     * 最多存放log的数量
     */
    private static final int LOG_NUM_RESERVED = 50;

    /**
     * 初始化
     */
    public static void init() {
        if (isLogToFile) {
            try { // 防止没有sd卡的情况
                LogConfigurator logConfigurator = new LogConfigurator();
                Date date = new Date(System.currentTimeMillis());
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");//yyyyMMddHHmmssSSS
                String logFileName = df.format(date) + ".txt";
                logConfigurator.setFileName(AppConstants.LOGS_PATH + logFileName);
                logConfigurator.setRootLevel(Level.DEBUG);
                logConfigurator.setLevel("org.apache", Level.ERROR);
                logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
                logConfigurator.setMaxFileSize(1024 * 1024 * 5);
                logConfigurator.setImmediateFlush(true);
                logConfigurator.configure();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 清除过旧日志文件
     */
    public static void clearOldFiles() {
        if (isLogToFile) {
            File file = new File(AppConstants.LOGS_PATH);
            if (file.exists() && file.isDirectory()) {
                String[] fns = file.list();
                if (fns.length > LOG_NUM_RESERVED) {
                    long[] fnInt = new long[fns.length];
                    for (int i = 0; i < fnInt.length; i++) {
                        try {
                            fnInt[i] = Long.parseLong(fns[i].substring(0, fns[i].length() - 4));
                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                    Arrays.sort(fnInt);
                    // 最多留一定数量日志文件，删除旧文件
                    for (int i = 0; i < fnInt.length - LOG_NUM_RESERVED; i++) {
                        String fn2 = String.valueOf(fnInt[i]) + ".txt";
                        File file2 = new File(AppConstants.LOGS_PATH + fn2);
                        if (file2.exists()) {
                            file2.delete();
                        }
                    }
                }
            }
        }
    }

    /**
     * 输出info
     * 
     * @param tag
     * @param msg
     */
    public static void info(Class<?> tag, String msg) {
        if (isLogToFile) {
            Logger.getLogger(tag).info(msg);
        }
    }

    /**
     * 输出debug
     * 
     * @param tag
     * @param msg
     */
    public static void debug(Class<?> tag, String msg) {
        if (isLogToFile) {
            Logger.getLogger(tag).debug(msg);
        }
    }

    /**
     * 输出error
     * 
     * @param tag
     * @param msg
     */
    public static void error(Class<?> tag, String msg) {
        if (isLogToFile) {
            Logger.getLogger(tag).error(msg);
        }
    }

}
