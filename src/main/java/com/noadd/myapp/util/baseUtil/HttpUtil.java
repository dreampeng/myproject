package com.noadd.myapp.util.baseUtil;

import com.alibaba.fastjson.JSONObject;
import com.noadd.myapp.util.ProxyUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
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
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class HttpUtil {

//    private static RequestConfig reqConf = null;
//    private static StandardHttpRequestRetryHandler standardHandler = null;
//
//    static {
//        HttpHost proxy=new HttpHost("68.183.99.96", 8080);
//        reqConf = RequestConfig.custom()
//                .setSocketTimeout(5000)
//                .setConnectTimeout(5000)
//                .setConnectionRequestTimeout(2000)
//                .setRedirectsEnabled(false)
//                .setMaxRedirects(0)
//                .setProxy(proxy)
//                .build();
//        standardHandler = new StandardHttpRequestRetryHandler(3, true);
//    }


    private static HttpClientContext getDeafultHttpClientContext() {
        return HttpClientContext.create();
    }
    public  static  String getCookie(HttpClientContext context,String key){
        AtomicReference<String> value = new AtomicReference<>();
        context.getCookieStore().getCookies().forEach(cookie -> {
            if (key.equals(cookie.getName())){
                value.set(cookie.getValue());
            }
        });
        return value.get();
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
//            httpGet.setConfig(reqConf);
            // 执行请求
            response = httpclient.execute(httpGet, context);
            if (response.getStatusLine().getStatusCode() == 202) {
                CookieStore cookieStore = context.getCookieStore();
                for (Header header : response.getHeaders("Set-Cookie")) {
                    BasicClientCookie newCookie = new BasicClientCookie(header.getElements()[0].getName(), header.getElements()[0].getValue());
                    newCookie.setDomain(new URL(url).getHost());
//                        newCookie.setExpiryDate(cookieTemp.getExpiryDate());
                    newCookie.setPath(String.valueOf(header.getElements()[0].getParameterByName("path")));
                    cookieStore.addCookie(newCookie);
                }
                context.setCookieStore(cookieStore);
            }
            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            throw e;
        } finally {
            response.close();
            httpclient.close();
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
                response.close();
                httpclient.close();
            } catch (Exception e) {

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
//            httpPost.setConfig(reqConf);
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
                if (!targetFile.getParentFile().exists()) {
                    targetFile.getParentFile().mkdirs();
                    Runtime.getRuntime().exec("chmod -R 755 " + targetFile.getParentFile().getPath());
                }
                OutputStream outStream = new FileOutputStream(targetFile);
                outStream.write(buffer);
                Runtime.getRuntime().exec("chmod -R 744 " + targetFile.getPath());
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

    public static void main(String[] args) throws Exception {
//        120.234.63.196", 3128
        getProxy();
    }
    public static void getProxy() throws Exception {
        while (true) {
            HttpClientContext context = HttpClientContext.create();
            Map<String, String> header = new HashMap<>();
            header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
            header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36");
            String htmls = HttpUtil.doGet("http://www.data5u.com/free/index.html", null, header, context);
            Document doc = Jsoup.parseBodyFragment(htmls);

            Elements elements = doc.body().getElementsByClass("l2");
            elements.forEach(ele -> {
                Elements lis = ele.getElementsByTag("li");
                String ip = lis.get(0).text();
                int port =Integer.parseInt(lis.get(1).text());
//                String proxyType = lis.get(3).text();
                for (int i = 0; i < 10; i++) {
                    Thread thread = new Thread(() -> {
                        if (ProxyUtils.validateHttp(ip, port)){
                            System.out.println(ip + " " + port + " http");
                        }
                        if (ProxyUtils.validateHttps(ip, port)){
                            System.out.println(ip + " " + port + " https");
                        }
                    });
                    thread.start();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
