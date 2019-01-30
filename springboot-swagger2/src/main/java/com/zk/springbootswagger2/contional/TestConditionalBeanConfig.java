package com.zk.springbootswagger2.contional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConditionalBeanConfig {

  @Bean
  @Conditional(WindowsCondition.class)
  public Person person1() {
    return new Person("Windows", 10);
  }

  @Bean
  @Conditional(MacOsCondition.class)
  public Person person2() {
    return new Person("MacOs", 10);
  }

}
