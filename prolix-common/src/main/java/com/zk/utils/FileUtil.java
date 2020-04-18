package com.zk.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhukai on 2020/3/15
 */
@Slf4j
public class FileUtil {

    public static boolean isFileExited(String fileAbsolutePath) {
        return new File(fileAbsolutePath).exists();
    }

    /**
     * 若文件不存在则创建
     * @param filePath 文件绝对路径
     * @return
     */
    public static File createFileIfNotExisted(String filePath) {
        File file = new File(filePath);

        if (file.exists() && file.isFile()) {
            return file;
        }

        if (!file.getParentFile().exists()) {
            boolean mkdirs = file.getParentFile().mkdirs();
            AssertUtil.assertTrue(mkdirs, "文件所在目录创建失败");
        }
        try {
            AssertUtil.assertTrue(file.createNewFile(), "文件创建失败");
        } catch (IOException e) {
            System.err.println("文件创建失异常，错误信息：" + e.getMessage());
        }

        return file;
    }

    /**
     * 若目录不存在则创建
     * @param dirPath 目录绝对路径
     * @return
     */
    public static File createDirIfNotExisted(String dirPath) {
        File file = new File(dirPath);
        if (file.exists() && file.isDirectory()) {
            return file;
        }
        boolean mkdirs = file.mkdirs();
        AssertUtil.assertTrue(mkdirs, "目录创建失败");
        return file;
    }

}
