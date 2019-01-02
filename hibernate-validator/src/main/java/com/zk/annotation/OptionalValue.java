package com.zk.annotation;

import com.zk.processor.OptionalValueProcessor;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 描述：校验属性值是否在集合中
 * 注解适用的属性字段类型：String，Number
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = OptionalValueProcessor.class)
public @interface OptionalValue {

  //可选值集合
  String[] collection();

  String message() default "可选值范围{collection}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
