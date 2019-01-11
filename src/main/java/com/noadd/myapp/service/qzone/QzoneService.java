package com.noadd.myapp.service.qzone;

public interface QzoneService {
    String getQrCode(String qq) throws Exception;
    String login(String qq,String qrPath) throws Exception;
    void miaoZhan(String qq);
}
