package com.zk.springbootswagger2.controller;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/log/")
@Slf4j
public class LogTestController {

    @PostMapping("query")
    public String post(@RequestBody User user) {
        log.info("do LogTestController.post api，param = {}", user);
        return "ok, time is " + LocalDateTime.now();
    }

    @PostMapping("query/{userId}")
    public String queryUser(
            @RequestHeader("aid") Long aid,
            @RequestParam String name,
            @PathVariable("userId") Long userId) {
        log.info("do LogTestController.query api，aid = {}, name = {}, userId = {}", aid, name, userId);
        return "ok, time is " + LocalDateTime.now();
    }

    @GetMapping("get")
    public String getUser(@RequestParam String name) {
        log.info("do LogTestController.query api，name = {}", name);
        return "ok, time is " + LocalDateTime.now();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Getter
    private static class User {
        private String name;
        private Integer age;
    }
}
