package com.zk.annotation.sub;

import com.zk.processor.sub.OnConditionFieldValueProcessor;

import java.lang.annotation.*;

/**
 * 描述：满足某条件时（即指定的属性等于某个值），该字段可为空
 */
@Target ({ElementType.FIELD})
@Retention (RetentionPolicy.RUNTIME)
@Documented
@ProcessorHandler (OnConditionFieldValueProcessor.class)
public @interface IsNullOnConditionFieldValue {
  // 所依赖的字段名
  String fieldName ();

  // 所依赖的字段名对应的值
  String fieldValue () ;
}
