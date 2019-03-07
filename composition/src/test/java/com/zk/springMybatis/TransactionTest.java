package com.zk.springMybatis;

import static junit.framework.Assert.assertTrue;

import com.zk.springMybatis.transactionTest.TransactionService;
import java.lang.reflect.Proxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springMybatis/application.xml"})
public class TransactionTest {

  @Autowired
  private TransactionService transactionService;

  @Before
  public void before() {
    System.out.println("\r\n=========== before ===========\r\n");
  }
  @After
  public void after() {
    System.out.println("\r\n=========== after ===========\r\n");
  }

  /**
   * 校验是否是代理类
   */
  @Test
  public void test() {
    Class<? extends TransactionService> aClass = transactionService.getClass();
    boolean enhanced = Enhancer.isEnhanced(aClass);
    boolean proxyClass = Proxy.isProxyClass(aClass);
    if (enhanced) {
      System.out.println("是cglib代理");
    } else if (proxyClass) {
      System.out.println("是jdk代理");
    }
    assertTrue(enhanced || proxyClass);
  }

  /**
   * 方法内直接调用，事务不生效
   */
  @Test
  public void test1() {
    try {
      transactionService.invokeDirectly();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 同一service中，因方法内直接调用导致事务失效的解决方法
   * 解决方法：方法内使用代理对象调用
   */
  @Test
  public void test2() {
    try {
      transactionService.invokeWithProxy();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testNested() {
    transactionService.testNested("zk");
  }

  @Test
  public void testNestedRollback() {
    transactionService.testNestedRollback("zk");
  }

  @Test
  public void testRequestNew() {
    transactionService.testRequestNew("zk");
  }

}
