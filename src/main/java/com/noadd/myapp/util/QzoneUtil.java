package com.noadd.myapp.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.noadd.myapp.util.baseUtil.FileUtil;
import com.noadd.myapp.util.baseUtil.HttpUtil;
import com.noadd.myapp.util.baseUtil.StringUtil;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QzoneUtil {
    private HttpClientContext context;
    private Map<String, Object> cookieMap;

    public QzoneUtil() {
        context = HttpClientContext.create();
    }

    /**
     * 获取单页说说
     *
     * @param page   页数
     * @param limit  每页数量
     * @param ssJson 上次调用本方法返回的JSONObject
     * @throws Exception 错误
     */
    public void getOnePageSs(Integer page, Integer limit, JSONObject ssJson) throws Exception {
        String url = "https://user.qzone.qq.com/proxy/domain/taotao.qq.com/cgi-bin/emotion_cgi_msglist_v6";
        Map<String, String> param = new HashMap<>();
        param.put("uin", (String) getCookieMap().get("qq_num"));
        param.put("pos", Integer.toString((page - 1) * limit));
        param.put("num", Integer.toString(limit));
        param.put("replynum", "100");
        param.put("code_version", "1");
        param.put("format", "json");
        param.put("need_private_comment", "1");
        param.put("g_tk", getG_tk());
        Map<String, String> header = new HashMap<>();
        header.put("referer", "https://qzs.qq.com/qzone/app/mood_v6/html/index.html");
        header.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36");
        JSONObject result = JSONObject.parseObject(HttpUtil.doGet(url, param, header, context));
        int code = result.getInteger("code");
        ssJson.put("code", code);
        if (code != 0) {
            return;
        }
        JSONArray msgList = ssJson.getJSONArray("msgList") == null ? new JSONArray() : ssJson.getJSONArray("msgList");
        List<String> tidList = (List<String>) (ssJson.get("tidList") == null ? new ArrayList<String>() : ssJson.get("tidList"));
        JSONArray msglistTemp = result.getJSONArray("msglist");
        int total = result.getInteger("total");
        ssJson.put("total", total);
        for (Object temp : msglistTemp) {
            JSONObject msgTemp = new JSONObject();
            JSONObject msg = (JSONObject) temp;
            //正文
            String content = msg.getString("content");
            msgTemp.put("content", content);
            //说说图片地址
            List<String> picUrlpics = new ArrayList<>();
            for (Object picTemp : msg.getJSONArray("pic") == null ? new JSONArray() : msg.getJSONArray("pic")) {
                picUrlpics.add(((JSONObject) picTemp).getString("url1"));
            }
            msgTemp.put("picUrlpics", picUrlpics);
            //tid
            String tid = msg.getString("tid");
            msgTemp.put("tid", tid);
            if (tidList.contains(tid)) {
                System.out.println(tid);
                continue;
            } else {
                tidList.add(tid);
            }
            //回复列表
            JSONArray commentList = new JSONArray();
            for (Object tempObj : msg.getJSONArray("commentlist") == null ? new JSONArray() : msg.getJSONArray("commentlist")) {
                JSONObject tempJson = (JSONObject) tempObj;
                JSONObject tempComment = new JSONObject();
                tempComment.put("tid", tempJson.getString("tid"));
                tempComment.put("content", tempJson.getString("content"));
                tempComment.put("createTime", tempJson.getString("createTime"));
                tempComment.put("name", tempJson.getString("name"));
                JSONArray list3 = new JSONArray();
                for (Object tempTempObj : tempJson.getJSONArray("list_3") == null ? new JSONArray() : tempJson.getJSONArray("list_3")) {
                    JSONObject tempTempJson = (JSONObject) tempTempObj;
                    JSONObject tempTempComment = new JSONObject();
                    tempTempComment.put("content", tempTempJson.getString("content"));
                    tempTempComment.put("name", tempTempJson.getString("name"));
                    tempTempComment.put("createTime", tempTempJson.getString("createTime"));
                    list3.add(tempTempComment);
                }
                tempComment.put("list3", list3);
                commentList.add(tempComment);
            }
            msgTemp.put("commentList", commentList);
            //来源
            String sourceName = msg.getString("source_name");
            msgTemp.put("sourceName", sourceName);
            //发送时间
            String createTime = msg.getString("createTime");
            msgTemp.put("createTime", createTime);
            msgList.add(msgTemp);
        }
        ssJson.put("tidList", tidList);
        ssJson.put("msgList", msgList);
    }

    /* 获取has33
     public static int hash33(String o) {
     int t = 0, e = 0, n = o.length();
     for (; e < n; ++e) {
     t += (t << 5) + o.charAt(e);
     }
     return 2147483647 & t;
     }
     */

    /**
     * 删除说说
     *
     * @param tid cookie值
     * @return 删除结果
     */
    public JSONObject deleteSs(String tid) {
        String url = "https://user.qzone.qq.com/proxy/domain/taotao.qzone.qq.com/cgi-bin/emotion_cgi_delete_v6";
        Map<String, String> param = new HashMap<>();
        param.put("qzonetoken", (String) getCookieMap().get("qzonetoken"));
        param.put("g_tk", getG_tk());
        Map<String, String> dataParam = new HashMap<>();
        dataParam.put("hostuin", (String) getCookieMap().get("qq_num"));
        dataParam.put("tid", tid);
        dataParam.put("code_version", "1");
        dataParam.put("format", "json");
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        return JSONObject.parseObject(HttpUtil.doPost(url, param, dataParam, header, context));
    }

    /**
     * 获取当前用户说有说说
     *
     * @return 结果
     * @throws Exception error
     */
    public JSONObject getAllSs() throws Exception {
        Map<String, Object> cookieMap = getCookieMap();
        JSONObject ss = new JSONObject();
        int limit = 20, total, page = 1;
        while (true) {
            try {
                getOnePageSs(page, limit, ss);
                if ("-3000".equals(ss.getString("code"))) {
                    System.out.println("QQ号:" + cookieMap.get("qq_num") + "登录失败");
                    break;
                }
                total = ss.getInteger("total");
            } catch (Exception e) {
                continue;
            }
            System.out.println("已取到" + page++ + "页，共" + ss.getJSONArray("msgList").size() + "条，总共" + total + "条");
            if ((page * limit) > total) {
                break;
            }
        }
        System.out.println(ss);
        return ss;
    }

    /**
     * 删除说说
     *
     * @param tids 说说tidlist
     */
    public void deleteSs(List<String> tids) {
        int i = 0, x = 0, y = 0;
        while (i < tids.size()) {
            String tid = tids.get(i);
            JSONObject result = deleteSs(tid);
            if (result != null) {
                int code = (int) result.get("code");
                int subcode = (int) result.get("subcode");
                if (code == 0 && subcode != -200) {
                    System.out.println("刪除成功tid:" + tid);
                    x++;
                }
                if (code == -3001) {
                    break;
                } else {
                    y++;
                }
            } else {
                y++;
            }
            i++;
        }
        System.out.println("成功" + x + "条，失败" + y + "条，之后超时");
    }

    private static String getQrqrtoken(String qrsig) {
        int t = 0,
                e = 0,
                n = qrsig.length();
        for (; e < n; ++e)
            t += (t << 5) + qrsig.charAt(e);
        return Integer.toString(2147483647 & t);
    }

    /**
     * 获取二维码图片
     *
     * @return 二维码地址
     * @throws Exception error
     */
    public String ptqrshow() throws Exception {
        String ptqrshowUrl = "https://ssl.ptlogin2.qq.com/ptqrshow?" +
                "appid=549000912&e=2&l=M&s=3&d=72&v=4&t=0.23397805982077213&daid=5&pt_3rd_aid=0";
        String path = "C:\\Users\\Administrator\\Desktop\\" + StringUtil.getUuid() + ".png";
        HttpUtil.doGetPic(ptqrshowUrl, path, null, null, context);
        return path;
    }

    /**
     * 二维码轮询
     *
     * @return 结果
     * @throws Exception error
     */
    public String ptqrlogin() throws Exception {
        Map<String, Object> cookieMap = getCookieMap();
        String qrsig = (String) cookieMap.get("qrsig");
        String qrqrtoken = getQrqrtoken(qrsig);
        String ptqrloginUrl = "https://ssl.ptlogin2.qq.com/ptqrlogin?u1=https://qzs.qq.com/qzone/v5/loginsucc.html?" +
                "para=izone&ptqrtoken=" + qrqrtoken + "&from_ui=1&aid=549000912&daid=5";
        return HttpUtil.doGet(ptqrloginUrl, null, null, context);
    }

    /**
     * 二次登录
     *
     * @param loginUrl 二维码登录成功返回的地址
     */
    public void loginAgain(String loginUrl) throws Exception {
        HttpUtil.doGet(loginUrl, null, null, context);
        String successUrl = "https://qzs.qq.com/qzone/v5/loginsucc.html?para=izone";
        HttpUtil.doGet(successUrl, null, null, context);
        String userQzoneUrl = "https://user.qzone.qq.com/" + getCookieMap().get("qq_num");
        Map<String, String> header = new HashMap<>();
        header.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.36 Core/1.63.6788.400 QQBrowser/10.3.2767.400");
        String resultHtml = HttpUtil.doGet(userQzoneUrl, null, header, context);
        String qzoneToken = resultHtml.substring(resultHtml.indexOf("window.g_qzonetoken = (function(){ try{return ") + 47, resultHtml.indexOf(";} catch(e) {var xhr = new XMLHttpRequest();xhr.withCredentials = true;xhr.open('post', '//h5.qzone.qq.com/log/post/error/qzonetoken', true)") - 1);
        addCookie("qzonetoken", qzoneToken);
    }

    /**
     * 获取g_tk
     *
     * @return g_tk
     */
    private String getG_tk() {
        String p_skey = (String) cookieMap.get("p_skey");
        if (p_skey == null) {
            return "";
        }
        long hash = 5381;
        for (int i = 0; i < p_skey.length(); i++) {
            hash += (hash << 5) + p_skey.charAt(i);
        }
        return Long.toString(hash & 0x7fffffff);
    }

    /**
     * 添加cookie
     *
     * @param name  名
     * @param value 值
     */
    private void addCookie(String name, String value) {
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

    /**
     * 获取Cookie
     *
     * @return Map
     */
    private Map<String, Object> getCookieMap() {
        if (cookieMap == null) {
            cookieMap = new HashMap<>();
        }
        String qqNum = "";
        for (Cookie cookie : context.getCookieStore().getCookies()) {
            if ("uin".equals(cookie.getName())) {
                String uin = cookie.getValue();
                if (uin.startsWith("o0")) {
                    qqNum = uin.substring(2);
                } else {
                    qqNum = uin.substring(1);
                }
            }
        }
        if (!StringUtil.isEmpty(qqNum)) {
            addCookie("qq_num", qqNum);
        }
        for (Cookie cookie : context.getCookieStore().getCookies()) {
            cookieMap.put(cookie.getName(), cookie.getValue());
        }
        return cookieMap;
    }

    /**
     * 说说发布
     *
     * @param content 内容
     * @return 结果 0成功  其余失败
     */
    public String publishSs(String content) {
        String publishUrl = "https://user.qzone.qq.com/proxy/domain/taotao.qzone.qq.com/cgi-bin/emotion_cgi_publish_v6";
        Map<String, String> param = new HashMap<>();
        param.put("qzonetoken", (String) cookieMap.get("qzonetoken"));
        param.put("g_tk", getG_tk());
        String qq_num = (String) cookieMap.get("qq_num");
        Map<String, String> hearder = new HashMap<>();
        hearder.put("Content-Type", "application/x-www-form-urlencoded");
        Map<String, String> formDate = new HashMap<>();
        formDate.put("con", content);
        formDate.put("hostuin", qq_num);
        formDate.put("code_version", "1");
        formDate.put("format", "json");
        String result = HttpUtil.doPost(publishUrl, param, formDate, hearder, context);
        return Integer.toString((Integer) JSONObject.parseObject(result).get("code"));
    }

    public static void main(String[] args) throws Exception {
        QzoneUtil qzoneUtil = new QzoneUtil();
        //生成二维码
        String qrPath = qzoneUtil.ptqrshow();
        System.out.println("请扫描二维码，地址：" + qrPath);
        //轮询二维码状态
        String qrResult;
        while (true) {
            //获取二维码状态
            qrResult = qzoneUtil.ptqrlogin();
            Thread.sleep(1000);
            //不存在返回结果
            if (!qrResult.contains("ptuiCB('")) {
                break;
            }
            //未过期或认证中
            if (qrResult.contains("ptuiCB('66',") || qrResult.contains("ptuiCB('67',")) {
                continue;
            }
            //已过期
            if (qrResult.contains("ptuiCB('65',")) {
                System.out.println(qrResult);
                //删除二维码
                FileUtil.delFile(qrPath);
                return;
            }
            //登录成功
            if (qrResult.contains("ptuiCB('0'") && qrResult.contains("登录成功")) {
                qrResult = qrResult.split("','")[2];
                break;
            }
        }
        //删除二维码
        FileUtil.delFile(qrPath);
        //二次登录
        qzoneUtil.loginAgain(qrResult);
        System.out.println(qzoneUtil.getCookieMap());
        //获取说说
        JSONObject ss = qzoneUtil.getAllSs();
        //删除说说
        List<String> tids = (List<String>) ss.get("tidList");
        qzoneUtil.deleteSs(tids);
        System.out.println(qzoneUtil.publishSs("123"));
    }
}
