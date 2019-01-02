package compress;

/**
 * Created by csci on 17-6-6.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩文件工具类
 */
public class ZipUtils {

  /**
   * 描述：压缩指定目录下的文件到指定ZIP文件。
   * 注意：指定目录忽略子包中的文件
   * @param directoryFile 目标文件：要求是一个目录
   * @param destFile 压缩后的ZIP文件
   */
  public static void doCompress(File directoryFile, File destFile) {
    if (directoryFile == null || !directoryFile.exists()) throw new RuntimeException("目标文件不存在");
    if (directoryFile.isFile()) throw new RuntimeException("目标文件不是一个目录");
    List<File> fileList = Arrays.stream(directoryFile.listFiles()).collect(Collectors.toList());
    doCompress(fileList, destFile);
  }

  /**
   * 描述：压缩指定文件到指定ZIP文件。
   * 注意：指定目录忽略子包中的文件
   * @param fileList 指定文件，忽略所有的目录文件
   * @param destFile 压缩后的ZIP文件
   */
  public static void doCompress(List<File> fileList, File destFile) {
    final ZipOutputStream out;
    try {
      out = new ZipOutputStream(new FileOutputStream(destFile));
    } catch (FileNotFoundException e) {
      throw new RuntimeException("找不到目的地文件");
    }
    fileList.stream()
        .forEach(file ->  {
          if (file.isFile()) doCompress(file, out);
          if (file.isDirectory()) doCompress(file, destFile);
        });
  }

  /**
   * 描述：压缩指定文件到ZipOutputStream中
   */
  public static void doCompress(File file, ZipOutputStream out) {
    if (file == null || !file.exists()) return;
    try {
      doCompress(file.getName(), new FileInputStream(file), out);
    } catch (IOException e) {
      throw new RuntimeException("找不到文件", e);
    }
  }

  /**
   * 描述：压缩指定文件到ZipOutputStream中
   */
  public static void doCompress(ZipOutputStream out, File... files) {
    if (files==null || files.length==0) return;
    Arrays.stream(files).forEach(file -> {
      if (file.isFile()) doCompress(file, out);
      if (file.isDirectory()) {
        Arrays.stream(file.listFiles()).forEach(subFile -> {
          doCompress(subFile, out);
        });
      }
    });
  }

  /**
   * 描述：压缩指定输入流到ZipOutputStream中
   * @param elementFileName 子文件名
   * @param in 子文件输入流
   */
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
