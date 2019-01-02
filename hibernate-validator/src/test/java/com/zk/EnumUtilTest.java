package com.zk;

import com.zk.model.User.BorrowerType;
import com.zk.utils.EnumUtil;
import java.util.List;
import org.junit.Test;

public class EnumUtilTest {

  @Test
  public void test() {
    BorrowerType borrowerType = EnumUtil.getEnumByName(BorrowerType.class, "COMPANY");
    System.out.println(borrowerType.getDesc());

  }

  @Test
  public void test2() {
    String val = EnumUtil.getValueByFieldName(BorrowerType.class, "COMPANY", "code");
    System.out.println(val);
  }

  @Test
  public void testGetValueList() {
    List val = EnumUtil.getValueListByFieldName(BorrowerType.class, "code");
    System.out.println(val);
  }

  @Test
  public void testGetNameList() {
    System.out.println(EnumUtil.getNameList(BorrowerType.class));
  }

}
