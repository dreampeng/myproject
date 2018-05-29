package com.noadd.myapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtil {

    //用户名
    public final static String userName = "^[a-zA-Z][a-zA-Z0-9_]{5,11}$";
    //密码
    public final static String userPass = "^[a-zA-Z0-9_]{6,12}$";
    //邮箱
    public final static String email = "^\\w+([-+.]\\w+)*@qq.com*$";
    //验证码
    public final static String validCode = "^[a-zA-Z0-9]{6}$";

    public final static boolean regularMatch(String str, String pattern) {
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(str);
        return !m.find();
    }
}
