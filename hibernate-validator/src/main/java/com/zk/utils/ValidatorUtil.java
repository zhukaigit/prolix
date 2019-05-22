package com.zk.utils;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 添加依赖：
 * org.hibernate:hibernate-validator:5.4.1.Final
 * org.glassfish.web:javax.el:2.2.6
 *
 * Created by zhuk on 2017/7/24.
 */
public class ValidatorUtil {

  // 用于存储参数错误信息
  public static final ThreadLocal<Map<String, String>> ERR_MSG_CONTAINER =
          ThreadLocal.withInitial(() -> new HashMap<String, String>());

  /**
   * 默认校验Default.class
   */
  public static <T> ValidatorResult validate(T t) {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Set<ConstraintViolation<T>> validate = validator.validate(t, Default.class);
    return getValidateResult(validate);
  }

  /**
   * 校验指定分组
   */
  public static <T> ValidatorResult validate(T t, Class... group) {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Set<ConstraintViolation<T>> validate = validator.validate(t, group);
    return getValidateResult(validate);
  }

  private static <T> ValidatorResult getValidateResult(Set<ConstraintViolation<T>> validate) {
    try {
      Objects.requireNonNull(validate, "入参validate不能为空");
      ValidatorResult validatorResult = new ValidatorResult();
      boolean isValid = true;
      Iterator<ConstraintViolation<T>> iterator = validate.iterator();
      while (iterator.hasNext()) {
        isValid = false;
        ConstraintViolation<T> next = iterator.next();
        String propertyPath = next.getPropertyPath().toString();
        String message = next.getMessage();
        if (!propertyPath.trim().isEmpty()) {
          ERR_MSG_CONTAINER.get().put(propertyPath, message);
        }
      }
      validatorResult.setErrorMsg(ERR_MSG_CONTAINER.get());
      validatorResult.setValid(isValid);

      return validatorResult;
    } finally {
      ERR_MSG_CONTAINER.remove();
    }
  }

  @Setter
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @ToString
  public static class ValidatorResult {
    boolean isValid;
    Map<String, String> errorMsg;
  }

}
