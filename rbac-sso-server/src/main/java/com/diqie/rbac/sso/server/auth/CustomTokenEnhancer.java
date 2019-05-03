package com.diqie.rbac.sso.server.auth;

import com.diqie.rbac.sso.domain.RbacUser;
import com.diqie.rbac.sso.domain.UserDetail;
import com.diqie.rbac.sso.server.service.RbacUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;


public class CustomTokenEnhancer implements TokenEnhancer {

    @Autowired
    RbacUserService rbacUserService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
        UserDetail userDetail= (UserDetail)authentication.getPrincipal();
        String username = userDetail.getUsername();
        RbacUser rbacUser = rbacUserService.getRbacUserByUserName(username);
        rbacUser.setUserPassword(null);
        //获取用户菜单
        Map<String, Object> info = new HashMap<>();
        info.put("rbacUser", rbacUser);
        token.setAdditionalInformation(info);
        return token;
    }
}
