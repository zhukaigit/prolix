package com.zk.springbootswagger2.contional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class WindowsCondition implements Condition {

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    String name = context.getEnvironment().getProperty("name");
    if ("windows".equalsIgnoreCase(name)) {
      return true;
    }
    return false;
  }
}
