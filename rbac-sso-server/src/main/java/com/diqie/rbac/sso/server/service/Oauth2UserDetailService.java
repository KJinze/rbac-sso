package com.diqie.rbac.sso.server.service;

import com.diqie.rbac.sso.domain.RbacUser;
import com.diqie.rbac.sso.domain.UserDetail;
import com.diqie.rbac.sso.server.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

@Component
public class Oauth2UserDetailService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(Oauth2UserDetailService.class);

    @Autowired
    RbacUserService rbacUserService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("用户名：",username);
        if(username.length()==36){
            String relUsername = redisUtil.get(username).toString();
            if(relUsername==null){
                throw new UsernameNotFoundException("用户不存在");
            }
            logger.debug("根据pageId：{}获取用户：{}",username,relUsername);
            username = relUsername;
        }
        RbacUser rbacUser = rbacUserService.getRbacUserByUserName(username);
        if (rbacUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        //获取oauth资源权限
        authorities.add(new SimpleGrantedAuthority("BASE"));
        UserDetail userDetail = new UserDetail(rbacUser.getUserId(), rbacUser.getUserName(), rbacUser.getUserPassword(),authorities, true, true, true, true);
        return userDetail;
    }
}

