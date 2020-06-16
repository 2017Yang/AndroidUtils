package com.example.androidutils;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.example.androidutils.customLog.LogManager;
import com.example.androidutils.customLog.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.example.androidutils.MainActivity.MODULE_NAME;

/**
 * Created by Bingo on 16/12/5.
 */
public class FileUtils {
    private static Logger sLogger = LogManager.getLogger(MODULE_NAME);
    private static final String TAG = FileUtils.class.getSimpleName();

    private final static String PREFIX_VIDEO = "video/";
    private final static String PREFIX_VIDEO_TEXT_TEXMACS = "text/texmacs";
    private final static String SUFFIX_ZIP = "/zip";

    public static final String TYPE_JPG = "jpg";
    public static final String TYPE_GIF = "gif";
    public static final String TYPE_PNG = "png";
    public static final String TYPE_BMP = "bmp";
    public static final String TYPE_UNKNOWN = "unknown";

    public static Drawable getPicture(String path) {
        Drawable drawable = Drawable.createFromPath(path);
        if (drawable == null) {
            sLogger.e(TAG, " getPicture error path: " + path);
            return null;
        } else {
            return drawable;
        }
    }

    /**
     * Get the Mime Type from a File
     *
     * @param fileName 文件名
     * @return 返回MIME类型
     */
    private static String getMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = null;
        try {
            type = fileNameMap.getContentTypeFor(fileName);
        }catch (Exception e) {
            e.printStackTrace();
            sLogger.w(TAG, " getMimeType e.msg: " + String.valueOf(e));
        }
        return type;
    }

    /**
     * 判断件是否是音频文件
     * @param fileName
     * @return
     */
    public static boolean isAudio(String fileName) {
        return MediaFile.isAudioFileType(fileName);
    }

    /**
     * 根据文件后缀名判断 文件是否是视频文件
     *
     * @param fileName 文件名
     * @return 是否是视频文件
     */
    public static boolean isVideo(String fileName) {
        String mimeType = getMimeType(fileName);
        sLogger.i(TAG, " isVideo fileName: " + fileName + ", mimeType: " + mimeType);
        if ((mimeType != null && !TextUtils.isEmpty(fileName)
                && (mimeType.contains(PREFIX_VIDEO)
                || mimeType.contains(PREFIX_VIDEO_TEXT_TEXMACS))) || MediaFile.isVideoFileType(fileName)) {
            sLogger.i(TAG, " isVideo fileName: " + fileName + ", is video ...");
            return true;
        }
        return false;
    }

    /**
     * 根据文件后缀名判断 文件是否是压缩文件
     *
     * @param fileName 文件名
     * @return 是否是视频文件
     */
    public static boolean isZip(String fileName) {
        String mimeType = getMimeType(fileName);
        sLogger.i(TAG, " isZip fileName: " + fileName + ", mimeType: " + mimeType);
        if (mimeType != null
                && !TextUtils.isEmpty(fileName)
                && mimeType.endsWith(SUFFIX_ZIP)) {

            return true;
        }
        return false;
    }


    /**
     * byte数组转换成16进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /**
     * 根据文件流判断图片类型
     *
     * @param fis
     * @return jpg/png/gif/bmp
     */
    public static String getPicType(FileInputStream fis) {
        //读取文件的前几个字节来判断图片格式
        byte[] b = new byte[4];
        int readByte = -1;
        try {
            readByte = fis.read(b, 0, b.length);
            if (readByte >= 0) {
                String type = bytesToHexString(b).toUpperCase();
                sLogger.i(TAG, " getPicType type: " + type);
                if (type.contains("FFD8FF")) {
                    return TYPE_JPG;
                } else if (type.contains("89504E47")) {
                    return TYPE_PNG;
                } else if (type.contains("47494638")) {
                    return TYPE_GIF;
                } else if (type.contains("424D")) {
                    return TYPE_BMP;
                } else {
                    return TYPE_UNKNOWN;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            sLogger.e(TAG, " getPicType error msg: "  + e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    sLogger.e(TAG, " getPicType finally error msg: "  + e.getMessage());
                }
            }
        }
        return null;
    }

    /**
     * 文件的绝对路径
     * @param fileName
     * @return
     */
    public static boolean isPicture(String fileName) {
        String type = null;
        try {
            type = getPicType(new FileInputStream(new File(fileName)));
            sLogger.i(TAG, " isPicture type: " + type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            sLogger.e(TAG, " isPicture error msg: " + e.getMessage());

        }
        return type != null && !TYPE_UNKNOWN.equals(type);
    }

    public static boolean write(File file, String msg) {
        FileOutputStream out = null;
        try {
            if (!file.exists()) {
                sLogger.d(TAG, " FileUtils write fileName is not found, fileName:" + file.getPath());
                if (!file.createNewFile())
                    return false;
            }

            out = new FileOutputStream(file, true);
            out.write(msg.getBytes("utf-8"));
        } catch (IOException e) {
            sLogger.w(TAG, "FileUtils write() caught exception: " + e.getMessage());
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e ) { /* ignore */ }
            }
        }
        return true;
    }

    public static boolean write(String fileName, String msg) {
        return write(new File(fileName), msg);
    }

    public static String read(File file) {
        FileInputStream in = null;
        String readStr = null;
        try {
            if (!file.exists()) {
                sLogger.d(TAG, " FileUtils read fileName is not found, fileName:" + file.getPath());
                return null;
            }
            byte[] bbuf = new byte[1024];
            in = new FileInputStream(file);
            int byteCount = in.read(bbuf);
            if (byteCount != -1)
                readStr = new String(bbuf, 0, byteCount, "UTF-8");
//            if (!TextUtils.isEmpty(readStr)) readStr = readStr.trim();
        } catch (Exception e) {
            sLogger.w(TAG, "FileUtils read() caught exception: " + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) { /* ignore */ }
            }
        }
        return readStr;
    }

    public static String read(String fileName) {
        return read(new File(fileName));
    }

    public static String readLine(File file) {
        StringBuilder text = new StringBuilder();
        BufferedReader br = null;
        try {
            if (!file.exists()) {
                sLogger.i(TAG, " FileUtils read fileName is not found, fileName:" + file.getPath());
                return null;
            }
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line;

            if ((line = br.readLine()) != null) {
                text.append(line);
            }
        }
        catch (IOException e) {
            sLogger.w(TAG, "FileUtils readLine() caught exception: " + e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) { /* ignore */ }
            }
        }
        return text.toString();
    }

    public static String readLine(String fileName) {
        return readLine(new File(fileName));
    }

    public static List<String> readAllLines(File file) {
        List<String> list = new ArrayList<>();
        BufferedReader br = null;
        try {
            if (!file.exists()) {
                sLogger.i(TAG, " FileUtils readAllLines fileName is not found, fileName:" + file.getPath());
                return list;
            }
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line;

            while((line = br.readLine()) != null && !line.isEmpty()) {
                list.add(line);
            }
        }
        catch (IOException e) {
            sLogger.w(TAG, "FileUtils readAllLines() caught exception: " + e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) { /*ignore*/ }
            }
        }
        return list;
    }

    public static List<String> readAllLines(String fileName) {
        return readAllLines(new File(fileName));
    }

    /**
     * 获取文件中的数值，判断成boolean状态。
     * 用于将数字转换成boolean值，主要获取开关状态
     * @param path
     * @return {@code 0}:视作{@code false}，其他情况视为{@code true}
     */
    public static boolean getOnOffState(String path) {
        String readFile = FileUtils.read(path);
        return !TextUtils.isEmpty(readFile) && !("0".equals(readFile.trim()));
    }

    /**
     * 获取文件中的数值，用于文件中只包含数字的情况
     * 通常是获取/sys/目录下的设备状态，如电量、开关等
     * @param path
     * @return int值，其中 {@code -1}:读取失败，可能为文件内容为空或不可转换为数字
     */
    public static int getIntState(String path) {
        return getIntState(path, -1);
    }

    public static int getIntState(String path, int defaultVal) {
        String readFile = FileUtils.read(path);
        return StringUtils.getIntValue(readFile, defaultVal);
    }

    public static boolean copyFile(String sourceFile, String targetFile) {
        sLogger.d(TAG, "enter copyFile");
        int i = 0;
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            if (new File(sourceFile).exists()) {
                sLogger.d(TAG, "sourceFile = " + sourceFile);
                fileInputStream = new FileInputStream(sourceFile);
                fileOutputStream = new FileOutputStream(targetFile);
                byte[] arrayOfByte = new byte[1444];
                while (true) {
                    int j = fileInputStream.read(arrayOfByte);
                    if (j == -1) {
                        break;
                    }
                    i += j;
                    fileOutputStream.write(arrayOfByte, 0, j);
                }
                fileInputStream.close();
                fileOutputStream.close();
                return true;
            }
        } catch (Exception e) {
            sLogger.d(TAG, "复制单个文件操作出错");
            e.printStackTrace();
        }
        return false;
    }

    public static void delFile(String paramString) {
        File file = new File(paramString);
        if (file.exists()) {
            file.delete();
        }
    }
}
