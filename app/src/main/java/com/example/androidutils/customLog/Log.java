package com.example.androidutils.customLog;


public class Log {
    private static Logger mLogger = LogManager.getLogger(LogManager.defaultModelName);

    public static void v(String tag, String msg, Throwable exp) {
        mLogger.v(tag, msg, exp);
    }

    public static void d(String tag, String msg, Throwable exp) {
        mLogger.d(tag, msg, exp);
    }

    public static void i(String tag, String msg, Throwable exp) {
        mLogger.i(tag, msg, exp);
    }

    public static void w(String tag, String msg, Throwable exp) {
        mLogger.w(tag, msg, exp);
    }

    public static void e(String tag, String msg, Throwable exp) {
        mLogger.e(tag, msg, exp);
    }

    public static void v(String tag, String msg) {
        mLogger.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        mLogger.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        mLogger.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        mLogger.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        mLogger.e(tag, msg);
    }
}
