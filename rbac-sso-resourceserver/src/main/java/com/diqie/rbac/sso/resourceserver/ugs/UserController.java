package com.diqie.rbac.sso.resourceserver.ugs;

import com.diqie.rbac.sso.domain.RbacUser;
import com.diqie.rbac.sso.resourceserver.auth.CustomerSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/auth")
    public Object getAuthentication(){
        return  SecurityContextHolder.getContext().getAuthentication();
    }

    @PostMapping("/")
    public RbacUser getUser(){
        RbacUser currentUser = CustomerSecurityContext.getCurrentUser();
        return currentUser;
    }
    @GetMapping("/")
    public Principal user(Principal user){
        return user;
    }
}
