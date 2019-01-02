package com.zk.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ApiModel
public class RequestVo<T> {
    @ApiModelProperty(value = "渠道编号", required = true, example = "JSY")
    private String channel;
    @ApiModelProperty(value = "业务数据", required = true)
    private T data;
}
