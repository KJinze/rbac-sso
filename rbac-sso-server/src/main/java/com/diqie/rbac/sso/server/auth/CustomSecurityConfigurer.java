package com.diqie.rbac.sso.server.auth;

import com.diqie.rbac.sso.server.service.Oauth2UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class CustomSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Oauth2UserDetailService userDetailService;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        //filter.setAuthenticationSuccessHandler(new SavedRequestAwareAuthenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/rbac/login?error"));

        CustomProvider provider = new CustomProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailService(userDetailService);
        http.authenticationProvider(provider)
                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
