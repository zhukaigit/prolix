package com.zk.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Documented
@Constraint(
    validatedBy = {}
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Min(0L)
@Max(9223372036854775807L)
@ReportAsSingleViolation
@Repeatable(Range.List.class)
public @interface Range {
  @OverridesAttribute(
      constraint = Min.class,
      name = "value"
  )
  long min() default 0L;

  @OverridesAttribute(
      constraint = Max.class,
      name = "value"
  )
  long max() default 9223372036854775807L;

  String message() default "{org.hibernate.validator.constraints.Range.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface List {
    Range[] value();
  }
}
