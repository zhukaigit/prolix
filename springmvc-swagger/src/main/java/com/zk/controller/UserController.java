package com.zk.controller;

import com.zk.constant.annotation.ApiDocTwo;
import com.zk.model.RequestVo;
import com.zk.model.ResponseVo;
import com.zk.model.request.UserReqDto;
import com.zk.model.response.UserResDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@Slf4j
@Api(description = "用户模块API")
public class UserController {

    @ApiDocTwo
    @ApiOperation("查询User信息")
    @ResponseBody
    @RequestMapping (value = "/query/user", method = RequestMethod.POST)
    public ResponseVo<UserResDto> queryUser(
            @ApiParam @RequestBody RequestVo<UserReqDto> requestVo) {
        log.info("查询user信息，入参：{}", requestVo.getData());
        UserResDto user = new UserResDto.UserResDtoBuilder()
                .setAge(18)
                .setFav("电影")
                .setName("朱凯")
                .setSalary("300000")
                .setApplyTime(new Date())
                .build();
        return ResponseVo.success(user);

    }


}
