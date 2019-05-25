package com.zk.datafix;

import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Tools {


    public static String getContentFromFile (String resourceLocation) {

        String content = null;
        try {
            File file = ResourceUtils.getFile(resourceLocation);
            content = new String(FileCopyUtils.copyToByteArray(file), Charset.forName("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("从文件中获取文本异常");
            return null;
        }
        return content;
    }

    /**
     * 文件转成数组
     *
     * 每一行作为一个数组元素
     */
    public static ArrayList<String> fileToArr (String resourceLocation) {
        BufferedReader reader = null;
        try {
            ArrayList<String> list = new ArrayList<>();
            File file = ResourceUtils.getFile(resourceLocation);
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null && line.trim().length() > 0) {
                list.add(line.trim());
                line = reader.readLine();
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("file to arr 异常了");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
