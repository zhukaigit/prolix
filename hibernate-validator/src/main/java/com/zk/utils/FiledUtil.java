package com.zk.utils;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FiledUtil {

  public static Field getDeclaredField(Class tClass, String fieldName) {
    Field field = null;
    try {
      field = tClass.getDeclaredField(fieldName);
      field.setAccessible(true);
    } catch (NoSuchFieldException e) {
      log.error("获取DeclaredFiled异常", e);
      throw new RuntimeException(MessageFormat.format("{0}没有该属性{1}", tClass.getName(), fieldName));
    }
    return field;
  }

  public static Object getFieldValue(Object obj, Field field) {
    field.setAccessible(true);
    Object o = null;
    try {
      o = field.get(obj);
    } catch (IllegalAccessException e) {
      log.error("获取属性值异常", e);
      throw new RuntimeException("获取属性值异常");
    }
    return o;
  }


}
