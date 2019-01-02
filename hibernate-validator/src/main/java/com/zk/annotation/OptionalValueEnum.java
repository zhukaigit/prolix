package com.zk.annotation;

import com.zk.processor.OptionalValueEnumProcessor;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 描述：校验是否在指定枚举的指定属性值中。
 * 默认为枚举的name()
 *
 * 注解适用的属性字段类型：String，Number
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = OptionalValueEnumProcessor.class)
public @interface OptionalValueEnum {

  //指定枚举类型
  Class<? extends Enum> enumClass();

  //指定枚举中属性名，默认为枚举的常量名
  String constraintFiled() default "name()";

  String message() default "可选值范围参照枚举{enumClass}的{constraintFiled}属性值";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
