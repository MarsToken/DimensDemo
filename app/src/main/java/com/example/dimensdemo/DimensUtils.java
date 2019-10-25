package com.example.dimensdemo;

import android.util.Log;

import java.io.*;

/**
 * dimens数据自动生成工具
 */
public class DimensUtils {
    /**
     * 源文件-基准
     */
    static String oldFilePath = "res/values-nodpi/dimens.xml";
    /**
     * 新生成文件路径
     */
    static String filePath1280 = "res/values-sw540dp-car-mdpi/dimens.xml";
    /**
     * 新生成文件路径-基准
     */
    static String filePath1920 = "res/values-sw540dp-xxhdpi/dimens.xml";
    /**
     * 缩小倍数
     */
    static float changes = 1.5f;

    public static void main(String path) {
        //生成1-1920px
        String allPx = getAllPx();
        makeFile(path + oldFilePath);
        writeFile(path + oldFilePath, allPx);

        String st = convertStreamToString(path + oldFilePath, changes);
        makeFile(path + filePath1280);
        writeFile(path + filePath1280, st);

        String st1 = convertStreamToString(path + oldFilePath, 1f);
        makeFile(path + filePath1920);
        writeFile(path + filePath1920, st1);
    }

    /**
     * 读取文件 生成缩放后字符串
     */
    public static String convertStreamToString(String filepath, float f) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(filepath));
            String line = null;
            System.out.println("q1");
            String endmark = "px</dimen>";
            String startmark = ">";
            while ((line = bf.readLine()) != null) {
                if (line.contains(endmark)) {
                    int end = line.lastIndexOf(endmark);
                    int start = line.indexOf(startmark);
                    String stpx = line.substring(start + 1, end);
                    int px = Integer.parseInt(stpx);
                    float newpx = px / f;
                    String newline = line.replace(px + "px", newpx + "px");
                    sb.append(newline + "\r\n");
                } else {
                    sb.append(line + "\r\n");
                }
            }
            System.out.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static void makeFile(String sPath) {
        String path = sPath.replace("dimens.xml", "");
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        File file = new File(sPath);
        if (!file.exists()) {
            try {
                Log.e("tag", "path=" + sPath);
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        File file = new File(sPath);
//        // // 判断目录或文件是否存在
//        if (!file.exists()) { // 不存在返回 false
//            file.getAbsoluteFile().mkdirs();
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return true;
//        } else {
//            // 判断是否为文件
//            if (file.isFile()) { // 为文件时调用删除文件方法
//                return deleteFile(sPath);
//            } else { // 为目录时调用删除目录方法
//                // return deleteDirectory(sPath);
//            }
//        }
//        return false;
    }

    /**
     * 存为新文件
     */
    public static void writeFile(String filepath, String st) {
        try {
            FileWriter fw = new FileWriter(filepath);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(st);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成全px文件
     */
    public static String getAllPx() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<resources>" + "\r\n");
            sb.append("<dimen name=\"screen_width\">1920px</dimen>" + "\r\n");
            sb.append("<dimen name=\"screen_height\">720px</dimen>" + "\r\n");
            for (int i = 1; i <= 1920; i++) {
                System.out.println("i=" + i);
                sb.append("<dimen name=\"px" + i + "\">" + i + "px</dimen>"
                        + "\r\n");
            }
            sb.append("</resources>" + "\r\n");
            System.out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
}
