package com.example.androidutils.customLog;

import java.util.Date;

/**
 * Created by root on 16-4-15.
 */
public class LogEvent {
    private String logType;
    private String tag;
    private String msg;
    private Date dateNow;
    private long threadID;

    public String[] getmMultInfos() {
        return mMultInfos;
    }

    private String[] mMultInfos;

    public LogEvent(String logType, String tag, String msg, String infos) {
        this.logType = logType;
        this.tag = tag;
        this.msg = msg;
        this.threadID = Thread.currentThread().getId();
        this.dateNow = new Date();
    }

    public LogEvent(String logType, String tag, String msg) {
        this(logType, tag, msg, null);
    }

    public Date getDateNow() {
        return dateNow;
    }

    public String getLogType() {
        return logType;
    }

    public String getTag() {
        return tag;
    }

    public long getThreadID() {
        return threadID;
    }

    public String getMsg() {
        return msg;
    }
}
