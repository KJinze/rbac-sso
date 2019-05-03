package com.diqie.rbac.sso.resourceserver.auth;

import com.alibaba.fastjson.JSON;
import com.diqie.rbac.sso.domain.RbacUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Map;

public class CustomerSecurityContext {

    public static RbacUser getCurrentUser(){
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
        Map decodedDetails = (Map)details.getDecodedDetails();
        Object rbacUserObj = decodedDetails.get("rbacUser");
        RbacUser rbacUser = JSON.parseObject(JSON.toJSONString(rbacUserObj), RbacUser.class);
        return rbacUser;
    };
}
