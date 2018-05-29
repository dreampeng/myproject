package com.noadd.myapp.util.baseUtil;

import java.util.UUID;

public class GUIDUtil {
    public static String createGuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
