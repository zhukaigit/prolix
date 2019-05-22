package com.zk.annotation;

import com.zk.processor.RelatedValidateProcessor;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2018/7/22.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = RelatedValidateProcessor.class)
@Inherited
public @interface RelatedValidate {

  String message () default "";

  Class<?>[] groups () default {};

  Class<? extends Payload>[] payload () default {};
}
