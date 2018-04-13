package com.noadd.myapp.util.SmsUtil;

/**
 * 短信发送工具
 */
public class SmsSendUtil {

    private static String APP_ID;
//
//    static {
//        InputStream inputStream = SmsSendUtil.class.getClassLoader()
//                .getResourceAsStream("public.properties");
//        Properties prop = new Properties();
//        try {
//            prop.load(inputStream);
//        } catch (IOException e) {
//            log.error(e);
//        }
//        APP_ID = prop.getProperty("APP_ID");
//    }

    /**
     * 发送短信验证码
     *
     * @param phone
     * @param content
     * @return
     */
    public static boolean sendSms(String phone, String content) {
        //TODO 没有写内容
        return true;
    }
}
