package com.zk.lifeCycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile ("springEvent")
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

  public MyBeanFactoryPostProcessor() {
    super();
    System.err.println("这是BeanFactoryPostProcessor实现类构造器！！");
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
      throws BeansException {
    System.err
        .println("BeanFactoryPostProcessor调用postProcessBeanFactory方法 =================== 开始");
    BeanDefinition beanDefinition = beanFactory.getBeanDefinition("person");
    beanDefinition.getPropertyValues().addPropertyValue("phone", "110");
    System.err
        .println("BeanFactoryPostProcessor调用postProcessBeanFactory方法 =================== 结束");
  }
}