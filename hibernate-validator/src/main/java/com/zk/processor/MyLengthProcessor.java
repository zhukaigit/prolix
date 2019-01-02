package com.zk.processor;

import com.zk.annotation.MyLength;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by zhuk on 2017/7/24.
 */
public class MyLengthProcessor implements ConstraintValidator<MyLength, String> {

  private int max;
  private int min;

  @Override
  public void initialize(MyLength constraintAnnotation) {
    max = constraintAnnotation.max();
    min = constraintAnnotation.min();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.trim().length() == 0) return true;
    int length = value.length();
    return (length >= min && length <= max);
  }
}
