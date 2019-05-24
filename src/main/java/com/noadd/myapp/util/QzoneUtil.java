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
import java.util.concurrent.atomic.AtomicReference;

public class QzoneUtil {
    public QzoneUtil() {
        context = HttpClientContext.create();
    }

    public QzoneUtil(List<Cookie> cookieList) throws Exception {
        context = HttpClientContext.create();
        HttpUtil.doGet("https://qzone.qq.com", null, null, context);
        getCookieMap();
        for (Cookie cookie : cookieList) {
            context.getCookieStore().addCookie(cookie);
            cookieMap.put(cookie.getName(), cookie.getValue());
        }
    }

    private HttpClientContext context;
    private Map<String, Object> cookieMap;

    public HttpClientContext getContext() {
        return context;
    }

    public void setContext(HttpClientContext context) {
        this.context = context;
    }


    /**
     * 获取单页说说
     *
     * @param page   页数
     * @param limit  每页数量
     * @param uin    指定获取说说QQ号码
     * @param ssJson 上次调用本方法返回的JSONObject
     * @throws Exception 错误
     */
    public void getOnePageSs(Integer page, Integer limit, String uin, JSONObject ssJson) throws Exception {
        String url = "https://user.qzone.qq.com/proxy/domain/taotao.qq.com/cgi-bin/emotion_cgi_msglist_v6";
        Map<String, String> param = new HashMap<>();
        param.put("uin", uin);
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
        for (Object temp : msglistTemp == null ? new JSONArray() : msglistTemp) {
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
     * @param qq qq
     * @return
     * @throws Exception
     */
    public JSONObject getAllSs(String qq, int maxPage) throws Exception {
        Map<String, Object> cookieMap = getCookieMap();
        JSONObject ss = new JSONObject();
        int limit = 20, total, page = 1;
        while (true) {
            try {
                if (StringUtil.isEmpty(qq)) {
                    qq = (String) getCookieMap().get("qq_num");
                }
                getOnePageSs(page, limit, qq, ss);
                if ("-3000".equals(ss.getString("code"))) {
                    System.out.println("QQ号:" + cookieMap.get("qq_num") + "登录失败");
                    break;
                }
                total = ss.getInteger("total");
            } catch (Exception e) {
                break;
            }
            System.out.println("已取到" + page++ + "页，共" + ss.getJSONArray("msgList").size() + "条，总共" + total + "条");
            if ((page * limit) > total || maxPage < page) {
                break;
            }
        }
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
    public String ptqrshow(String path) throws Exception {
        String ptqrshowUrl = "https://ssl.ptlogin2.qq.com/ptqrshow?" +
                "appid=549000912&e=2&l=M&s=3&d=72&v=4&t=0.23397805982077213&daid=5&pt_3rd_aid=0";
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
        getCookieMap();
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

    public static void main(String[] args) {
        List<String> qqnums = new ArrayList<>();
        qqnums.add("291244026");
        qqnums.add("910615337");
        qqnums.add("3392407634");
        qqnums.add("2267776418");
        qqnums.add("1463207647");
        qqnums.add("2368291085");
        qqnums.add("1098108453");
        qqnums.add("2945059297");
        qqnums.add("2955683837");
        qqnums.add("2308628595");
        qqnums.add("2147659148");
        qqnums.add("2684726505");
        qqnums.add("2960518197");
        qqnums.add("2048787831");
        while (true) {
            freeGift(qqnums);
        }
    }

    public static void freeGift(List<String> qqnums) {
        AtomicReference<JSONObject> retJson = new AtomicReference<>(new JSONObject());
        String publishUrl = "https://h5.qzone.qq.com/" +
                "proxy/domain/campaign.qzone.qq.com/" +
                "fcg-bin/v2/fcg_send_qq_msg_and_send_free_gift" +
                "?g_tk=1418751766&r=0.6254867021952432";
        String g_tk = "1418751766";
        Map<String, String> param = new HashMap<>();
        param.put("r", "0.6254867021952432");
        param.put("g_tk", g_tk);
        Map<String, String> hearder = new HashMap<>();
        hearder.put("Content-Type", "application/x-www-form-urlencoded");
        hearder.put("Cookie", "_qpsvr_localtk=0.13376528601665671; pgv_pvi=1932525568; pgv_si=s6118779904; uin=o0815566704; skey=@PwQLoI1Mp; ptisp=ctc; RK=RHDssv/oZ4; ptcz=7ab71e4638f5c4665ec0040fd4f14aab926e6ebc655f09b660c402b7f64674ee; p_uin=o0815566704; Loading=Yes; welcomeflash=815566704_80548; qz_screen=1366x768; qqmusic_uin=0815566704; qqmusic_key=@PwQLoI1Mp; qqmusic_fromtag=6; pgv_pvid=902080752; pgv_info=ssid=s8694755365; 815566704_todaycount=0; 815566704_totalcount=16552; QZ_FE_WEBP_SUPPORT=1; pt4_token=7cwLiLTMU38zmJQD3ffRAwSBRPB03rUoXvKBddNsTlw_; p_skey=DtED-tYpx56kS9O4*9rYbzSwU7lrQ*NQqKYY1Wa4ZLA_; cpu_performance_v8=36; __Q_w_s__QZN_TodoMsgCnt=1; qzmusicplayer=qzone_player_815566704_1557197820291");
        Map<String, String> formDate = new HashMap<>();
        formDate.put("title", "太空狂欢嘉年华");
        formDate.put("summary", "庆祝QQ20周年++免费抢80元礼包");
        formDate.put("uin", "o0815566704");
        formDate.put("link", "https://act.qzone.qq.com/vip/2019/vip-20th?_wv=16778243&qzUseTransparentNavBar=1&_wwv=1&_sr_20=1");
        formDate.put("image_url", "https://qzonestyle.gtimg.cn/aoi/sola/20190424161728_XB5E5drf48.png");
        formDate.put("business", "0");
        formDate.put("format", "json");
        qqnums.forEach(str -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            formDate.put("dest_uin", str);
            String result = HttpUtil.doPost(publishUrl, param, formDate, hearder, null);
            retJson.set(JSONObject.parseObject(result));
            if (retJson.get().getJSONObject("data") != null
                    && 1 == retJson.get().getJSONObject("data").getInteger("state")) {
                return;
            }
            System.out.println(retJson.get() + str);

        });
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
        cookieMap.put(name, value);
    }

    /**
     * 获取Cookie
     *
     * @return Map
     */
    public Map<String, Object> getCookieMap() {
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
     * 说说发布 @{uin:@人qq} #话题内容# [/表情代码]
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

    /**
     * 获取当前相册列表
     *
     * @return 数据  结构
     * [{
     * ...前面的删掉了，那个不确定是否有
     * "id" : "V11ZcXYf3BbePb",
     * "lastuploadtime" : 1522764291,
     * "modifytime" : 1522764291,
     * "name" : "我家小乖乖",
     * "order" : 0,
     * "pre" : "", 这是一个HTTP地址
     * "priv" : 1,
     * "pypriv" : 1,
     * "total" : 61,
     * "viewtype" : 5
     * }]
     * @throws Exception error
     */
    public JSONArray getAlbum() throws Exception {
        String url = "https://h5.qzone.qq.com/proxy/domain/photo.qzone.qq.com/fcgi-bin/fcg_list_album_v3";
        String qq_num = (String) cookieMap.get("qq_num");
        Map<String, String> param = new HashMap<>();
        param.put("g_tk", getG_tk());
        param.put("hostUin", qq_num);
        param.put("uin", qq_num);
        param.put("inCharset", "utf-8");
        param.put("outCharset", "utf-8");
        param.put("format", "json");
        //不知道用途可以去到
        param.put("pageNumModeSort", "40");
        param.put("pageNumModeClass", "15");
        JSONObject result = JSONObject.parseObject(HttpUtil.doGet(url, param, null, context));
        return result.getJSONObject("data").getJSONArray("albumListModeSort");
    }

    /**
     * 获取该相册所有图片
     *
     * @param albumId 相册ID
     * @return 数据 结构
     * 以下数据不一定有，重要的也就那些
     * [ {
     * "batchId": "1435553016100",
     * "browser": 0,
     * "cameratype": " ",
     * "cp_flag": false,
     * "cp_x": 453,
     * "cp_y": 325,
     * "desc": "",
     * "exif": {
     * "exposureCompensation": "",
     * "exposureMode": "",
     * "exposureProgram": "",
     * "exposureTime": "",
     * "flash": "",
     * "fnumber": "",
     * "focalLength": "",
     * "iso": "",
     * "lensModel": "",
     * "make": "",
     * "meteringMode": "",
     * "model": "",
     * "originalTime": ""
     * },
     * "forum": 0,
     * "frameno": 0,
     * "height": 640,
     * "id": 0,
     * "is_video": false,
     * "is_weixin_mode": 0,
     * "ismultiup": 0,
     * "lloc": "NDR0cI.cMMrMkFXYhCsJ7d2zbZMHAAA!",
     * "modifytime": 1435552970,
     * "name": "2015-06-29",
     * "origin": 0,
     * "origin_upload": 0,
     * "origin_url": "",
     * "owner": "815566704",
     * "ownername": "815566704",
     * "photocubage": 53734,
     * "phototype": 1,
     * "picmark_flag": 0,
     * "picrefer": 22,
     * "platformId": 52,
     * "platformSubId": 4,
     * "poiName": "",
     * "pre": "http://b184.photo.store.qq.com/psb?/V11ZcXYf3BbePb/pCqmMuX67DkOWjZlPq5d9sNuS2x.6O*XZ4TmiWw7uTc!/a/dO3ds22TBwAA&bo=wAOAAgAAAAABB2E!",
     * "raw": "",
     * "raw_upload": 0,
     * "rawshoottime": "2015-06-29 12:41:17",
     * "shoottime": "2015-06-29 ",
     * "shorturl": "",
     * "sloc": "NDR0cI.cMMrMkFXYhCsJ7d2zbZMHAAA!",
     * "tag": "",
     * "uploadtime": "2015-06-29 12:42:50",
     * "url": "http://b184.photo.store.qq.com/psb?/V11ZcXYf3BbePb/pCqmMuX67DkOWjZlPq5d9sNuS2x.6O*XZ4TmiWw7uTc!/b/dO3ds22TBwAA&bo=wAOAAgAAAAABB2E!",
     * "width": 960,
     * "yurl": 0
     * }
     * ]
     * @throws Exception error
     */
    public JSONArray getPhotos(String albumId) throws Exception {
        String url = "https://h5.qzone.qq.com/proxy/domain/photo.qzone.qq.com/fcgi-bin/cgi_list_photo";
        int start = 0, end = 50;
        String qq_num = (String) cookieMap.get("qq_num");
        Map<String, String> param = new HashMap<>();
        param.put("g_tk", getG_tk());
        param.put("hostUin", qq_num);
        param.put("uin", qq_num);
        param.put("topicId", albumId);
        param.put("inCharset", "utf-8");
        param.put("outCharset", "utf-8");
        param.put("format", "json");
        param.put("pageStart", Integer.toString(start));
        param.put("pageNum", Integer.toString(end));
        JSONObject result = JSONObject.parseObject(HttpUtil.doGet(url, param, null, context));
        JSONArray photoes = result.getJSONArray("albumListModeSort");
        int total = result.getInteger("totalInAlbum");
        while (total > end) {
            result = JSONObject.parseObject(HttpUtil.doGet(url, param, null, context));
            total = result.getInteger("totalInAlbum");
            photoes.addAll(result.getJSONArray("albumListModeSort"));
            start += 50;
            end += 50;
        }
        return photoes;
    }


    //查询二维码状态
    public String loginOnce(String path) throws Exception {
        //轮询二维码状态
        String qrResult;
        String retStr = null;
        //获取二维码状态
        qrResult = ptqrlogin();
        //未过期或认证中
        if (qrResult.contains("ptuiCB('66',") || qrResult.contains("ptuiCB('67',")) {
            retStr = "0";
        }
        //已过期
        if (qrResult.contains("ptuiCB('65',")) {
            retStr = "-1";
        }
        //登录成功
        if (qrResult.contains("ptuiCB('0'") && qrResult.contains("登录成功")) {
            qrResult = qrResult.split("','")[2];
            //二次登录
            loginAgain(qrResult);
            retStr = (String) cookieMap.get("qq_num");
        }
        if (!"0".equals(retStr)) {
            FileUtil.delFile(path);
        }
        return retStr;
    }

    //轮询查询
    public String login(String path) throws Exception {
        //轮询二维码状态
        String qrResult;
        String retStr = "-1";
        while (true) {
            Thread.sleep(1000);
            //获取二维码状态
            qrResult = ptqrlogin();
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
                retStr = "0";
                break;
            }
            //登录成功
            if (qrResult.contains("ptuiCB('0'") && qrResult.contains("登录成功")) {
                qrResult = qrResult.split("','")[2];
                retStr = (String) cookieMap.get("qq_num");
                break;
            }
        }
        //删除二维码
        FileUtil.delFile(path);
        //二次登录
        loginAgain(qrResult);
        return retStr;
    }

    /**
     * 赞说说
     *
     * @param uin    发送人uin
     * @param key    说说ID
     * @param appid  说说类型
     * @param unikey 说说类型
     * @param curkey 说说类型
     * @param isLike 赞或取消
     */
    public boolean doLike(String uin, String key, String appid, String unikey, String curkey, boolean isLike) {
        String url = "https://user.qzone.qq.com/proxy/domain/w.qzone.qq.com/cgi-bin/likes/";
        if (isLike) {
            url += "internal_dolike_app";
        } else {
            url += "internal_unlike_app";
        }
        Map<String, String> param = new HashMap<>();
        param.put("qzonetoken", (String) cookieMap.get("qzonetoken"));
        param.put("g_tk", getG_tk());
        Map<String, String> data = new HashMap<>();
        data.put("curkey", curkey);
        data.put("unikey", unikey);
        data.put("appid", appid);
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        return HttpUtil.doPost(url, param, data, header, context).contains("\"msg\":\"succ\"");
    }


    /**
     * 赞说说
     *
     * @param qq     被赞QQ
     * @param tid    说说ID
     * @param isLike 赞或取消
     */
    public boolean doLike(String fromQq,String qq, String tid, boolean isLike) {
        String url = "https://user.qzone.qq.com/proxy/domain/w.qzone.qq.com/cgi-bin/likes/";
        if (isLike) {
            url += "internal_dolike_app";
        } else {
            url += "internal_unlike_app";
        }
        Map<String, String> param = new HashMap<>();
        param.put("qzonetoken", (String) cookieMap.get("qzonetoken"));
        param.put("g_tk", getG_tk());
        Map<String, String> data = new HashMap<>();
        String curkey = "http://user.qzone.qq.com/" + qq + "/mood/" + tid + ".1";
        data.put("qzreferrer", "https://user.qzone.qq.com/"+qq);
        data.put("curkey", curkey);
        data.put("unikey", curkey);
        data.put("opuin", fromQq);
        data.put("from", "-100");
        data.put("fupdate", "1");
        data.put("face", "0");
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        return HttpUtil.doPost(url, param, data, header, context).contains("\"message\":\"succ\"");
    }

    /**
     * 刷新最新说说 20条
     *
     * @return
     * @throws Exception error
     */
    public List<Map<String, String>> getNewSs() throws Exception {
        String url = "https://user.qzone.qq.com/proxy/domain/ic2.qzone.qq.com/cgi-bin/feeds/feeds3_html_more";
        Map<String, String> param = new HashMap<>();
        param.put("qzonetoken", (String) cookieMap.get("qzonetoken"));
        param.put("g_tk", getG_tk());
        param.put("useutf8", "1");
        param.put("count", "20");
        param.put("filter", "all");
        param.put("format", "json");
        param.put("uin", (String) cookieMap.get("qq_num"));
        //不传会默认取
//        param.put("begintime","1");
        JSONObject object = JSONObject.parseObject(HttpUtil.doGet(url, param, null, context));
        List<Map<String, String>> result = new ArrayList<>();
        if (0 == (int) object.get("code")) {
            JSONArray datas = (JSONArray) ((Map<String, Object>) object.get("data")).get("data");
            for (Object dataTemp : datas) {
                if (dataTemp == null) {
                    continue;
                }
                JSONObject data = (JSONObject) dataTemp;
                if (((String) data.get("html")).contains("cancellike")) {
                    continue;
                }
                String html = data.getString("html");
                Map<String, String> temp = new HashMap<>();
                temp.put("uin", (String) data.get("uin"));
                temp.put("key", (String) data.get("key"));
                temp.put("appid", (String) data.get("appid"));
                try {
                    if (html.contains("<div class=\"f-single-top\" data-istop=\"\">    <span>    好友热播    </span> ") ||
                            html.contains("<div class=\"f-single-top\"><span>广告</span>")) {
                        //广告或好友热播 跳过
                        continue;
                    }
                    String unikey = html.substring(html.indexOf("data-unikey=\"") + 13, html.indexOf("\" data-curkey=\""));
                    String curkey = html.substring(html.indexOf("data-curkey=\"") + 13,
                            !html.contains("\" data-clicklog=\"like\"")
                                    ? html.indexOf("\" data-clicklog=\"cancellike\"")
                                    : html.indexOf("\" data-clicklog=\"like\""));
                    temp.put("unikey", unikey);
                    temp.put("curkey", curkey);
                    result.add(temp);
                } catch (Exception ignored) {
                }
            }
        } else if (-10001 == (int) object.get("code")) {
            result = new ArrayList();
        } else {
            System.out.println(object.toJSONString());
            return null;
        }
        return result;
    }

    private void likes(List<Map<String, String>> newSs) {
        for (Map<String, String> ss : newSs) {
            String uin = ss.get("uin");
            String key = ss.get("key");
            String appid = ss.get("appid");
            String unikey = ss.get("unikey");
            String curkey = ss.get("curkey");
            if (doLike(uin, key, appid, unikey, curkey, true)) {
                System.out.println(uin + "----" + key);
            }
        }
    }

    /**
     * {
     * "fuin": 910615337,
     * "nick": "           浮沉",
     * "portrait": "http://qlogo2.store.qq.com/qzone/910615337/910615337/30",
     * "gender": "男",
     * "constellation": "白羊座",
     * "addr": "江北",
     * "if_qq_friend": 1,
     * "if_special_care": 0,
     * "is_special_vip": false
     * }
     *
     * @return
     * @throws Exception
     */
    public JSONArray headLikeList() throws Exception {
        String url = "https://h5.qzone.qq.com/proxy/domain/users.qzone.qq.com/cgi-bin/likes/get_like_list_app";
        Map<String, String> param = new HashMap<>();
        param.put("qzonetoken", (String) cookieMap.get("qzonetoken"));
        param.put("g_tk", getG_tk());
        param.put("uin", (String) cookieMap.get("qq_num"));
        param.put("unikey", "http://user.qzone.qq.com/" + cookieMap.get("qq_num"));
        param.put("home", "1");
        param.put("rd", "0." + StringUtil.getRandomNumStr(17));
        String retStr = HttpUtil.doGet(url, param, null, context);
        int startIndex = retStr.indexOf("(");
        int endIndex = retStr.lastIndexOf(")");
        String json = retStr.substring(startIndex + 1, endIndex);
        return JSONObject.parseObject(json).getJSONObject("data").getJSONArray("like_data").getJSONObject(0).getJSONArray("data");
    }

    public void headDoLike(String qq) {
        String url = "https://user.qzone.qq.com/proxy/domain/w.qzone.qq.com/cgi-bin/likes/internal_dolike_app";
        Map<String, String> param = new HashMap<>();
        param.put("qzonetoken", (String) cookieMap.get("qzonetoken"));
        param.put("g_tk", getG_tk());
        String dataUrl = "http://user.qzone.qq.com/" + qq;
        Map<String, String> data = new HashMap<>();
        data.put("appid", "7030");
        data.put("fupdate", "1");
        data.put("opuin", (String) cookieMap.get("qq_num"));
        data.put("unikey", dataUrl);
        data.put("curkey", dataUrl);
        data.put("format", "json");
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HttpUtil.doPost(url, param, data, header, context);
    }

    public void headLike() {
        try {
            JSONArray array = headLikeList();
            array.forEach(o -> {
                JSONObject json = (JSONObject) o;
                headDoLike(json.getString("fuin"));
            });
        } catch (Exception e) {
            System.out.println("获取首页赞列表失败，" + cookieMap.get("qq_num"));
        }
    }

    public int miaoZhan() {
        try {
            List<Map<String, String>> newSs;
            newSs = getNewSs();
            if (newSs == null) {
                return -1;
            }
            likes(newSs);
            return 0;
        } catch (Exception e) {
            System.out.println(e);
            return -2;
        }
    }
}
