package com.noadd.myapp.Thread;

import com.alibaba.fastjson.JSONObject;
import com.noadd.myapp.util.baseUtil.FileUtil;
import com.noadd.myapp.util.baseUtil.HttpUtil;
import com.noadd.myapp.util.baseUtil.TimeUtil;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 **/
public class ThreadTemp implements Runnable {
    private String qq;

    public ThreadTemp(String qq) {
        this.qq = qq;
    }


    @Override
    public void run() {
        System.out.println(qq + " 开始执行 " + TimeUtil.getCurrentDate(null));
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36");
        header.put("DNT", "1");
        header.put("Connection", "keep-alive");
        header.put("Upgrade-Insecure-Requests", "1");
        header.put("Cache-Control", "max-age=0");
        header.put("Accept-Encoding", "gzip, deflate");
        header.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
//        String filePath = Objects.requireNonNull(FileUtil.class.getClassLoader().getResource("addr.txt")).getPath();
        String filePath = "/opt/addr.txt";
        List<String> iPath = FileUtil.readFileByLine(filePath);
        iPath.forEach(str -> {
            String hashSaltStr;
            String url = str.substring(0, str.indexOf(" "));
            String tid = str.substring(str.indexOf(" ") + 1);
            try {
                HttpClientContext context = HttpClientContext.create();
                hashSaltStr = HttpUtil.doGet(url, null, header, context);
                int from;
                int to;
                if (!hashSaltStr.contains("var hashsalt")) {
                    from = hashSaltStr.indexOf("setCookie('sec_defend',") + 23;
                    to = hashSaltStr.indexOf(");", from);
                    String sec_defend = getHashSalt("=" + hashSaltStr.substring(from, to));
                    if (sec_defend == null) {
                        System.out.println(qq + " " + url + " " + tid);
                    }

                    setCookie(context, "sec_defend", sec_defend);
                    setCookie(context, "sec_defend_time", "1");
                    hashSaltStr = HttpUtil.doGet(url, null, header, context);
                }
                from = hashSaltStr.indexOf("var hashsalt") + 12;
                to = hashSaltStr.indexOf(";", from);
                hashSaltStr = hashSaltStr.substring(from, to);
                hashSaltStr = getHashSalt(hashSaltStr);
                if (hashSaltStr == null) {
                    System.out.println(qq + " " + url + " " + tid);
                } else {
                    Map<String, String> data = new HashMap<>();
                    data.put("inputvalue", qq);
                    data.put("inputvalue1", qq);
                    data.put("inputvalue2", qq);
                    data.put("tid", tid);
                    data.put("hashsalt", hashSaltStr);
                    data.put("num", "1");
                    String result = HttpUtil.doPost(url + "/ajax.php?act=pay", null, data, header, context);
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    //System.out.println(qq +" "+ url + " " + tid + "----" + jsonObject.getString("msg"));
                }
            } catch (Exception e) {
                System.out.println(qq + " 失败的地址：" + url);
            }
        });
        System.out.println(qq + " 执行完成 " + TimeUtil.getCurrentDate(null));
    }

    private static void setCookie(HttpClientContext context, String name, String value) {
        CookieStore cookieStore = context.getCookieStore();
        if (cookieStore == null || cookieStore.getCookies() == null || cookieStore.getCookies().size() == 0) {
            return;
        }
        Cookie cookieTemp = context.getCookieStore().getCookies().get(0);
        BasicClientCookie newCookie = new BasicClientCookie(name, value);
        newCookie.setDomain(cookieTemp.getDomain());
        newCookie.setExpiryDate(cookieTemp.getExpiryDate());
        newCookie.setPath(cookieTemp.getPath());
        cookieStore.addCookie(newCookie);
    }

    public static String getHashSalt(String hashSaltStr) {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("javascript");
        try {
            scriptEngine.eval("function getHashSalt() {\n" +
                    "    var hashsalt  " + hashSaltStr + ";\n" +
                    "    return hashsalt;\n" +
                    "}\n");
            if (scriptEngine instanceof Invocable) {
                Invocable in = (Invocable) scriptEngine;
                return in.invokeFunction("getHashSalt").toString();
            }
        } catch (Exception ignored) {

        }
        return null;
    }
}
