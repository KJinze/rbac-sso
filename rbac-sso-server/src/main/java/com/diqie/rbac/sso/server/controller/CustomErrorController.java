package com.diqie.rbac.sso.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController {
    @RequestMapping("/oauth/error")
    public String loginError(){
        return "登录错误";
    }
}
