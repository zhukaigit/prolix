package com.zk.springbootswagger2.common;

import com.zk.springbootswagger2.contional.Person;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ConditionTest {

  @Autowired
  private ApplicationContext applicationContext;

  /**
   * 设置环境变量用于测试：name = windows
   */
  @Test
  public void test() {
    Map<String, Person> beansOfType = applicationContext.getBeansOfType(Person.class);
    System.err.println("注入结果：" + beansOfType);
  }

}
