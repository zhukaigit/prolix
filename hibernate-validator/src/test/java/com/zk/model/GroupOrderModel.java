package com.zk.model;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

/**
 * 描述：该model主要验证分组校验时的先后顺序
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class GroupOrderModel {

  @Range(min = 0, max = 10, groups = GroupA.class)
  private Integer nameA;
  @Range(min = 0, max = 10, groups = GroupB.class)
  private Integer nameB;
  @Range(min = 0, max = 10, groups = GroupC.class)
  private Integer nameC;
  @Range(min = 0, max = 10, groups = {GroupA.class, GroupB.class})
  private Integer nameAB;
  @Range(min = 0, max = 10, groups = {GroupA.class, GroupC.class})
  private Integer nameAC;
  @Range(min = 0, max = 10)
  private Integer nameDefault;

  public interface GroupA {

  }

  public interface GroupB {

  }

  public interface GroupC {

  }

  //校验时，会按照下面的分组顺序校验
  @GroupSequence({Default.class, GroupA.class, GroupB.class})
  public interface GroupOrderOne {

  }


}
