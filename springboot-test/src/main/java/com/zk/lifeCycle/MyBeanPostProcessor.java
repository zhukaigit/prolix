package com.zk.lifeCycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 如上，BeanPostProcessor接口包括2个方法postProcessAfterInitialization和postProcessBeforeInitialization，
 * 这两个方法的第一个参数都是要处理的Bean对象，第二个参数都是Bean的name。返回值也都是要处理的Bean对象。这里要注意。
 */
@Component
@Profile ("springEvent")
public class MyBeanPostProcessor implements BeanPostProcessor {

  public MyBeanPostProcessor() {
    super();
    System.err.println("这是BeanPostProcessor实现类构造器！！");
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName)
      throws BeansException {
    System.err
        .println(
            "BeanPostProcessor接口方法postProcessAfterInitialization对属性进行更改！" + ", bean = " + bean
                .getClass().getSimpleName() + ", beanName = " + beanName);
    return bean;
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    System.err
        .println("BeanPostProcessor接口方法postProcessBeforeInitialization对属性进行更改！" + ", bean = " + bean
            .getClass().getSimpleName() + ", beanName = " + beanName);
    return bean;
  }
}
