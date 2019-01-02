package com.zk.model;

import com.zk.annotation.MyLength;
import com.zk.annotation.OptionalValue;
import com.zk.annotation.OptionalValueEnum;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2018/7/22.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User {

  @MyLength(max = 10, min = 4)
  private String username;

  @OptionalValueEnum(enumClass = BorrowerType.class, constraintFiled = "desc")
  private String userType;

  @OptionalValue(collection = {"刘德华", "周星驰"})
  private String loveStar;

  @NonNull
  @OptionalValue(collection = {"18", "25"})
  private Integer age;

  @Pattern(regexp = "(^\\d{15}$)\\|(^\\d{18}$)\\|(^\\d{17}(\\d\\|X\\|x)$)")
  private String cell;

  @OptionalValueEnum(enumClass = BorrowerType.class, constraintFiled = "desc")
  private String subjectType;

  @AllArgsConstructor
  @Getter
  public enum  BorrowerType {
    PERSONAL("个人", 1),
    COMPANY("企业", 2),
    ;
    private String desc;
    private int code;
  }
}
