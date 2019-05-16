package com.zk.config;

import com.zk.lifeCycle.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
public class MyConfig {

  @Autowired
  private Environment environment;

  @Bean(initMethod = "myInit", destroyMethod = "myDestroy")
  @Profile ("springEvent")
  public Person person() {
    return new Person();
  }

  @Bean
  public BinderTestModel binderTestModel() {
    Binder binder = Binder.get(environment);
    BinderTestModel model = binder.bind("test.binder", BinderTestModel.class).get();
    return model;
  }

  @Bean
  public PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver(){
    return new PathMatchingResourcePatternResolver();
  }
}
