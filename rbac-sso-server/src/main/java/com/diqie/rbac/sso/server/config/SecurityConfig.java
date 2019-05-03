package com.diqie.rbac.sso.server.config;

import com.diqie.rbac.sso.server.auth.CustomPasswordEncoder;
import com.diqie.rbac.sso.server.auth.CustomSecurityConfigurer;
import com.diqie.rbac.sso.server.service.Oauth2UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    private Oauth2UserDetailService oauth2UserDetailService;

    @Autowired
    private CustomSecurityConfigurer customSecurityConfigurer;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/rbac/login").and()
                .formLogin().loginPage("/rbac/login").permitAll().and()
                .authorizeRequests()
                .antMatchers("/users/**","/js-lib/**", "/ui-thems/**","/qr/**","/qrcode/login","/rbac/stat/check","/qrcode/check","/rbac/auth")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable()
                .apply(customSecurityConfigurer);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(oauth2UserDetailService).passwordEncoder(passwordEncoder());
    }
}
