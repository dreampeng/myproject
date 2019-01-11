package com.noadd.myapp.controller;

import com.noadd.myapp.service.qzone.QzoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 **/
@RestController
@RequestMapping("/japi/qzone")
public class QzoneController {
    @Autowired
    QzoneService qzoneService;

    @RequestMapping("/qrcode")
    public Map<String, String> qrCode(String qq) throws Exception {
        Map<String, String> out = new HashMap<>();
        out.put("code", "0000");
        out.put("path", qzoneService.getQrCode(qq));
        return out;
    }

    @RequestMapping("/login")
    public Map<String, String> toLogin(String qq, String path) throws Exception {
        Map<String, String> out = new HashMap<>();
        out.put("code", "0000");
        out.put("qq", qzoneService.login(qq, path));
        return out;
    }

    @RequestMapping("/miao")
    public Map<String, String> miaozhan(String qq) throws Exception {
        Map<String, String> out = new HashMap<>();
        out.put("code", "0000");
        qzoneService.miaoZhan(qq);
        return out;
    }
}
