package com.noadd.myapp.mailservice;

import com.noadd.myapp.util.baseUtil.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LogToMail {
    private final MyMailService myMailService;
    @Value("${mail.logMail.addr}")
    private String to;

    @Autowired
    public LogToMail(MyMailService myMailService) {
        this.myMailService = myMailService;
    }

    public void error(String content, Exception e) {
        StringBuilder sOut = new StringBuilder();
        StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement ste : trace) {
            sOut.append("\t\tat ").append(ste).append("\r\n");
        }
        myMailService.sendSimpleMail(to, "错误日志" + TimeUtil.getCurrentDate(null), content +
                "\n\t错误信息:" + e.getMessage() + "\n\t错误详情:\n" + sOut.toString());
    }

    public void error(String content, Throwable throwable) {
        StringBuilder sOut = new StringBuilder();
        StackTraceElement[] trace = throwable.getStackTrace();
        for (StackTraceElement ste : trace) {
            sOut.append("\t\tat ").append(ste).append("\r\n");
        }
        myMailService.sendSimpleMail(to, "错误日志" + TimeUtil.getCurrentDate(null), content +
                "\n\t错误信息:" + throwable.getMessage() + "\n\t错误详情:\n" + sOut.toString());
    }

    public void warn(String content) {
        myMailService.sendSimpleMail(to, "警告日志" + TimeUtil.getCurrentDate(null), content);
    }

    public void info(String content) {
        myMailService.sendSimpleMail(to, "消息日志" + TimeUtil.getCurrentDate(null), content);
    }
}
