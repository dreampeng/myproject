package com.noadd.myapp.util;

import java.lang.reflect.Field;

public class ParamUtil {
    public static boolean registvalidate(String userName, String userPass, String email) {
        if (RegularUtil.regularMatch(userName, RegularUtil.userName)) {
            return false;
        }
        if (RegularUtil.regularMatch(userPass, RegularUtil.userPass)) {
            return false;
        }
        if (RegularUtil.regularMatch(email, RegularUtil.email)) {
            return false;
        }
        return true;
    }
}
