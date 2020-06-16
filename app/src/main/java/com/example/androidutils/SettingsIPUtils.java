package com.example.androidutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bingo on 16/7/1.
 */
public class SettingsIPUtils {
    public static final String DEF_MASK = "255.255.255.0";
    public static final String DEF_DNS = "8.8.8.8";

    public static String getDefGateWay(String ip) {
        String defGateWay = "";
        if (!StringUtils.isNullOrEmpty(ip)) {
            defGateWay = ip.substring(0, ip.lastIndexOf('.') + 1) + "1";
        }
        return defGateWay;
    }

    public static boolean checkPort(String portStr) {
        boolean flag = true;
        try {
            int port = Integer.parseInt(portStr);
            if (port < 0 || port > 65535) {
                flag = false;
            }
        } catch (NumberFormatException var4) {
            flag = false;
        }
        return flag;
    }

    public static boolean isValidNetMask(String value) {
        String validMaskReg = "^(254|252|248|240|224|192|128|0)\\.0\\.0\\.0$" +
                "|^(255\\.(254|252|248|240|224|192|128|0)\\.0\\.0)$" +
                "|^(255\\.255\\.(254|252|248|240|224|192|128|0)\\.0)$" +
                "|^(255\\.255\\.255\\.(254|252|248|240|224|192|128|0))$";

        Pattern pattern = Pattern.compile(validMaskReg);
        Matcher matcher = pattern.matcher(value);

        return matcher.matches();
    }

    public static boolean isValidMask(String value) {
        int start = 0;
        int end = value.indexOf('.');
        int numBlocks = 0;
        boolean has_zero = false;
        int block;

        if ("".equals(value)) return true;

        while (start < value.length()) {
            if (end == -1) {
                end = value.length();
            }
            try {
                block = Integer.parseInt(value.substring(start, end));
                if ((block > 255) || (block < 0) || (end - start) > 3) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
            numBlocks++;
            start = end + 1;
            end = value.indexOf('.', start);

            int dat = 0x80;
            if (block == 255)
                continue;
            if (block == 0) {
                has_zero = true;
                continue;
            }
            while (dat != 0) {
                if (((dat & block) != 0) && (has_zero == true)) {
                    return false;
                } else if (((dat & block) == 0) && (has_zero == false)) {
                    has_zero = true;
                }
                dat = (dat >> 1);
            }
        }

        return numBlocks == 4;
    }

    public static boolean isValidIpAddress(String ipaddr) {
        boolean flag = false;
        Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
        Matcher m = pattern.matcher(ipaddr);
        flag = m.matches();
        return flag;
    }

}
