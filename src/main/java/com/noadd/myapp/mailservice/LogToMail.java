package com.noadd.myapp.mailservice;

import com.noadd.myapp.util.baseUtil.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LogToMail {
    @Autowired
    MyMailService myMailServicel;
    @Value("${mail.logMail.addr}")
    private String to;

    public void error(String content, Exception e) {
        String sOut = "";
        StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement s : trace) {
            sOut += "\tat " + s + "\r\n";
        }
        myMailServicel.sendSimpleMail(to, "错误日志" + TimeUtil.getCurrentDate(null), content +
                "\nDetailMessage : " + e.getMessage() + "\n错误详情：" + sOut);
    }

    public void error(String content, Throwable throwable) {
        String sOut = "";
        StackTraceElement[] trace = throwable.getStackTrace();
        for (StackTraceElement s : trace) {
            sOut += "\tat " + s + "\r\n";
        }
        myMailServicel.sendSimpleMail(to, "错误日志" + TimeUtil.getCurrentDate(null), content +
                "\nDetailMessage : " + throwable.getMessage() + "\n错误详情：" + sOut);
    }

    public void warn(String content) {
        myMailServicel.sendSimpleMail(to, "警告日志" + TimeUtil.getCurrentDate(null), content);
    }

    public void info(String content) {
        myMailServicel.sendSimpleMail(to, "消息日志" + TimeUtil.getCurrentDate(null), content);
    }
}
