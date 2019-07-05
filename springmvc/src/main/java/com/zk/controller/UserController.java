package com.zk.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@Slf4j
public class UserController {

    @Autowired
    private RestTemplate restTemplate;

    @ResponseBody
    @RequestMapping (value = "/health", method = RequestMethod.POST)
    public String health () {
        return "ok";
    }

    @ResponseBody
    @RequestMapping (value = "/testRestTemplate", method = RequestMethod.POST)
    public String testRestTemplate () {
        ResponseEntity<String> resp = restTemplate.getForEntity("https://www.baidu.com", String.class);
        return resp.getBody();
    }

}
