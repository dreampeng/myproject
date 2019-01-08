package com.noadd.myapp.util.baseUtil;

import com.alibaba.fastjson.JSONObject;
import com.noadd.myapp.util.securityUtil.Base64Util;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    private static HttpClientContext getDeafultHttpClientContext() {
        return HttpClientContext.create();
    }

    public static String doGet(String url, Map<String, String> param, Map<String, String> hearder, HttpClientContext context) throws Exception {
        if (context == null) {
            context = getDeafultHttpClientContext();
        }
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
            response = httpclient.execute(httpGet, context);
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

    public static JSONObject doGet(String url, JSONObject param, HttpClientContext context) {
        if (context == null) {
            context = getDeafultHttpClientContext();
        }
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
            response = httpclient.execute(httpGet, context);
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


    public static JSONObject doPostJson(String url, String json, HttpClientContext context) {
        if (context == null) {
            context = getDeafultHttpClientContext();
        }
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        JSONObject result = new JSONObject();
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            // 执行http请求
            response = httpClient.execute(httpPost, context);
            String resultString = EntityUtils.toString(response.getEntity(), "utf-8");
            result = JSONObject.parseObject(resultString);
        } catch (Exception e) {
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


    public static String doPost(String url, Map<String, String> data, Map<String, String> fromData, Map<String, String> header, HttpClientContext context) {
        if (context == null) {
            context = getDeafultHttpClientContext();
        }
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
            response = httpClient.execute(httpPost, context);
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


    public static void doGetPic(String url, String path, Map<String, String> param, Map<String, String> hearder, HttpClientContext context) throws Exception {
        if (context == null) {
            context = getDeafultHttpClientContext();
        }
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

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
            response = httpclient.execute(httpGet, context);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream initialStream = response.getEntity().getContent();
                byte[] buffer = new byte[initialStream.available()];
                initialStream.read(buffer);
                File targetFile = new File(path);
                OutputStream outStream = new FileOutputStream(targetFile);
                outStream.write(buffer);
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
    }
}
