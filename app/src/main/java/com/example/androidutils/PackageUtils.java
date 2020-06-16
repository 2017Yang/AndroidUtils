package com.example.androidutils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.androidutils.customLog.LogManager;
import com.example.androidutils.customLog.Logger;
import java.io.File;

import static com.example.androidutils.MainActivity.MODULE_NAME;

/**
 * Created by Bingo on 2018/3/9.
 */
public class PackageUtils {
    private static final String TAG = PackageUtils.class.getSimpleName();
    private static Logger sLogger = LogManager.getLogger(MODULE_NAME);
    public static final String CLASS_NAME_PACKAGE_PARSER = "android.content.pm.PackageParser";

    /**
     * Utility method to get package information for a given {@link File}
     */
    public static Object getPackageInfo(File sourceFile) {
        final String archiveFilePath = sourceFile.getAbsolutePath();

        Object packageParser = ReflectionUtils.newInstance(
                CLASS_NAME_PACKAGE_PARSER,
                new Class[]{String.class},
                new Object[]{archiveFilePath});

        android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
        metrics.setToDefaults();

        Object pkg = ReflectionUtils.invokeMethod(
                CLASS_NAME_PACKAGE_PARSER,
                "parsePackage",
                new Class[]{File.class, String.class, android.util.DisplayMetrics.class, int.class},
                packageParser,
                new Object[]{sourceFile,
                        archiveFilePath, metrics, 0});

        if (pkg == null) {
            sLogger.e(TAG, " getPackageInfo sourceFile: " + sourceFile + ", pkg is null...");
            return null;
        }
        if (!PackageParserRef.collectManifestDigest(packageParser, new Object[]{pkg})) {
            sLogger.e(TAG, " getPackageInfo collectManifestDigest is false...");
            return null;
        }
        return pkg;
    }

    public static PackageInfo getPackageInfo(Context context){
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            sLogger.e(TAG, e.getLocalizedMessage());
        }
        return  new PackageInfo();
    }

    public static String getPackageName(PackageManager pm, String filePath) {
        String packageName = "";
        PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            packageName = appInfo.packageName;  //获取安装包名称
        }

        sLogger.i(TAG, " getPackageName filePath: " + filePath
                + ", info: " + info
                + ", packageName: " + packageName);

        return packageName;
    }

    public static boolean isInstalled(PackageManager pm, String pgkName) {

        ApplicationInfo info = null;
        try {
            info = pm.getApplicationInfo(pgkName,
                    PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            sLogger.e(TAG, " isInstalled error msg: " + e.getMessage());
        }

        sLogger.i(TAG, "isInstalled pgkName: " + pgkName + ", info: " + info + ", isInstalled: " + (info != null));
        return info != null;
    }
}
