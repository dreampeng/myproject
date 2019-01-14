package com.noadd.myapp.util.baseUtil;

import java.io.*;

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
}
