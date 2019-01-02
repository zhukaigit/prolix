package com.zk.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@ApiModel
public class UserReqDto {
    @ApiModelProperty(value = "用户名", required = true, example = "zhukai")
    private String name;
    @ApiModelProperty(value = "id", required = true, example = "100")
    private Integer id;
}
