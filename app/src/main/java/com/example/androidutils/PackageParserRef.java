package com.example.androidutils;

import android.content.pm.PackageInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Bingo on 2018/3/23.
 */
public class PackageParserRef {
    public static final String CLASS_NAME_PACKAGE_PARSER = "android.content.pm.PackageParser";

    public static boolean collectManifestDigest(Object object, Object[] args) {
        return (boolean)ReflectionUtils.invokeMethod(
                CLASS_NAME_PACKAGE_PARSER,
                "collectManifestDigest",
                new Class[]{ReflectionUtils.forName("android.content.pm.PackageParser$Package")},
                object,
                args);
    }

    public static PackageInfo generatePackageInfo(Object[] args) {
        Set<String> dsd = new HashSet<>();
        return (PackageInfo)ReflectionUtils.invokeMethod(
                CLASS_NAME_PACKAGE_PARSER,
                "generatePackageInfo",
                new Class[]{
                        ReflectionUtils.forName("android.content.pm.PackageParser$Package"),
                        int[].class,
                        int.class,
                        long.class,
                        long.class,
                        dsd.getClass(),
                        ReflectionUtils.forName("android.content.pm.PackageUserState")
                },
                args);
    }

}
