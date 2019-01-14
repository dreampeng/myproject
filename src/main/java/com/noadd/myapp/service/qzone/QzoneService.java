package com.noadd.myapp.service.qzone;

public interface QzoneService {
    String getQrCode(String qq) throws Exception;
    String login(String qq) throws Exception;
    String miaoZhan(String qq) throws Exception;
}
