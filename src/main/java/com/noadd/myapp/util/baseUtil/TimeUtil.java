package com.noadd.myapp.util.baseUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    private static String defaultDateFormat = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前时间的时间戳
     *
     * @return
     */
    public static long getTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取当前时间字符串
     */
    public static String getCurrentDate(String dateformat) {
        if (StringUtil.isEmpty(dateformat)) {
            dateformat = defaultDateFormat;
        }
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
        return sdf.format(d);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getDate() {
        return new Date();
    }

    /**
     * 将时间戳转换为时间
     *
     * @throws ParseException
     */
    public static String stampToDate(Long s, String dateformat)
            throws ParseException {
        if (StringUtil.isEmpty(dateformat)) {
            dateformat = defaultDateFormat;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
        Long time = new Long(s) * 1000;
        Date date = new Date(time);
        return sdf.format(date);
    }

    /**
     * 将时间转换为时间戳
     */
    public Long dateToStamp(Date date) {
        Long stamp = date.getTime() / 1000;
        return stamp;
    }

    /**
     * 时间加减
     *
     * @param day
     * @param hour
     * @param min
     * @param sec
     * @return
     */
    public static Long stampAdd(Long stamp, int day, int hour, int min, int sec) {
        stamp = stamp + (day * 24 * 60 * 60) + (hour * 60 * 60) + (min * 60) + sec;
        return stamp;
    }
}
