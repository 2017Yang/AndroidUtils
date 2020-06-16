package com.example.androidutils.customLog;


/**
 * Created by nzery on 16-3-4.
 */
public interface Logger {
    void v(String tag, String msg, Throwable exception);
    void d(String tag, String msg, Throwable exception);
    void i(String tag, String msg, Throwable exception);
    void w(String tag, String msg, Throwable exception);
    void e(String tag, String msg, Throwable exception);

    void v(String tag, String msg);
    void d(String tag, String msg);
    void i(String tag, String msg);
    void w(String tag, String msg);
     void e(String tag, String msg);

    @Deprecated
    void v(String msg);
    @Deprecated
    void d(String msg);
    @Deprecated
    void i(String msg);
    @Deprecated
    void w(String msg);
    @Deprecated
    void e(String msg);
    @Deprecated
    void exception(String tag, Exception e);
    void setLevel(LogLevel level);
    void setModel(String modelName);
}
