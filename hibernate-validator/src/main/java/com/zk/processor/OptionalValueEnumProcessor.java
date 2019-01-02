package com.zk.processor;

import com.zk.annotation.OptionalValueEnum;
import com.zk.utils.EnumUtil;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * {@link ConstraintValidator<OptionalValueEnum, Object>}
 * 其中的Object为字段类型。
 */
public class OptionalValueEnumProcessor implements ConstraintValidator<OptionalValueEnum, Object> {
  private Class<? extends Enum> enumClass;
  private String constraintFiled;

  @Override
  public void initialize(OptionalValueEnum constraintAnnotation) {
    enumClass = constraintAnnotation.enumClass();
    constraintFiled = constraintAnnotation.constraintFiled();
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value==null || value.toString().trim().length()==0) return true;
    List<String> valueList = null;
    if ("name()".equals(constraintFiled)) {
      valueList = EnumUtil.getNameList(enumClass);
    } else {
      valueList = EnumUtil.getValueListByFieldName(enumClass, constraintFiled);
    }
    return valueList.contains(String.valueOf(value));
  }
}
