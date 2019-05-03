package com.diqie.rbac.sso.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableOAuth2Sso
public class SsoClientConfig extends WebSecurityConfigurerAdapter {

    @Value("${diqie.sso.sso-logout-url}")
    private String ssoLogoutUrl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.csrf().disable();
        http.logout()
                .logoutSuccessUrl(ssoLogoutUrl)
                .deleteCookies("JSESSIONID")
                .and()
                .formLogin().loginPage("/login").permitAll().and()
                .authorizeRequests()
                .antMatchers("/index2.html").permitAll()
                .anyRequest().authenticated();

    }
}
