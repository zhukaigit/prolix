package com.zk.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class MyEvent extends ApplicationEvent {

  private String name;
  private int age;

  public MyEvent(Object source) {
    super(source);
  }

  public MyEvent(Object source, String name, int age) {
    super(source);
    this.name = name;
    this.age = age;
  }

  @Override
  public String toString() {
    return "MyEvent{" +
        "name='" + name + '\'' +
        ", age=" + age +
        '}';
  }
}
