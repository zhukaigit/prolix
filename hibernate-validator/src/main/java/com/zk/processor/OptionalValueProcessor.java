package com.zk.processor;

import com.zk.annotation.OptionalValue;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OptionalValueProcessor implements ConstraintValidator<OptionalValue, Object> {
  private String[] collection;

  @Override
  public void initialize(OptionalValue constraintAnnotation) {
    collection = constraintAnnotation.collection();
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value == null || value.toString().trim().length() == 0) return true;
    for (String s : collection) {
      if (String.valueOf(value).equals(s)) return true;
    }
    return false;
  }
}
