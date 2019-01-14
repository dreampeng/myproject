package com.noadd.myapp.controller;

import com.noadd.myapp.service.qzone.QzoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/qrcode/{qq}")
    public Map<String, String> qrCode(@PathVariable String qq) throws Exception {
        Map<String, String> out = new HashMap<>();
        out.put("code", "0000");
        out.put("path", qzoneService.getQrCode(qq));
        return out;
    }

    @RequestMapping("/login/{qq}")
    public Map<String, String> toLogin(@PathVariable String qq) throws Exception {
        Map<String, String> out = new HashMap<>();
        out.put("code", "0000");
        out.put("qq", qzoneService.login(qq));
        return out;
    }

    @RequestMapping("/miao/{qq}")
    public Map<String, String> miaozhan(@PathVariable String qq) throws Exception {
        Map<String, String> out = new HashMap<>();
        out.put("code", "0000");
        out.put("data",qzoneService.miaoZhan(qq));
        return out;
    }
}
