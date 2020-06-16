package com.example.androidutils.customLog;

import java.util.Hashtable;

/**
 * Created by yang on 20-04-14.
 */
public abstract class LogManager {
    private static final String TAG = "LogManager";
    public static final String defaultModelName = "ROOT";

    private static LogLevel LEVEL = LogLevel.Verbose;
    private static Hashtable<String, Logger> logHt = new Hashtable<>();
    private static Object htLock = new Object();


    /**
     * 获取日志接口
     * @param modelName 模块名，输出时单条日志加入[--&modelName&--]
     * @return 用于写日志对象
     */
    public static Logger getLogger(String modelName) {
        if (modelName == null || modelName.length() == 0)
            modelName = defaultModelName;
        synchronized (htLock) {
            if (logHt.containsKey(modelName)) {
                return logHt.get(modelName);
            } else {
                Logger logger = new LoggerImpl();
                logger.setLevel(LEVEL);
                logger.setModel(modelName);
                logHt.put(modelName, logger);
                return logger;
            }
        }
    }

    /**
     * 设置日志级别 所有模块的级别
     *
     * @param level
     */
    public static void setLevel(LogLevel level) {
        Log.d(TAG, "setLevel :" + level);
        synchronized (htLock) {
            LEVEL = level;
            for (Logger logger : logHt.values()) {
                logger.setLevel(level);
            }
        }
    }

    public static LogLevel getLevel() {
        return LEVEL;
    }
}
