package com.noadd.myapp.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.noadd.myapp.util.baseUtil.HttpUtils;
import com.noadd.myapp.util.baseUtil.StringUtil;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QzoneUtils {
    private HttpClientContext context;

    public QzoneUtils() {
        context = HttpClientContext.create();
    }

    /**
     * 获取单页说说
     *
     * @param uin    cookie值
     * @param pSkey  cookie值
     * @param page   页数
     * @param limit  每页数量
     * @param ssJson 上次调用本方法返回的JSONObject
     * @return JSONObject
     * @throws Exception 错误
     */
    public JSONObject getOnePageSs(String uin, String pSkey, Integer page, Integer limit, JSONObject ssJson) throws Exception {
        String url = "https://user.qzone.qq.com/proxy/domain/taotao.qq.com/cgi-bin/emotion_cgi_msglist_v6";
        String cookie = " uin=" + uin + "; p_skey=" + pSkey + ";";
        Map<String, String> param = new HashMap<>();
        param.put("pos", Integer.toString((page - 1) * limit));
        param.put("num", Integer.toString(limit));
        param.put("code_version", "1");
        param.put("format", "json");
        param.put("need_private_comment", "1");
        param.put("g_tk", "2132853281");
        Map<String, String> header = new HashMap<>();
        header.put("cookie", cookie);
        header.put("referer", "https://qzs.qq.com/qzone/app/mood_v6/html/index.html");
        header.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36");
        JSONObject result = JSONObject.parseObject(HttpUtils.doGet(url, param, header, null));
        int code = result.getInteger("code");
        ssJson.put("code", code);
        if (code != 0) {
            return ssJson;
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
        return ssJson;
    }

    public static int hash33(String o) {
        int t = 0, e = 0, n = o.length();
        for (; e < n; ++e) {
            t += (t << 5) + o.charAt(e);
        }
        return 2147483647 & t;
    }

    /**
     * 删除说说
     *
     * @param tid        cookie值
     * @param qq         QQ号
     * @param qzoneToken cookie值
     * @param cookie     cookie值
     * @return
     */
    public JSONObject deleteSs(String tid, String qq, String qzoneToken, String cookie) {
        String url = "https://user.qzone.qq.com/proxy/domain/taotao.qzone.qq.com/cgi-bin/emotion_cgi_delete_v6";
        Map<String, String> param = new HashMap<>();
        param.put("qzonetoken", qzoneToken);
        param.put("g_tk", "1549933695");
        Map<String, String> dataParam = new HashMap<>();
        dataParam.put("hostuin", qq);
        dataParam.put("tid", tid);
        dataParam.put("code_version", "1");
        dataParam.put("format", "json");
        Map<String, String> header = new HashMap<>();
        header.put("cookie", cookie);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        JSONObject result = JSONObject.parseObject(HttpUtils.doPost(url, param, dataParam, header, null));
        return result;
    }

    /**
     * 获取qzt
     *
     * @param qq     QQ号
     * @param cookie cookie
     * @return String
     * @throws Exception error
     */
    public String qzoneToken(String qq, String cookie) throws Exception {
        Map<String, String> header = new HashMap<>();
        header.put("cookie", cookie);
        header.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.36 Core/1.63.6788.400 QQBrowser/10.3.2767.400");
        String resultHtml = HttpUtils.doGet("https://user.qzone.qq.com/" + qq, null, header, null);
        return resultHtml.substring(resultHtml.indexOf("window.g_qzonetoken = (function(){ try{return ") + 47, resultHtml.indexOf(";} catch(e) {var xhr = new XMLHttpRequest();xhr.withCredentials = true;xhr.open('post', '//h5.qzone.qq.com/log/post/error/qzonetoken', true)") - 1);
    }

    /* 获取需要的cookie
        var a, r ;
        var s="";
        r = new RegExp("p_skey=(.{43}_;)");
        if (a = document.cookie.match(r)) {
            s += " " + a[0];
        }
        r = new RegExp("skey=(@.{9};)");
        if (a = document.cookie.match(r)) {
            s += " " + a[0];
        }
        r = new RegExp("uin=(.{6,13};)");
        if (a = document.cookie.match(r)) {
            s += " " + a[0];
        }
        r = new RegExp("p_uin=(.{6,13});");
        if (a = document.cookie.match(r)) {
            s += " " + a[0];
        }
        r = new RegExp("pt4_token=(.{43}_);");
        if (a = document.cookie.match(r)) {
            s += " " + a[0];
        }
        console.log(s);
    */

    public void main1() throws Exception {
        Map<String,Object> cookieMap = getCookieMap();
        String p_skey = (String) cookieMap.get("p_skey");
        String sky = (String) cookieMap.get("sky");
        String uin = (String) cookieMap.get("uin");
        String p_uin =  (String) cookieMap.get("p_uin");
        String pt4_token = (String) cookieMap.get("pt4_token");
        String cookie = "p_skey=" + p_skey + "; skey=" + sky + "; uin=" + uin + "; p_uin=" + p_uin + "; pt4_token=" + pt4_token + ";";
        String deleteCookie = "p_skey=" + p_skey + "; p_uin=" + p_uin + "; ";
        String qq = "815566704";
        JSONObject ss = new JSONObject();
        int limit = 20, total, page = 1;
        while (true) {
            try {
                ss = getOnePageSs(uin, p_skey, page, limit, ss);
                if ("-3000".equals(ss.getString("code"))) {
                    System.out.println("QQ号:" + qq + "登录失败");
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
        String qzoneToken = qzoneToken(qq, cookie);
        System.out.println(ss);
        System.out.println(qzoneToken);
        List<String> tids = (List<String>) ss.get("tidList");
        int i = 0, x = 0, y = 0;
        while (i < tids.size()) {
            String tid = tids.get(i);
            JSONObject result = deleteSs(tid, qq, qzoneToken, deleteCookie);
            if (result != null) {
                int code = (int) result.get("code");
                int subcode = (int) result.get("subcode");
                if (code == 0 && (subcode != -200 || subcode != 0)) {
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

    public static String getQrqrtoken(String qrsig) {
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
     * @return
     * @throws Exception error
     */
    public String ptqrshow() throws Exception {
        String ptqrshowUrl = "https://ssl.ptlogin2.qq.com/ptqrshow?" +
                "appid=549000912&e=2&l=M&s=3&d=72&v=4&t=0.23397805982077213&daid=5&pt_3rd_aid=0";
        String path = "C:\\Users\\Administrator\\Desktop\\" + StringUtil.getUuid() + ".png";
        HttpUtils.doGetPic(ptqrshowUrl, path, null, null, context);
        return path;
    }

    public String ptqrlogin() throws Exception {
        Map<String, Object> cookieMap = getCookieMap();
        String qrsig = (String) cookieMap.get("qrsig");
        String qrqrtoken = getQrqrtoken(qrsig);
        String ptqrloginUrl = "https://ssl.ptlogin2.qq.com/ptqrlogin?u1=https://qzs.qq.com/qzone/v5/loginsucc.html?" +
                "para=izone&ptqrtoken=" + qrqrtoken + "&from_ui=1&aid=549000912&daid=5";
        String result = null;
        while (true) {
            Thread.sleep(1000);
            String ret = HttpUtils.doGet(ptqrloginUrl, null, null, context);
            if (!ret.contains("ptuiCB('")) {
                break;
            }
            if (ret.contains("ptuiCB('66',") || ret.contains("ptuiCB('67',")) {
                continue;
            }
            if (ret.contains("ptuiCB('65',")) {
                result = "65";
                System.out.println(ret);
                break;
            }
            if (ret.contains("ptuiCB('0'") && ret.contains("登录成功")) {
                result = ret.split("','")[2];
                break;
            }
        }
        return result;
    }

    private Map<String, Object> getCookieMap() {
        Map<String, Object> cookieMap = new HashMap<>();
        for (Cookie cookie : context.getCookieStore().getCookies()) {
            cookieMap.put(cookie.getName(), cookie.getValue());
        }
        return cookieMap;
    }

    public static void main(String[] args) throws Exception {
        QzoneUtils qzoneUtils = new QzoneUtils();
        System.out.println(qzoneUtils.ptqrshow());
        String retUrl = qzoneUtils.ptqrlogin();
        System.out.println(retUrl);
        HttpUtils.doGet(retUrl, null, null, qzoneUtils.context);
        System.out.println(qzoneUtils.getCookieMap());
        qzoneUtils.main1();
    }
}
