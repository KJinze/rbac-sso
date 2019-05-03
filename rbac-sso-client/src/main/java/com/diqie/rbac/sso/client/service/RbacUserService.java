package com.diqie.rbac.sso.client.service;

import com.diqie.rbac.sso.domain.RbacUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.LinkedHashMap;

public class RbacUserService {

    public static RbacUser getCurrentUser(){
        LinkedHashMap map = (LinkedHashMap) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = (String)map.get("userName");
        RbacUser rbacUser = new RbacUser();
        rbacUser.setUserName(userName);
        //RbacUser rbacUser = JSON.parseObject(JSON.toJSONString(map),RbacUser.class);
        return rbacUser;
    }

    public static String getCurrentUserName(){
        RbacUser currentUser = getCurrentUser();
        if(currentUser!=null){
           return currentUser.getUserName();
        }
        return null;
    }
}
