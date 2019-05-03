package com.diqie.rbac.sso.client.controller;

import com.diqie.rbac.sso.client.service.RbacUserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user")
    public Object getUser(){
        return RbacUserService.getCurrentUser();
    }

    @GetMapping("/token")
    public Object getToken(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
