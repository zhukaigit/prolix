package com.zk.springbootswagger2.controller;


import java.util.Date;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

  @GetMapping("/health")
  public String health() {
    return "OK, time is " + new Date().toLocaleString();
  }

}
