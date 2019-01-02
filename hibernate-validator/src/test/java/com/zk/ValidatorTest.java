package com.zk;

import com.zk.model.GroupModel;
import com.zk.model.GroupModel.GroupA;
import com.zk.model.GroupOrderModel;
import com.zk.model.GroupOrderModel.GroupOrderOne;
import com.zk.model.SubUser;
import com.zk.model.User;
import com.zk.utils.ValidatorUtil;
import com.zk.utils.ValidatorUtil.ValidatorResult;
import org.junit.After;
import org.junit.Test;

public class ValidatorTest {

  private ValidatorResult validatorResult = null;

  @After
  public void printResult() {
    if (!validatorResult.isValid()) {
      System.out.println(validatorResult.getErrorMsg());
    } else {
      System.out.println("validate successfully");
    }
  }

  @Test
  public void testMyAnnotation() {
    User user = new User();
    user.setUsername("zhukai");
    user.setUserType("个人的");
    user.setAge(10);
    user.setLoveStar("自己");
    validatorResult = ValidatorUtil.validate(user);
  }

  //结果：{nameAC=不能为空, nameAB=不能为空, nameA=不能为空}
  @Test
  public void testGroupA() {
    GroupModel model = new GroupModel();
    validatorResult = ValidatorUtil.validate(model, GroupA.class);
  }

  //覆盖夫类的校验注解
  @Test
  public void testExtends() {
    SubUser subUser = new SubUser();

    //子类有该字段，约束不同与父类
    subUser.setLoveStar("林俊杰");

    //子类有该字段，无任何约束
//    subUser.setAge(10);

    //子类没有该字段，约束继承自父类
    subUser.setCell("1301");
    validatorResult = ValidatorUtil.validate(subUser);
  }

  @Test
  public void testGroupOrdered() {
    GroupOrderModel model = new GroupOrderModel();
    model.setNameDefault(-1);
    model.setNameA(-1);
    model.setNameB(-1);
    model.setNameC(-1);
    model.setNameAB(-1);
    model.setNameAC(-1);
    //会按照GroupOrderOne上的GroupSequence注解指定顺序校验
    validatorResult = ValidatorUtil.validate(model, GroupOrderOne.class);

  }
}
