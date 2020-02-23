package com.zk.annotation;

import java.lang.annotation.*;

/**
 * 描述：校验一批字段至少有一个不能为空。
 *
 * 用法：在校验对象上加上@AnyoneExist，并在那一批字段加上注解
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldGroup {

  // 为group指定名称
  String value() default "default";
}
