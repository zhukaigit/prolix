package com.zk.springbootswagger2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.junit.Test;

/**
 * serialVersionUID适用于Java的序列化机制。简单来说，Java的序列化机制是通过判断类的serialVersionUID来
 * 验证版本一致性的。在进行反序列化时，JVM会把传来的字节流中的serialVersionUID与本地相应实体类的
 * serialVersionUID进行比较，如果相同就认为是一致的，可以进行反序列化，否则就会出现序列化版本不一致的异常，
 * 即是InvalidCastException。
 */
public class SerialVersionUIDTest {

  private final String basePath = "/Users/zhukai/temp";

  /**
   * 执行这个test时，serialVersionUID = 1234567890L
   * @throws Exception
   */
  @Test
  public void testSerialize() throws Exception {
    FileOutputStream fileOutputStream = null;
    ObjectOutputStream objectOutputStream = null;
    try {
      User user = new User("zk", 29);
      fileOutputStream = new FileOutputStream(new File(basePath + "/user.txt"));
      objectOutputStream = new ObjectOutputStream(fileOutputStream);
      objectOutputStream.writeObject(user);
      fileOutputStream.flush();
      objectOutputStream.flush();
    } finally {
      fileOutputStream.close();
      objectOutputStream.close();
    }
  }

  /**
   * 1、执行这个test时，serialVersionUID = 1234567890L，与写入时相同，可以成功反序列化
   * 2、执行这个test时，serialVersionUID = 123456789L，与写入时不同，反序列化失败
   */

  @Test
  public void testRead() throws Exception {
    FileInputStream fileInputStream = new FileInputStream(new File(basePath + "/user.txt"));
    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
    User user = (User) objectInputStream.readObject();
    System.out.println(user);
    fileInputStream.close();
    objectInputStream.close();
  }

  @Data
  @AllArgsConstructor
  @ToString
  private static class User implements Serializable {
    private static final long serialVersionUID = 1234567890L;
    private String name;
    private int age;
  }

}
