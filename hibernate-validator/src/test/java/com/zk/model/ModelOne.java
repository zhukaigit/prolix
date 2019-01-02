package com.zk.model;

import com.zk.annotation.Range;
import javax.validation.constraints.Pattern;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 业务场景，同一个字段可能根据不同的分组情况，校验的值的规范不一样，那么有可能在同一个字段上面有相同的注解，
 * 此时若填加注解类上没有 被@java.lang.annotation.Repeatable修饰，那么一般会报编译错误，如【duplicate
 * annotation】
 *
 * 解决方式：自定义注解，内容与要拓展的注解一样，区别除了包名不一致外，只需要在自定义注解上添加注解@Repeatable
 */
@Setter
public class ModelOne {

  @NotEmpty
  @Pattern(regexp = "[\\u4e00-\\u9fa5]{5,}", groups = GroupCompany.class)
  @Pattern(regexp = "[\\u4e00-\\u9fa5]{2,4}", groups = GroupPerson.class)
  private String name;

  @Range(min = 10, max = 20, groups = GroupPerson.class)
  @Range(min = 30, max = 40, groups = GroupCompany.class)
  private int age;

  //分组
  public interface GroupPerson {

  }

  public interface GroupCompany {

  }
}
