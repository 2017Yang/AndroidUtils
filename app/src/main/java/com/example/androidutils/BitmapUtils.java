package com.example.androidutils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.androidutils.customLog.LogManager;
import com.example.androidutils.customLog.Logger;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.example.androidutils.MainActivity.MODULE_NAME;

public class BitmapUtils {
    private static final String TAG = BitmapUtils.class.getSimpleName();
    private static Logger sLogger = LogManager.getLogger(MODULE_NAME);

    // 从Resources中加载图片
    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options); // 读取图片长款
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight); // 计算inSampleSize
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeResource(res, resId, options); // 载入一个稍大的缩略图
        return createScaleBitmap(src, reqWidth, reqHeight); // 进一步得到目标大小的缩略图
    }

    /**
     * // 根据磁盘路径，加载磁盘上的图片
     * @param pathName
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFd(String pathName,
                                                   int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        if (src != null) {
            return createScaleBitmap(src, reqWidth, reqHeight);
        } else {
            return null;
        }
    }

    /**
     * 根据磁盘上的Zip包，加载磁盘上的图片
     * @param zf
     * @param ze
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromZip(ZipFile zf, ZipEntry ze,
                                                    int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap src = null;
        try {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(zf.getInputStream(ze), null, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            src = BitmapFactory.decodeStream(zf.getInputStream(ze), null, options);
        } catch (IOException e) {
            e.printStackTrace();
            sLogger.e(TAG, " decodeSampledBitmapFromZip entry.name: " + ze.getName());
        }

        if (src != null) {
            return createScaleBitmap(src, reqWidth, reqHeight);
        } else {
            return null;
        }
    }


    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth,
                                            int dstHeight) {
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }
}