package com.zk.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by zhukai on 2020/3/15
 */
public class FileUtilTest {

    @Test
    public void test_createFileIfNotExisted() throws InterruptedException {
        String tmpDir = System.getProperty("java.io.tmpdir");

        // 文件目录
        String testDir = tmpDir + File.separator + UUID.randomUUID().toString() + File.separator;
        // 文件绝对路径
        String testFilePath = testDir + "test.txt";

        // 1、说明文件所在目录不存在
        Assert.assertFalse(new File(testDir).exists());
        // 2、说明文件不存在
        Assert.assertFalse(new File(testFilePath).exists());

        System.out.println("文件创建时间：" + new Date().toLocaleString());
        File file = FileUtil.createFileIfNotExisted(testFilePath);
        // 3、验证文件创建成功
        Assert.assertTrue(file.exists());
        Thread.sleep(2000L);
        // 4、验证再次调用时，是通过一个文件
        File file2 = FileUtil.createFileIfNotExisted(testFilePath);
        System.out.println(new Date(file.lastModified()).toLocaleString());
        Assert.assertEquals(file.lastModified(), file2.lastModified());

        file.deleteOnExit();
    }

    @Test
    public void test_createDirIfNotExisted() throws InterruptedException {
        String tmpDir = System.getProperty("java.io.tmpdir");

        // 文件目录
        String testDir = tmpDir + File.separator + UUID.randomUUID().toString() + File.separator;

        // 1、说明文件所在目录不存在
        Assert.assertFalse(new File(testDir).exists());

        System.out.println("目录创建时间：" + new Date().toLocaleString());
        File file = FileUtil.createDirIfNotExisted(testDir);
        // 3、验证文件创建成功
        Assert.assertTrue(file.exists());
        Thread.sleep(2000L);
        // 4、验证再次调用时，是通过一个文件
        File file2 = FileUtil.createDirIfNotExisted(testDir);
        System.out.println(new Date(file.lastModified()).toLocaleString());
        Assert.assertEquals(file.lastModified(), file2.lastModified());

        file.deleteOnExit();
    }

}
