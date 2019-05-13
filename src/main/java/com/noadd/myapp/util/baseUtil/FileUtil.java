package com.noadd.myapp.util.baseUtil;

import com.alibaba.fastjson.JSONObject;
import com.noadd.myapp.util.ProxyUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.*;
import java.util.*;

/**
 * 文件处理工具
 **/
public class FileUtil {
    /**
     * 复制文件
     *
     * @param src  原地址
     * @param dest 复制地址
     * @throws IOException error
     */
    public static void copyFile(String src, String dest) throws IOException {
        FileInputStream in = new FileInputStream(src);
        File file = new File(dest);
        if (!file.exists())
            file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        int c;
        byte buffer[] = new byte[1024];
        while ((c = in.read(buffer)) != -1) {
            for (int i = 0; i < c; i++)
                out.write(buffer[i]);
        }
        in.close();
        out.close();
    }

    /**
     * 文件重命名
     *
     * @param path    文件夹路径
     * @param oldName 文件名称
     * @param newName 重命名名称
     */
    public static void renameFile(String path, String oldName, String newName) {
        if (!oldName.equals(newName)) {//新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(path + "/" + oldName);
            File newfile = new File(path + "/" + newName);
            if (newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                System.out.println(newName + "已经存在！");
            else {
                oldfile.renameTo(newfile);
            }
        }
    }

    /**
     * 文件移动
     *
     * @param filename 文件名
     * @param oldPath  原来地址
     * @param newPath  移动地址
     * @param cover    是否覆盖
     */
    public static void changeDirectory(String filename, String oldPath, String newPath, boolean cover) {
        if (!oldPath.equals(newPath)) {
            File oldFile = new File(oldPath + "/" + filename);
            File newFile = new File(newPath + "/" + filename);
            if (newFile.exists()) {//若在待转移目录下，已经存在待转移文件
                if (cover)//覆盖
                    System.out.println(oldFile.renameTo(newFile));
                else
                    System.out.println("在新目录下已经存在：" + filename);
            } else {
                System.out.println(oldFile.renameTo(newFile));
            }
        }
    }

    /**
     * 读文件流
     *
     * @param path 文件地址
     * @return 文件流
     * @throws IOException error
     */
    public static String getFileInputStream(String path) throws IOException {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException();
        }
        FileInputStream fis = new FileInputStream(file);
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while ((fis.read(buf)) != -1) {
            sb.append(new String(buf));
            buf = new byte[1024];//重新生成，避免和上次读取的数据重复
        }
        return sb.toString();
    }


    /**
     * 读文件流
     *
     * @param path 文件地址
     * @return 文件流
     * @throws IOException error
     */
    public static String getBufferedReader(String path) throws IOException {
        File file = new File(path);
        if (!file.exists() || file.isDirectory())
            throw new FileNotFoundException();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String temp;
        StringBuilder sb = new StringBuilder();
        temp = br.readLine();
        while (temp != null) {
            sb.append(temp).append(" ");
            temp = br.readLine();
        }
        return sb.toString();
    }

    /**
     * 删除文件/夹
     *
     * @param path 地址
     */
    public static void delFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile())
            if (file.delete()) {
                System.out.println("文件已删除：" + path);
            }
    }

    /**
     * 删除目录
     *
     * @param path 地址
     */
    public static void delDir(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            File[] tmp = dir.listFiles();
            if (tmp == null) {
                if (dir.delete()) {
                    System.out.println("文件已删除：" + path);
                }
            } else {
                for (File aTmp : tmp) {
                    if (aTmp.isDirectory()) {
                        delDir(path + "/" + aTmp.getName());
                    } else {
                        if (aTmp.delete()) {
                            System.out.println("文件已删除：" + aTmp.getAbsolutePath());
                        }
                    }
                }
                if (dir.delete()) {
                    System.out.println("文件夹已删除：" + path);
                }
            }
        }
    }


    /**
     * 按行读文件
     *
     * @param filePath 文件地址
     * @return 返回值
     */
    public static List<String> readFileByLine(String filePath) {
        List<String> arrayList = new ArrayList<>();
        try {
            File file = new File(filePath);
            InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            BufferedReader bf = new BufferedReader(inputReader);
            // 按行读取字符串
            String str;
            while ((str = bf.readLine()) != null) {
                arrayList.add(str);
            }
            bf.close();
            inputReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
    public static void main(String[] args) throws Exception {
        String qq = "747837043";
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36");
        header.put("DNT", "1");
        header.put("Connection", "keep-alive");
        header.put("Upgrade-Insecure-Requests", "1");
        header.put("Cache-Control", "max-age=0");
        header.put("Accept-Encoding", "gzip, deflate");
        header.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        String filePath = Objects.requireNonNull(FileUtil.class.getClassLoader().getResource("addr.txt")).getPath();
        List<String> iPath = readFileByLine(filePath);
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
                        System.out.println(url + " " + tid);
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
                    System.out.println(url + " " + tid);
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
                    System.out.println(url + " " + tid + "----" + jsonObject.getString("msg"));
                }
            } catch (Exception e) {
                System.out.println("失败的地址：" + url);
            }
        });


    }

    public static void setCookie(HttpClientContext context, String name, String value) {
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
