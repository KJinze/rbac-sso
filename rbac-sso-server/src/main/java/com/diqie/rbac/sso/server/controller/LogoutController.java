package com.diqie.rbac.sso.server.controller;

import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LogoutController {

    @RequestMapping("/logout")
    public void exit(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, null, null);
    }
}
