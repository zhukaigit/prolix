package com.zk.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EnumUtil {

  /**
   * 获取一个枚举的nameList
   * name： the name of constant
   */
  public static <E extends Enum<E>> List<String> getNameList(Class<E> enumClass) {
    ArrayList<String> list = new ArrayList<>();
    E[] constants = enumClass.getEnumConstants();
    Arrays.stream(constants).forEach(e -> list.add(e.name()));
    return Collections.unmodifiableList(list);
  }

  /**
   * 获取枚举中对应属性的属性值列表
   */
  public static <E extends Enum<E>> List<String> getValueListByFieldName(Class<E> enumClass, String fieldName) {
    ArrayList<String> list = new ArrayList<>();
    E[] constants = enumClass.getEnumConstants();
    Arrays.stream(constants).forEach(e -> {
      String name = e.name();
      String val = getValueByFieldName(enumClass, name, fieldName);
      list.add(val);
    });
    return Collections.unmodifiableList(list);
  }

  /**
   *
   * 获取某个常量的一个属性值
   * 对应常量为：@Param(name)
   * 对应属性为：@Param(constraintFiled)
   *
   * @param enumClass 枚举Class对象
   * @param constantName 常量名
   * @param fieldName 属性明
   */
  public static <E extends Enum<E>> String getValueByFieldName(Class<E> enumClass, String constantName, String fieldName) {
    E e = getEnumByName(enumClass, constantName);
    Field field = FiledUtil.getDeclaredField(enumClass, fieldName);
    Object fieldVal = FiledUtil.getFieldValue(e, field);
    return String.valueOf(fieldVal);
  }

  /**
   * 获取对应name的枚举常量
   * @param enumClass 枚举Class对象
   * @param constantName 常量名称
   */
  public static <E extends Enum<E>> E getEnumByName(Class<E> enumClass, String constantName) {
    if (null == constantName || constantName.trim().length() == 0) {
      return null;
    }
    E[] os = enumClass.getEnumConstants();
    for (E o : os) {
      if (o.name().equals(constantName)) {
        return o;
      }
    }
    return null;
  }
}
