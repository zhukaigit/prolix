package com.zk.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2018/7/22.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class GroupModel {

  @NotEmpty(groups = GroupA.class)
  private String nameA;
  @NotEmpty(groups = GroupB.class)
  private String nameB;
  @NotEmpty(groups = GroupC.class)
  private String nameC;
  @NotEmpty(groups = {GroupA.class, GroupB.class})
  private String nameAB;
  @NotEmpty(groups = {GroupA.class, GroupC.class})
  private String nameAC;
  //不加groups属性，则为javax.validation.groups.Default这组，即@NotEmpty(groups = javax.validation.groups.Default.class)
  @NotEmpty
  private String nameDefault;

  public interface GroupA {

  }

  public interface GroupB {

  }

  public interface GroupC {

  }


}
