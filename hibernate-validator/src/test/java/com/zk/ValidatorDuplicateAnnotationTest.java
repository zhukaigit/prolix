package com.zk;

import com.zk.model.ModelOne;
import com.zk.model.ModelOne.GroupCompany;
import com.zk.utils.ValidatorUtil;
import com.zk.utils.ValidatorUtil.ValidatorResult;
import javax.validation.groups.Default;
import org.junit.After;
import org.junit.Test;

public class ValidatorDuplicateAnnotationTest {

  private ValidatorResult validatorResult = null;

  @After
  public void printResult() {
    if (!validatorResult.isValid()) {
      System.out.println(validatorResult.getErrorMsg());
    } else {
      System.out.println("validate successfully");
    }
  }

  //对同一字段，不同组不同的校验
  @Test
  public void test() {
    ModelOne modelOne = new ModelOne();
    modelOne.setName("朱凯");
    modelOne.setAge(18);

//    modelOne.setName("上海科技公司");
//    modelOne.setAge(35);

    validatorResult = ValidatorUtil.validate(modelOne, GroupCompany.class, Default.class);
//    validatorResult = ValidatorUtil.validate(modelOne, GroupPerson.class, Default.class);


  }

}
