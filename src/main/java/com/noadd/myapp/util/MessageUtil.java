package com.noadd.myapp.util;


public class MessageUtil {

    public static String sysCodeMsg(String code) {
        String msg;
        switch (code) {
            case "0000":
                msg = "请求成功";
                break;
            case "9999":
                msg = "系统错误请联系管理员";
                break;
            default:
                msg = "发现未定义的错误:" + code;
                break;
        }
        return msg;
    }

}
