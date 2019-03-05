package com.zk.config;

import com.zk.lifeCycle.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {

  @Bean(initMethod = "myInit", destroyMethod = "myDestroy")
  public Person person() {
    return new Person();
  }
}
