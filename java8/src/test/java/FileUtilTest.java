import compress.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by zhukai on 2020/3/15
 */
public class FileUtilTest {

    @Test
    public void test_createNewFileIfNotExisted() {
        String tmpDir = System.getProperty("java.io.tmpdir");
        String testDir = tmpDir + File.separator + UUID.randomUUID().toString() + File.separator;
        String testFilePath = testDir + "test.txt";

        System.out.println("文件所在目录是否存在：" + new File(testDir).exists());
        System.out.println("文件是否存在：" + new File(testFilePath).exists());

        File file = FileUtil.createNewFileIfNotExisted(testFilePath);
        Assert.assertTrue(file.exists());

        Assert.assertTrue(file.delete());
        Assert.assertTrue(new File(testDir).delete());
    }

    @Test
    public void test() {
        String bizData = "orderNo=12000,electronicFileType=1";
        System.out.println(bizData.split(",")[0].split("=")[1]);
        System.out.println(bizData.split(",")[1].split("=")[1]);
    }
}
