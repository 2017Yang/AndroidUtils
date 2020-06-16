package com.example.androidutils;

import android.content.Context;

public class TimeUtils {

    /**
     * 将秒数转换为日时分秒，
     *
     * @param second
     * @return
     */
    public static String secondToTime(Context context, long second) {
        String dL = context.getString(R.string.day);
        String hL = context.getString(R.string.hour);
        String mL = context.getString(R.string.minute);
        String sL = context.getString(R.string.second);

        long days = second / 86400;            //转换天数
        second = second % 86400;            //剩余秒数
        long hours = second / 3600;            //转换小时
        second = second % 3600;                //剩余秒数
        long minutes = second / 60;            //转换分钟
        second = second % 60;                //剩余秒数
        if (days > 0) {
            return days + dL + hours + hL + minutes + mL + second + sL;
        } else if (hours > 0) {
            return hours + hL + minutes + mL + second + sL;
        } else if (minutes > 0) {
            return minutes + mL + second + sL;
        } else {
            return second + sL;
        }
    }

}
