package com.noadd.myapp.util.baseUtil;

import java.util.UUID;

public class UUIDUtil {
    public static String createUuid() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
