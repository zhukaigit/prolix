package compress;

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
     */
    public static File createNewFileIfNotExisted(String fileAbsolutePath) {
        File file = new File(fileAbsolutePath);
        if (file.exists()) {
            return file;
        }

        if (!file.getParentFile().exists()) {
            boolean mkdirs = file.getParentFile().mkdirs();
            AssertUtil.assertTrue(mkdirs, "文件所在目录创建失败");
        }
        try {
            AssertUtil.assertTrue(file.createNewFile(), "文件创建失败");
        } catch (IOException e) {
            log.error("文件创建失异常", e);
        }

        return file;
    }
}
