package com.example.androidutils.customLog;

/**
 * Created by heeter on 2016/4/19.
 */
public enum LogLevel {
    None(0),
    Error(1),
    Warning(2),
    Info(3),
    Debug(4),
    Verbose(5);

    private int level;
    LogLevel(int level){
        this.level = level;
    }
    public int getLevel(){
        return this.level;
    }

    public boolean isInControl(LogLevel inLog){
        if(inLog==null)
            return false;
        if(inLog.level > level) {
            return false;
        }
        return true;
    }


    public static LogLevel getLogLevel(int level){
        switch (level){
            case 0:
                return None;
            case 1:
                return Error;
            case 2:
                return Warning;
            case 3:
                return Info;
            case 4:
                return Debug;
            case 5:
                return Verbose;
        }
        return null;
    }
}
