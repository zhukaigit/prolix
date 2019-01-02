package com.zk.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class ResponseVo<T> {
    @ApiModelProperty(value = "返回码", required = true)
    private Integer code;
    @ApiModelProperty(value = "返回信息", required = true)
    private String msg;
    @ApiModelProperty(value = "业务数据", required = true)
    private T data;

    public static <T> ResponseVo success(T data) {
        ResponseVo<T> tResponseVo = new ResponseVo<>();
        tResponseVo.code = 200;
        tResponseVo.msg = "success";
        tResponseVo.data = data;
        return tResponseVo;
    }

    public static <T> ResponseVo error(T data) {
        ResponseVo<T> tResponseVo = new ResponseVo<>();
        tResponseVo.code = 500;
        tResponseVo.msg = "error";
        tResponseVo.data = data;
        return tResponseVo;
    }
}
