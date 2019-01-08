/**
 * Copyright (c) 2015 SIH, Inc. All rights reserved.
 * This software is the confidential and proprietary information of
 * SIH, Inc. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SIH.
 */
package com.noadd.myapp.util.baseUtil;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @ClassName: StringUtil.java
 */
public class StringUtil {

    /**
     * 获取UUID
     *
     * @return uuid
     */
    public static String getUuid() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.toUpperCase().replaceAll("-", "");
        return uuid;
    }
    /**
     * 将以某种分隔符分隔的字符串转化为long数组
     *
     * @param str
     * @return Long[]
     * @Title: strToLong
     */
    public static Long[] strToLongArray(String str, String split) {
        if (isEmpty(str)) {
            return null;
        }
        String[] strs = str.split(split);
        Long[] ids = new Long[strs.length];
        for (int i = 0; i < strs.length; i++) {
            ids[i] = Long.valueOf(strs[i]);
        }
        return ids;
    }

    public static boolean isEmpty(String... strs) {
        for (String str : strs) {
            if (str == null || "".equals(str)) {
                return true;
            }
        }
        return false;
    }


    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 在每个字符中间添加\r\n
     *
     * @param s
     * @return
     */
    public static String allStrIntervalAddNewlines(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        StringBuffer str = new StringBuffer();
        str.append(s.charAt(0));
        for (int i = 1; i < s.length(); i++) {
            str.append("\r\n" + s.charAt(i));
        }
        return str.toString();
    }

    public static boolean isNumerLetteric(String str) {
        String regex = "^[a-z0-9A-Z]+$";
        return str.matches(regex);
    }

    public static boolean isChinese(String str) {
        String regex = "[\\u4e00-\\u9fa5]*";
        return str.matches(regex);
    }

    /**
     * 获取长度为N随机数字货字母
     *
     * @param n
     * @return
     */
    public static String getRandomStr(int n) {
        String[] ss = {
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3",
                "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
                "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
                "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
                "y", "z"};
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; i++) {
            int a = (int) (Math.random() * 62);
            sb.append(ss[a]);
        }
        return sb.toString();
    }

    /**
     * 获取长度为N的随机数字
     *
     * @param n
     * @return
     */
    public static String getRandomNumStr(int n) {
        String[] ss = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; i++) {
            int a = (int) (Math.random() * 10);
            sb.append(ss[a]);
        }
        return sb.toString();
    }


    /**
     * 获取长度为N随机字符串
     *
     * @param n
     * @param lowup 0 不限制大小写  1 只要小写  2 只要大写
     * @return
     */
    public static String getRandomLetter(int n, int lowup) {
        String[] ss = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d",
                "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
                "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
                "y", "z"};
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; i++) {
            int a = (int) (Math.random() * 52);
            if (lowup == 0) {

            } else if (lowup == 1) {
                if (a < 25) {
                    a += 26;
                }
            } else if (lowup == 2) {
                if (a > 24) {
                    a -= 26;
                }
            }
            sb.append(ss[a]);
        }

        return sb.toString();
    }

}
