package com.zk.controller;

import com.zk.constant.annotation.ApiDocOne;
import com.zk.model.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@Api(description = "订单模块API")
public class OrderController {

    @ApiDocOne
    @ApiOperation("根据ID查询订单信息")
    @ResponseBody
    @RequestMapping(value = "/query/{orderId}", method = RequestMethod.POST)
    public ResponseVo<Map> queryOrder(
            @ApiParam(value = "订单id", required = true)
            @PathVariable("orderId") Long orderId
    ) {
        log.info("查询订单信息，orderId：{}", orderId);
        Map<String, Object> content = new HashMap<String, Object>();
        content.put("orderId", orderId);
        return ResponseVo.success(content);
    }

    @ApiOperation("根据List<Id>批量查询订单信息")
    @RequestMapping(value = "/query/order/list", method = RequestMethod.POST)
    @ResponseBody
    public ResponseVo<Map> queryOrderList(
            @ApiParam(value = "一批订单id", required = true)
            @RequestBody List<Long> orderIdList
    ) {
        log.info("查询订单信息，orderIdList：{}", orderIdList);
        Map<String, Object> content = new HashMap<String, Object>();
        content.put("orderIdList", orderIdList);
        return ResponseVo.success(content);
    }
}
