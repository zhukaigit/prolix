package com.zk.springMybatis.transactionTest;

import com.zk.springMybatis.mapper.UserMapper;
import com.zk.springMybatis.model.UserInfo;
import java.lang.reflect.Proxy;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Transactional注解注意事项： 一、同一类中的方法内部的互调，事务失效。 一个有事务的方法，想要事务是生效的，在被调用时要注意： 1）在其他类被调用：都会生效
 * 2）本类被调用：不要直接调用，通过代理对象调用即可。 如本类中invokeWithProxy()，这样会让transactionMethod(String name)方法事务生效
 * 而invokeDirectly()，则会让transactionMethod(String name)方法事务失效
 *
 * 二、理解
 * 因为@Transactional是使用SpringAop实现的，而Aop是使用jdk动态代理或这cglib代理实现的。
 * 代理中增加的前置逻辑就包括校验是否需要手动开启事务，后置逻辑则包括校验是否需要手动提交事务。
 * 所以重点是方法调用的对象是代理对象还是原始对象。
 * 若是代理对象，那么会检查调用方法上是否有@Transactional注解，有则开启事务，没有则不开启事务。
 * 若是原始对象，那么肯定是不开启事务的。
 */
@Service
public class TransactionServiceImpl implements TransactionService {

  @Autowired
  private UserMapper userMapper;
  @Autowired
  private BeanFactory beanFactory;

  /**
   * 方法内直接调用，查看transactionMethod()是否会回滚 结论：报错语句之前的数据会插入，因为事务没有生效
   */
  @Override
  public void invokeDirectly() {
    try {
      this.transactionMethod("directly invoke");//直接调用内部方法，内部方法事务将失效
    } catch (Exception e) {
//      System.err.println(e.getMessage());
    }
  }

  /**
   * 方法内直接调用，查看transactionMethod()是否会回滚 结论：报错语句之前的数据会插入，因为事务没有生效
   */
  @Override
  public void invokeWithProxy() {
    try {
      TransactionService transactionService = beanFactory.getBean(TransactionService.class);//如果有切面逻辑，如@Transactional或者@AspectJ，那么从Spring容器中拿出来的bean都是代理类
      isProxy(transactionService);//校验是否是代理类。
      transactionService.transactionMethod("with proxy");//通过代理对象调用内部方法，内部方法事务不会失效
    } catch (Exception e) {
      System.err.println("错误信息： " + e.getMessage());
    }
  }

  //注意：默认情况下，指回滚RuntimeException的异常情况。如果所以异常或指定异常需要回滚，则要指定rollbackFor属性
  @Transactional(rollbackFor = {Exception.class})
  @Override
  public void transactionMethod(String name) throws Exception {
    insertUser(name);
    throw new RuntimeException("运行时异常");
//    throw new Exception("check error");
  }

  private void insertUser(String name) {
    UserInfo user = new UserInfo();
    user.setName(name);
    userMapper.insertSelective(user);
  }

  public static void isProxy(Object object) {
    Class clazz = object.getClass();
    boolean enhanced = Enhancer.isEnhanced(clazz);
    boolean proxyClass = Proxy.isProxyClass(clazz);
    if (enhanced) {
      System.out.println("是cglib代理");
    } else if (proxyClass) {
      System.out.println("是jdk代理");
    }
  }

  @Override
  @Transactional(propagation = Propagation.NESTED)
  public void insertUser2(String name) {
    insertUser(name);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void insertUser3(String name) {
    insertUser(name);
  }

  /**
   * 验证【Propagation.NESTED】需要随着外部事务的提交才能一起提交
   * @param name
   */
  @Override
  @Transactional
  public void testNested(String name) {
    TransactionService transactionService = beanFactory.getBean(TransactionService.class);
    transactionService.insertUser2(name);
    System.out.println("此时insertUser2()应该还没有提交");
  }

  /**
   * 验证【Propagation.NESTED】需要随着外部事务的回滚而回滚
   * @param name
   */
  @Override
  @Transactional
  public void testNestedRollback(String name) {
    TransactionService transactionService = beanFactory.getBean(TransactionService.class);
    transactionService.insertUser2(name);
    System.out.println("此时insertUser2()应该还没有提交");
    throw new RuntimeException("验证【Propagation.NESTED】需要随着外部事务的回滚而回滚");
  }

  @Override
  @Transactional
  public void testRequestNew(String name) {
    TransactionService transactionService = beanFactory.getBean(TransactionService.class);
    transactionService.insertUser3(name);
    System.out.println("此时insertUser2()应该提交了");
  }

}
