package com.zk.utils;

/**
 * Created by csci on 17-6-6.
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩文件工具类
 */
@Slf4j
public class ZipUtil {


    /**
     * 描述：压缩指定输入流到ZipOutputStream中
     *
     * @param elementFileName 子文件名
     * @param in              子文件输入流
     * @param out             压缩文件输出流
     */
    public static void doCompress(String elementFileName, InputStream in, ZipOutputStream out) {
        byte[] buffer = new byte[2048];
        try {
            out.putNextEntry(new ZipEntry("test"+File.separator+elementFileName));
            int len = 0;
            // 读取文件的内容,打包到zip文件
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.flush();
            in.close();
        } catch (IOException e) {
            throw new RuntimeException("压缩文件异常", e);
        }
    }

    /**
     * 描述：压缩指定输入流到指定路径的文件中
     *
     * @param compressInfoList         待压缩信息
     * @param compressFileAbsolutePath 压缩文件绝对路径名
     */
    public static void doCompress(List<CompressInfo> compressInfoList, String compressFileAbsolutePath) {
        AssertUtil.notEmpty(compressInfoList, "待压缩内容不能为空");
        File file = FileUtil.createFileIfNotExisted(compressFileAbsolutePath);
        ZipOutputStream out;
        try {
            out = new ZipOutputStream(new FileOutputStream(file));
        } catch (Exception e) {
            throw new RuntimeException("构建压缩文件流异常", e);
        }
        for (CompressInfo compressInfo : compressInfoList) {
            doCompress(compressInfo.getElementFileName(), compressInfo.getContent(), out);
        }
        try {
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("ZipOutputStream关闭异常", e);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompressInfo {
        // 压缩文件中 - 子文件名
        private String elementFileName;
        // 待压缩内容
        private InputStream content;
    }


    /**
     * 压缩整个文件夹中的所有文件，生成指定名称的zip压缩包
     * @param filepath 文件所在目录
     * @param zipPath 压缩后zip全路径文件名称。
     * @param createZipDirIfNotExisted 如果zipPath所在目录不存在是否需要创建，true-需要，false-报错
     * @param dirFlag zip文件中第一层是否包含一级目录，true包含；false没有。一般使用false即可
     * 2015年6月9日
     */
    public static void zipMultiFile(String filepath, String zipPath, boolean createZipDirIfNotExisted, boolean dirFlag) {
        try {
            File file = new File(filepath);// 要被压缩的文件夹
            File outTargetZipFile = new File(zipPath);
            if (!outTargetZipFile.getParentFile().exists()) {
                if (createZipDirIfNotExisted) {
                    FileUtil.createDirIfNotExisted(outTargetZipFile.getParentFile().getAbsolutePath());
                } else {
                    throw new RuntimeException("压缩文件所在目录不存在");
                }
            }
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(outTargetZipFile));
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files == null) {
                    return;
                }
                for (File fileSec : files) {
                    if (dirFlag) {
                        recursionZip(zipOut, fileSec, file.getName() + File.separator);
                    } else {
                        recursionZip(zipOut, fileSec, "");
                    }
                }
            }
            zipOut.flush();
            zipOut.close();
        } catch (Exception e) {
            String param = String.format("入参分别为%s,%s,%s,%s", filepath, zipPath, createZipDirIfNotExisted, dirFlag);
            log.warn("压缩文件异常, {}", param, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void recursionZip(ZipOutputStream zipOut, File file, String baseDir) throws Exception {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (File fileSec : files) {
                recursionZip(zipOut, fileSec, baseDir + file.getName() + File.separator);
            }
        } else {
            byte[] buf = new byte[1024];
            InputStream input = new FileInputStream(file);
            zipOut.putNextEntry(new ZipEntry(baseDir + file.getName()));
            int len;
            while ((len = input.read(buf)) != -1) {
                zipOut.write(buf, 0, len);
            }
            input.close();
        }
    }
}
