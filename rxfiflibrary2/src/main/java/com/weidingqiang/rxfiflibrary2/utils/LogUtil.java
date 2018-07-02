package com.weidingqiang.rxfiflibrary2.utils;

/**
 * 日志工具类
 * 
 * @author weidingqiang
 * 
 */
public class LogUtil {

    /**
     * 是否打印日志开关
     */
    public static final boolean DEBUG = true;

    /**
     * 输出debug级日志
     * 
     * @param tag
     * @param msg
     */
    public static void debug(String tag, String msg) {
        msg = safeString(msg);
        if (DEBUG) {
            LogToFileUtil.debug(LogUtil.class, tag
                    + ", " + msg);
        }
    }

    /**
     * 输出info级日志
     * 
     * @param tag
     * @param msg
     */
    public static void info(String tag, String msg) {
        msg = safeString(msg);
        if (DEBUG) {
            LogToFileUtil.info(LogUtil.class, tag
                    + ", " + msg);
        }
    }

    /**
     * 输出info级日志
     * 
     * @param tag
     * @param msg
     */
    public static void error(String tag, String msg) {
        msg = safeString(msg);
        if (DEBUG) {
            LogToFileUtil.error(LogUtil.class, tag
                    + ", " + msg);
        }
    }

    /**
     * 输出debug级日志
     * 
     * @param tag
     * @param method
     *            方法名
     * @param msg
     */
    public static void debug(String tag, String method, String msg) {
        method = safeString(method);
        msg = safeString(msg);
        if (DEBUG) {
            LogToFileUtil.debug(LogUtil.class, tag
                    + ", " + "method: " + method + ", " + msg);
        }
    }

    /**
     * 输出info级日志
     * 
     * @param tag
     * @param method
     *            方法名
     * @param msg
     */
    public static void info(String tag, String method, String msg) {
        method = safeString(method);
        msg = safeString(msg);
        if (DEBUG) {
            LogToFileUtil.info(LogUtil.class, tag
                    + ", " + "method: " + method + ", " + msg);
        }
    }

    /**
     * 输出error级日志
     * 
     * @param tag
     * @param method
     *            方法名
     * @param msg
     */
    public static void error(String tag, String method, String msg) {
        method = safeString(method);
        msg = safeString(msg);
        if (DEBUG) {
            LogToFileUtil.error(LogUtil.class, tag
                    + ", " + "method: " + method + ", " + msg);
        }
    }

    /**
     * 防止字符串空指针，出现异常
     * 
     * @param s
     * @return
     */
    public static String safeString(String s) {
        if (s == null) {
            return "null";
        } else {
            return s;
        }
    }
    /**
     * 返回是否是调试模式
     * @return
     */
    public static boolean isDebugMode(){
        return DEBUG;
    }

}
