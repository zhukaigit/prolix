package com.zk.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class UserController {

    @ResponseBody
    @RequestMapping (value = "/health", method = RequestMethod.POST)
    public String health () {
        return "ok";
    }

}
