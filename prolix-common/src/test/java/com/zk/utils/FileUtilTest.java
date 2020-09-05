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

        File file = FileUtil.createFile(testFilePath, true);
        long fileLastModified = file.lastModified();
        System.out.println("file lastModified：" + new Date(fileLastModified).toLocaleString());

        // 3、验证文件创建成功
        Assert.assertTrue(file.exists());
        Thread.sleep(2000L);// 睡眠2秒已校验lastModified
        // 4、验证再次调用时，是原来的文件
        File file2 = FileUtil.createFile(testFilePath, false);
        long file2LastModified = file2.lastModified();
        System.out.println("file2 lastModified：" + new Date(file2LastModified).toLocaleString());

        // 5、验证再次调用时，是新创建的文件
        Thread.sleep(2000L);// 睡眠2秒已校验lastModified
        File file3 = FileUtil.createFile(testFilePath, true);
        long file3LastModified = file3.lastModified();
        System.out.println("file3 lastModified：" + new Date(file3LastModified).toLocaleString());
        Assert.assertTrue(file3LastModified > fileLastModified);
        Assert.assertTrue(file3LastModified > file2LastModified);
        Assert.assertTrue(fileLastModified == file2LastModified);

        file3.deleteOnExit();
    }

    @Test
    public void test_createDirIfNotExisted() throws InterruptedException {
        String tmpDir = System.getProperty("java.io.tmpdir");

        // 文件目录
        String testDir = tmpDir + File.separator + UUID.randomUUID().toString() + File.separator;

        // 1、说明文件所在目录不存在
        Assert.assertFalse(new File(testDir).exists());

        System.out.println("目录创建时间：" + new Date().toLocaleString());
        File file = FileUtil.createDir(testDir);
        // 3、验证文件创建成功
        Assert.assertTrue(file.exists());
        Thread.sleep(2000L);
        // 4、验证再次调用时，是同一个文件
        File file2 = FileUtil.createDir(testDir);
        System.out.println(new Date(file.lastModified()).toLocaleString());
        Assert.assertEquals(file.lastModified(), file2.lastModified());

        file.deleteOnExit();
    }

    @Test
    public void testCreateDirIfNotExisted() {
        // 测试已存在的目录
        String dir = FileUtil.getTempDir("subDirName");
        File file = FileUtil.createDir(dir);
        Assert.assertTrue(file.exists() && file.isDirectory());

        // 测试不存在的目录
        String dir2 = FileUtil.getTempDir() + UUID.randomUUID().toString();
        File file2 = FileUtil.createDir(dir2);
        Assert.assertTrue(file2.exists() && file2.isDirectory());
    }

}
