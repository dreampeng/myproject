package com.noadd.myapp.util.securityUtil;


import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SecurityUtil {
    private static String publicKey;
    private static String privateKey;

    static {
        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = SecurityUtil.class.getClassLoader().getResourceAsStream("config.properties");
        // 使用properties对象加载输入流
        try {
            properties.load(in);
            //获取key对应的value值
            publicKey = properties.getProperty("publicKey");
            privateKey = properties.getProperty("privateKey");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 解密
     *
     * @param cipherText 密文
     * @return 返回解密后的字符串
     * @throws Exception
     */
    public static String decrypt(String cipherText) throws Exception {
        byte[] decryptByte = RSAUtil.decryptData(Base64Util.decode(cipherText), RSAUtil.loadPrivateKey(privateKey));
        String decryptStr = new String(decryptByte, "utf-8");
        return decryptStr;
    }

    /**
     * 加密
     *
     * @param plainTest 明文
     * @return 返回加密后的密文
     * @throws Exception
     */
    public static String encrypt(String plainTest) throws Exception {
        // 加密
        byte[] encryptByte = RSAUtil.encryptData(plainTest.getBytes(), RSAUtil.loadPublicKey(publicKey));
        String afterencrypt = Base64Util.encode(encryptByte);
        return afterencrypt;
    }

    /**
     * 签名
     *
     * @param signStr 明文
     * @return 签名后内容
     * @throws Exception
     */
    public static String signature(String signStr) throws Exception {
        // 加密
        byte[] encryptByte = RSAUtil.encryptData(signStr.getBytes(), RSAUtil.loadPrivateKey(privateKey));
        String afterencrypt = Base64Util.encode(encryptByte);
        return afterencrypt;
    }

    /**
     * 验证
     *
     * @param signStr 明文
     * @return 签名前内容
     * @throws Exception
     */
    public static String verification(String signStr) throws Exception {
        //验证
        byte[] decryptByte = RSAUtil.decryptData(Base64Util.decode(signStr), RSAUtil.loadPublicKey(publicKey));
        String decryptStr = new String(decryptByte, "utf-8");
        return decryptStr;
    }

    /**
     * 获取密钥对
     *
     * @return
     */
    public static Map<String, String> getKeyMap() {
        Map<String, String> keyMap = new HashMap<>();
        KeyPair keyPair = RSAUtil.generateRSAKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        keyMap.put("privateKey", Base64Util.encode(privateKey.getEncoded()));
        keyMap.put("publicKey", Base64Util.encode(publicKey.getEncoded()));
        return keyMap;
    }

    public static void main(String[] args) throws Exception {
        String str = "pengpai";
        // 加密
        String afterencrypt = encrypt(str);
        // 解密
        String decryptStr = decrypt(afterencrypt);
        System.out.println("原文：" + str);
        System.out.println("加密：" + afterencrypt);
        System.out.println("解密：" + decryptStr);
        System.out.println();
        // 签名
        afterencrypt = signature(str);
        // 验证
        decryptStr = verification(afterencrypt);
        System.out.println("原文：" + str);
        System.out.println("签名：" + afterencrypt);
        System.out.println("验证：" + decryptStr);

    }
}
