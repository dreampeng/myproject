package com.noadd.myapp.controller;

import com.noadd.myapp.mailservice.MyMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    MyMailService mailService;

    @RequestMapping("/{userName}/{loginPass}")
    public String welcome(@PathVariable("userName") String userName, @PathVariable("loginPass") String loginPass) {
        return "welcome, Tourist. <a href='index.php'>PHP Info.</a>";
    }

    @RequestMapping("/mail")
    public String sendMail() {
        mailService.sendSimpleMail("1022501366@qq.com", "这里是测试邮件发送的标题", "这里是测试邮件发送的内容");
        return "邮件已发送";
    }
}
