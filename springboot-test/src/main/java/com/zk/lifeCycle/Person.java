package com.zk.lifeCycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 二、各种接口方法分类
 *
 * Bean的完整生命周期经历了各种方法调用，这些方法可以划分为以下几类：
 *
 * 1、Bean自身的方法　　：　　这个包括了Bean本身调用的方法和通过配置文件中<bean>的init-method和destroy-method指定的方法
 *
 * 2、Bean级生命周期接口方法　　：　　这个包括了BeanNameAware、BeanFactoryAware、InitializingBean和DiposableBean这些接口的方法
 *
 * 3、容器级生命周期接口方法　　：　　这个包括了InstantiationAwareBeanPostProcessor 和 BeanPostProcessor
 * 这两个接口实现，一般称它们的实现类为“后处理器”。
 *
 * 4、工厂后处理器接口方法　　：　　这个包括了AspectJWeavingEnabler, ConfigurationClassPostProcessor,
 * CustomAutowireConfigurer等等非常有用的工厂后处理器　　接口的方法。工厂后处理器也是容器级的。在应用上下文装配配置文件之后立即调用。
 */
public class Person implements BeanFactoryAware, BeanNameAware, InitializingBean, DisposableBean {

  private String name;
  private String address;
  private int phone;

  private BeanFactory beanFactory;
  private String beanName;

  public Person() {
    System.err.println("【构造器】调用Person的构造器实例化");
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    System.err.println("【注入属性】注入属性name");
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    System.err.println("【注入属性】注入属性address");
    this.address = address;
  }

  public int getPhone() {
    return phone;
  }

  public void setPhone(int phone) {
    System.err.println("【注入属性】注入属性phone" + ", phone = " + phone);
    this.phone = phone;
  }

  @Override
  public String toString() {
    return "Person [address=" + address + ", name=" + name + ", phone="
        + phone + "]";
  }

  @PostConstruct
  public void init() {
    System.err.println("【@PostConstruct】");
  }

  @PreDestroy
  public void destroy2() {
    System.err.println("【@PreDestroy】");
  }

  // 这是BeanFactoryAware接口方法
  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    System.err
        .println("【BeanFactoryAware接口】调用BeanFactoryAware.setBeanFactory()");
    this.beanFactory = beanFactory;
  }

  // 这是BeanNameAware接口方法
  @Override
  public void setBeanName(String arg0) {
    System.err.println("【BeanNameAware接口】调用BeanNameAware.setBeanName(), param = " + arg0);
    this.beanName = arg0;
  }

  // 这是InitializingBean接口方法
  @Override
  public void afterPropertiesSet() throws Exception {
    System.err
        .println("【InitializingBean接口】调用InitializingBean.afterPropertiesSet()");
  }

  // 这是DiposibleBean接口方法
  @Override
  public void destroy() throws Exception {
    System.err.println("【DiposibleBean接口】调用DiposibleBean.destory()");
  }

  // 通过<bean>的init-method属性指定的初始化方法
  public void myInit() {
    System.err.println("【init-method】调用<bean>的init-method属性指定的初始化方法");
  }

  // 通过<bean>的destroy-method属性指定的初始化方法
  public void myDestroy() {
    System.err.println("【destroy-method】调用<bean>的destroy-method属性指定的初始化方法");
  }
}
