package com.noadd.myapp.util;


public class MessageUtil {

    public static String sysCodeMsg(String code) {
        String msg;
        switch (code) {
            case "0000":
                msg = "请求成功";
                break;
            case "0001":
                msg = "参数获取错误咯";
                break;
            case "0002":
                msg = "请按提示输入哦";
                break;
            case "0100":
                msg = "用户名已存在";
                break;
            case "0101":
                msg = "邮箱已存在";
                break;
            case "1001":
                msg = "验证码获取失败";
                break;
            case "1002":
                msg = "请重新获取验证码";
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
