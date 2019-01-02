package com.zk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.Test;

public class CommonTest {
  @Test
  public void test() {
    String s = BorrowerType.COMPANY.toString();
    System.out.println(s);

  }

  @AllArgsConstructor
  @Getter
  public enum BorrowerType {
    COMPANY("企业"),
    PERSONAL("个人"),
    ;
    private String desc;
  }

}
