/*
成都太阳高科技有限责任公司
http://www.suncd.com
*/
package com.suncd.conn.ibmmq.utils;

import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtil {
    public static List<String> readLastNLine(File file, long numRead) {
        List<String> result = new ArrayList<>();
        // 定义结果集
//        StringBuilder result = new StringBuilder();
        //行数统计
        long count = 0;
        // 排除不可读状态
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return null;
        }
        // 使用随机读取
        RandomAccessFile fileRead = null;
        try {
            // 使用读模式
            fileRead = new RandomAccessFile(file, "r");
            // 读取文件长度
            long length = fileRead.length();
            // 如果是0，代表是空文件，直接返回空结果
            if (length == 0L) {
                return result;
            } else {
                // 初始化游标
                long pos = length - 1;
                while (pos > 0) {
                    pos--;
                    // 开始读取
                    fileRead.seek(pos);
                    // 如果读取到\n代表是读取到一行
                    if (fileRead.readByte() == '\n') {
                        // 使用readLine获取当前行
                        String line = fileRead.readLine();
                        // 保存结果
                        result.add(line);
                        // 行数统计，如果到达了numRead指定的行数，就跳出循环
                        count++;
                        if (count == numRead) {
                            break;
                        }
                    }
                }
                if (pos == 0) {
                    fileRead.seek(0);
                    result.add(fileRead.readLine());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileRead != null) {
                try {
                    // 关闭资源
                    fileRead.close();
                } catch (Exception e) {
                }
            }
        }
        return result;
    }

    /**
     * 倒序输出字符串文本
     *
     * @param file
     * @param numberRead
     * @return
     */
    public static String readLastNLineString(File file, long numberRead) {
        String retStr = null;
        List<String> listStr = readLastNLine(file, numberRead);
        if (CollectionUtils.isEmpty(listStr)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();

        for (int i = listStr.size(); i > 0; i--) {
            String str = listStr.get(i - 1).replaceAll(" :: ", " :: </span>")
                    .replaceAll(" ", "&nbsp;")
                    .replaceAll("INFO&nbsp;-", "<span class='log-lv-info'>INFO&nbsp;-</span>")
                    .replaceAll("WARN&nbsp;-", "<span class='log-lv-warn'>WARN&nbsp;-</span>")
                    .replaceAll("ERROR&nbsp;-", "<span class='log-lv-error'>ERROR&nbsp;-</span>");
            builder.append("<span class='log-lv-title'>").append(str).append("<br/>");
        }
        try {

            retStr = new String(builder.toString().getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retStr;
    }

    public static List<Map<String ,String>> getFiles(File dir) {
        List<Map<String ,String>> ret = new ArrayList<>();
       File[] files = dir.listFiles();
       if (null == files){
           return ret;
       }
       for (File f : files) {
           Map<String ,String> map = new HashMap<>();
           map.put("fileName",f.getName());

           String fileSize = f.length()/1000L + "KB";
           map.put("fileSize",fileSize);
           ret.add(map);
       }  
       return ret;
   } 

}
