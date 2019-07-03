package com.zk.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

@RestController
@Slf4j
@RequestMapping("/feign")
public class TestFeignController {

    @GetMapping("/sleep")
    public String testTimeout(long sleepMills) throws InterruptedException {
        Thread.sleep(sleepMills);
        return "" + sleepMills;
    }

    @PostMapping("/queryUserDtoByName")
    public UserDto queryUserDtoByName(String name) {
        log.info("参数，{}", name);
        UserDto userDto = new UserDto(name, null);
        return userDto;
    }

    @ResponseBody
    @RequestMapping (value = "/findByModel", method = RequestMethod.POST)
    public UserDto findByModel(@RequestBody UserDto userDto) {
        log.info("查询user信息，入参：{}", userDto);
        return userDto;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class UserDto implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private Integer age;
    }
}
