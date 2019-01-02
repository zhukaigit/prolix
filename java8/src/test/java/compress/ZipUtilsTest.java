package compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.junit.Test;

@SuppressWarnings("all")
public class ZipUtilsTest {

  @Test
  public void test() throws FileNotFoundException {
    File file1 = new File("/Users/zhukai/Pictures/中文名/");
    File file2 = new File("/Users/zhukai/Pictures/中文名的副本/");
    List<File> fileList = Stream.of(file1, file2).collect(Collectors.toList());
    doCompress(getZipOutputStream("/Users/zhukai/testZip/我的压缩文件.zip"), file1, file2);
  }

  public static ZipOutputStream getZipOutputStream(String zipFileName) {
    try {
      File file = new File(zipFileName);
      return new ZipOutputStream(new FileOutputStream(file));
    } catch (FileNotFoundException e) {
      throw new RuntimeException("找不到目标文件");
    }
  }

  public static void doCompress(ZipOutputStream out, File... files) {
    if (files==null || files.length==0) return;
    Arrays.stream(files).forEach(file -> doCompress(out,file));
  }

  public static void doCompress(ZipOutputStream out, File file) {
    try {
      if (file==null || !file.exists()) return;
      if (file.isFile()) doCompress(file.getName(), new FileInputStream(file), out);
      if (file.isDirectory()) {
        Arrays.stream(file.listFiles()).forEach(subFile -> {
          doCompress(out, subFile);
        });
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException("找不到文件", e);
    }
  }

  public static void doCompress(String elementFileName, InputStream in, ZipOutputStream out) {
    byte[] buffer = new byte[2048];
    try {
      out.putNextEntry(new ZipEntry(elementFileName));
      int len = 0;
      // 读取文件的内容,打包到zip文件
      while ((len = in.read(buffer)) > 0) {
        out.write(buffer, 0, len);
      }
      out.flush();
      out.closeEntry();
      in.close();
    } catch (IOException e) {
      throw new RuntimeException("压缩文件异常", e);
    }
  }

}
