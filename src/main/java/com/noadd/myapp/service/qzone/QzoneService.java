package com.noadd.myapp.service.qzone;

import com.alibaba.fastjson.JSONArray;

public interface QzoneService {
    String getQrCode(String qq) throws Exception;

    String login(String qq) throws Exception;

    void miaoZhan();

    JSONArray getMiaoZhan();

    void addMiaoZhan();

    void delMz(String qq);

    void addZhan(String qq);

    void zdZhan();
}
