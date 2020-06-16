package com.example.androidutils;

/**
 * Created by Bingo on 2018/3/23.
 */
public class PackageManagerRef {
    public static final int GET_UNINSTALLED_PACKAGES = 0x00002000;
    public static final int INSTALL_REPLACE_EXISTING = 0x00000002;
    public static final int INSTALL_SUCCEEDED = 1;
    public static final int INSTALL_FAILED_INSUFFICIENT_STORAGE = -4;
    public static final int INSTALL_FAILED_INVALID_APK = -2;
    public static final int INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES = -104;
    public static final int INSTALL_FAILED_OLDER_SDK = -12;
    public static final int INSTALL_FAILED_CPU_ABI_INCOMPATIBLE = -16;
}
