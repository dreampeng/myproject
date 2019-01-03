package com.noadd.myapp.util.baseUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    public static String doGet(String url, Map<String, String> param, Map<String, String> hearder) throws Exception {

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        String resultString;
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            if (hearder != null) {
                for (String key : hearder.keySet()) {
                    httpGet.setHeader(key, hearder.get(key));
                }
            }
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            } else {
                throw new Exception("请求报错");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static JSONObject doGet(String url, JSONObject param) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        JSONObject result = new JSONObject();
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.getString(key));
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            // 执行请求
            response = httpclient.execute(httpGet);
            String resultString = EntityUtils.toString(response.getEntity(), "utf-8");
            result = JSONObject.parseObject(resultString);
        } catch (Exception e) {
//            e.printStackTrace();
            if (response != null) {
                result.put("code", Integer.toString(response.getStatusLine().getStatusCode()));
            } else {
                result.put("code", "99999");
                result.put("msg", "链接超时");
            }
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }


    public static JSONObject doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        JSONObject result = new JSONObject();
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            // 执行http请求
            response = httpClient.execute(httpPost);
            String resultString = EntityUtils.toString(response.getEntity(), "utf-8");
            result = JSONObject.parseObject(resultString);
        } catch (Exception e) {
//            e.printStackTrace();
            if (response != null) {
                result.put("code", Integer.toString(response.getStatusLine().getStatusCode()));
            } else {
                result.put("code", "99999");
                result.put("msg", "链接超时");
            }
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static String doPost(String url, Map<String, String> data, Map<String, String> fromData, Map<String, String> header) {
        // 创建Httpclient对象
        String resultString = null;
//        BasicCookieStore cookieStore = new BasicCookieStore();
//        BasicClientCookie cookie = new BasicClientCookie("name", "value");
//        cookieStore.addCookie(cookie);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            if (data != null) {
                String urlParam = "";
                for (String key : data.keySet()) {
                    urlParam += "&" + key + "=" + data.get(key);
                }
                url += "?" + urlParam.substring(1);
            }
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (fromData != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : fromData.keySet()) {
                    paramList.add(new BasicNameValuePair(key, fromData.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "utf-8");
                httpPost.setEntity(entity);
            }
            if (header != null) {
                for (String key : header.keySet()) {
                    httpPost.setHeader(key, header.get(key));
                }
            }
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
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
    public static JSONObject getOnePageSs(String uin, String pSkey, Integer page, Integer limit, JSONObject ssJson) throws Exception {
        String url = "https://h5.qzone.qq.com/proxy/domain/taotao.qq.com/cgi-bin/emotion_cgi_msglist_v6";
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
        JSONObject result = JSONObject.parseObject(doGet(url, param, header));
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
    public static JSONObject deleteSs(String tid, String qq, String qzoneToken, String cookie) {
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
        JSONObject result = JSONObject.parseObject(doPost(url, param, dataParam, header));
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
    public static String qzoneToken(String qq, String cookie) throws Exception {
        Map<String, String> header = new HashMap<>();
        header.put("cookie", cookie);
        header.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.36 Core/1.63.6788.400 QQBrowser/10.3.2767.400");
        String resultHtml = doGet("https://user.qzone.qq.com/" + qq, null, header);
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


    public static void main(String[] args) throws Exception {
        String pSkey = "DHOCHWzDpgUqo55yx3nUPWU1Niek86Rrixe3ASCNUvM_";
        String sky = "@7zIqroN0a";
        String uin = "o0815566704";
        String puin = "o0815566704";
        String pt4_token = "MT1oDQC7-hbPfOH8YB0EheJYWumyr7eXtfgsfn0Gxj8_";
        String cookie = "p_skey=" + pSkey + "; skey=" + sky + "; uin=" + uin + "; p_uin=" + puin + "; pt4_token=" + pt4_token + ";";
        String deleteCookie = "p_skey=" + pSkey + "; p_uin=" + puin + "; ";
        String qq = "815566704";
        JSONObject ss = new JSONObject();
        int limit = 20, total, page = 1;
        while (true) {
            try {
                ss = getOnePageSs(uin, pSkey, page, limit, ss);
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
}
