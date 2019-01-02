package com.zk.model;

import com.zk.annotation.OptionalValue;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2018/7/22.
 */
@Setter
@Getter
public class SubUser extends User {

  //覆盖父类
  @OptionalValue(collection = {"林俊杰", "潘玮柏"})
  private String loveStar;

  private Integer age;



}
