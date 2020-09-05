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
     * 创建文件
     * @param filePath 文件绝对路径
     * @param fileExistsDeleted 若文件已存在是否删除：true-删除，false-不删除，直接返回
     * @return
     */
    public static File createFile (String filePath, boolean fileExistsDeleted) {
        File file = new File(filePath);

        if (file.exists() && file.isFile()) {
            if (fileExistsDeleted) {
                AssertUtil.assertTrue(file.delete(), "删除文件失败");
            } else {
                return file;
            }
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
     * 创建目录
     * @param dirPath 目录绝对路径
     * @return
     */
    public static File createDir (String dirPath) {
        File file = new File(dirPath);
        if (file.exists() && file.isDirectory()) {
            return file;
        }
        boolean mkdirs = file.mkdirs();
        AssertUtil.assertTrue(mkdirs, "目录创建失败");
        return file;
    }

    /**
     * 获取临时目录,若subDirName参数不为空,则在该临时目录下创建子文件夹,并返回该文件夹路径
     *
     * @param subDirNames 临时目录下的子目录名称
     * @return
     */
    public static String getTempDir(String... subDirNames) {
        // 系统临时目录
        String temDir = System.getProperty("java.io.tmpdir");

        // 组装临时目录
        StringBuilder resultDir = new StringBuilder(temDir.endsWith(File.separator) ? temDir : temDir + File.separator);
        if (subDirNames != null && subDirNames.length > 0) {
            for (String subDirName : subDirNames) {
                // 校验子文件名中是否有目录分割符
                AssertUtil.assertTrue(!subDirName.contains(File.separator), subDirName + "子文件名中不能有目录分割符");
                resultDir.append(subDirName).append(File.separator);
            }
        }

        // 创建目录
        String path = createDir(resultDir.toString()).getAbsolutePath();
        return path.endsWith(File.separator) ? path : path + File.separator;
    }

}
