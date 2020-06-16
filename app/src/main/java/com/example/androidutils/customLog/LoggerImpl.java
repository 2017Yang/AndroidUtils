package com.example.androidutils.customLog;

import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * Created by nzery on 16-3-5.
 */
class LoggerImpl implements Logger {

    private LogLevel level = LogLevel.Verbose;
    private boolean mLogFlag = true;

    private String modelName = LogManager.defaultModelName; //

    private String getExceptionInfo(Throwable exp) {
        if (exp == null) {
            return null;
        }

        String expMessage = null;
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            exp.printStackTrace(pw);
            expMessage = sw.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pw != null)
                    pw.close();
                if (sw != null)
                    sw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return expMessage;
    }

    private String getDebugInfo(String msg, String exInfo) {
        if (exInfo == null) {
            return msg;
        } else {
            return msg + exInfo;
        }
    }
    public boolean isNullOrEmpty(String str) {
        return str == null || str.trim().equals("") || str.length() == 0;
    }


    /**
     * Get The Current Function Name
     * @return
     */
    private String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if(sts == null) {
            return null;
        }
        for(StackTraceElement st : sts) {
            if(st.isNativeMethod()) {
                continue;
            }
            if(st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if(st.getClassName().equals(this.getClass().getName())) {
                continue;
            }
            return "[ " + Thread.currentThread().getName() + ": "
                    + st.getFileName() + ":" + st.getLineNumber() + " "
                    + st.getMethodName() + " ]";
        }
        return null;
    }

    /**
     *  方式一
     *  tag:    自定义的标签
     *  msg:    信息
     *  Throwable exception
     *  [AppName]: TAG [ main: MainActivity.java:20 onCreate ]
     */
    @Override
    public void v(String tag, String msg, Throwable exception) {
        String exInfo = getExceptionInfo(exception);
        String debugMsg = getDebugInfo(msg, exInfo);
        if(mLogFlag) {
            if(level.isInControl(LogLevel.Verbose)) {
                String name = getFunctionName();
                if (name != null) {
                    msg = name + " - " + debugMsg;
                } else {
                    msg = debugMsg.toString();
                }
                if (isNullOrEmpty(tag)) {
                    Log.v(modelName, msg);
                } else {
                    Log.v(modelName, tag + msg);
                }
            }
        }
    }

    @Override
    public void d(String tag, String msg, Throwable exception) {
        String exInfo = getExceptionInfo(exception);
        String debugMsg = getDebugInfo(msg, exInfo);
        if(mLogFlag) {
            if(level.isInControl(LogLevel.Debug)) {
                String name = getFunctionName();
                if (name != null) {
                    msg = name + " - " + debugMsg;
                } else {
                    msg = debugMsg.toString();
                }
                if (isNullOrEmpty(tag)) {
                    Log.d(modelName, msg);
                } else {
                    Log.d(modelName, tag + msg);
                }
            }
        }
    }

    @Override
    public void i(String tag, String msg, Throwable exception) {
        String exInfo = getExceptionInfo(exception);
        String debugMsg = getDebugInfo(msg, exInfo);
        if(mLogFlag) {
            if(level.isInControl(LogLevel.Info)) {
                String name = getFunctionName();
                if (name != null) {
                    msg = name + " - " + debugMsg;
                } else {
                    msg = debugMsg.toString();
                }
                if (isNullOrEmpty(tag)) {
                    Log.i(modelName, msg);
                } else {
                    Log.i(modelName, tag + msg);
                }
            }
        }
    }

    @Override
    public void w(String tag, String msg, Throwable exception) {
        String exInfo = getExceptionInfo(exception);
        String debugMsg = getDebugInfo(msg, exInfo);
        if(mLogFlag) {
            if(level.isInControl(LogLevel.Warning)) {
                String name = getFunctionName();
                if (name != null) {
                    msg = name + " - " + debugMsg;
                } else {
                    msg = debugMsg.toString();
                }
                if (isNullOrEmpty(tag)) {
                    Log.w(modelName, msg);
                } else {
                    Log.w(modelName, tag + msg);
                }
            }
        }
    }

    @Override
    public void e(String tag, String msg, Throwable exception) {
        String exInfo = getExceptionInfo(exception);
        String debugMsg = getDebugInfo(msg, exInfo);
        if(mLogFlag) {
            if(level.isInControl(LogLevel.Error)) {
                String name = getFunctionName();
                if (name != null) {
                    msg = name + " - " + debugMsg;
                } else {
                    msg = debugMsg.toString();
                }
                if (isNullOrEmpty(tag)) {
                    Log.e(modelName, msg);
                } else {
                    Log.e(modelName, tag + msg);
                }
            }
        }
    }

    /**
     * 方式二
     *  tag:    自定义的标签
     *  msg:    信息
     *  [AppName]: TAG [ main: MainActivity.java:20 onCreate ]
    */
    @Override
    public void v(String tag, String msg) {
        v(tag, msg, null);
    }

    @Override
    public void d(String tag, String msg) {
        d(tag, msg, null);
    }

    @Override
    public void i(String tag, String msg) {
        i(tag, msg, null);
    }

    @Override
    public void w(String tag, String msg) {
        w(tag, msg, null);
    }

    @Override
    public void e(String tag, String msg) {
        e(tag, msg, null);
    }

    /**
     * 方式三
     *   没有自定义的标签,统一
     *  msg:   输出信息
     *  [AppName]:[ main: MainActivity.java:20 onCreate ]  msg
     */
    @Override
    public void v(String msg) {
        v(null, msg);
    }

    @Override
    public void d(String msg) {
        d(null, msg);
    }

    @Override
    public void i(String msg) {
        i(null, msg);
    }

    @Override
    public void w(String msg) {
        w(null, msg);
    }

    @Override
    public void e(String msg) {
        e(null, msg);
    }

    @Override
    public void exception(String tag, Exception e) {
        if (!level.isInControl(LogLevel.Error))
            return;
        e(tag, "exception", e);
    }

    @Override
    public void setLevel(LogLevel level) {
        this.level = level;
    }

    @Override
    public void setModel(String modelName) {
        if (isNullOrEmpty(modelName)) {
            this.modelName = "[--" +  this.modelName + "--]";
        } else {
            this.modelName = "[--" + modelName + "--]";
        }
    }
}
