package com.zk.springbootswagger2.contional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Person {
  private String name;
  private int age;
}
