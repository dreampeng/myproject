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

    @RequestMapping("/miao")
    public Map<String, String> miaozhan() {
        Map<String, String> out = new HashMap<>();
        out.put("code", "0000");
        out.put("data", qzoneService.getMiaoZhan().toJSONString());
        return out;
    }

    @RequestMapping("/start/miao/{psw}")
    public Map<String, String> miaozhan(@PathVariable("psw") String psw) {
        Map<String, String> out = new HashMap<>();
        String code = "0000";
        if (psw.equals("3321")) {
            qzoneService.miaoZhan();
            out.put("data", qzoneService.getMiaoZhan().toJSONString());
        } else {
            code = "9001";
        }
        out.put("code", code);
        return out;
    }

    @RequestMapping("/start/del/{qq}")
    public Map<String, String> delMz(@PathVariable("qq") String qq) {
        Map<String, String> out = new HashMap<>();
        String code = "0000";
        qzoneService.delMz(qq);
        out.put("code", code);
        out.put("data", "已删除");
        return out;
    }

    @RequestMapping("/start/zhan/{qq}")
    public Map<String, String> zhan(@PathVariable("qq") String qq) {
        Map<String, String> out = new HashMap<>();
        String code = "0000";
        qzoneService.addZhan(qq);
        out.put("data", "已开启，完成后会收到邮件一封");
        out.put("code", code);
        return out;
    }
}
