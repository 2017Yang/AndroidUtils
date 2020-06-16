package com.example.androidutils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by yangyong on 19-10-9.
 */

public class FileMd5Utils {

    public static String getFileMD5(String filePath) {
        File file = new File(filePath);
        return getFileMD5(file);
    }


    private static String getFileMD5(File file) {
        if (!file.isFile()) {
            Log.d("cn.starnet.settings", "[MD5]:not file");
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("cn.starnet.settings", "[MD5]:Exception1");
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        Log.d("cn.starnet.settings", "[MD5]:ok!");
        return String.format("%032x",bigInt);
    }
}
