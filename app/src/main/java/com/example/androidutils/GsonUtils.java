package com.example.androidutils;

import com.example.androidutils.customLog.LogManager;
import com.example.androidutils.customLog.Logger;
import com.google.gson.Gson;

import static com.example.androidutils.MainActivity.MODULE_NAME;

/**
 * Created by Bingo on 16/11/26.
 */
public class GsonUtils {
    private static Logger sLogger = LogManager.getLogger(MODULE_NAME);
    private static String TAG = GsonUtils.class.getCanonicalName();

    public static String toJson(Object object) {
        String infoStr = "";
        if (object == null) {
            sLogger.d("object is null");
            return infoStr;
        }
        try {
            Gson gson = new Gson();
            infoStr = gson.toJson(object);
        } catch (Exception e) {
            sLogger.d("Change Class To Gson String Failed");
        }
        return infoStr;
    }

}
