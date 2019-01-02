package com.zk.annotation;

import com.zk.processor.MyLengthProcessor;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2018/7/22.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MyLengthProcessor.class)
@Inherited
public @interface MyLength {

  int max() default 2147483647;

  int min() default 0;

  String message() default "长度应该在{min}与{max}之间";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
