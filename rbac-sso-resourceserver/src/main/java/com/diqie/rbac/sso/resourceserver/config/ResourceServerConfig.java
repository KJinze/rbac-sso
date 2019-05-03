package com.diqie.rbac.sso.resourceserver.config;

import com.diqie.rbac.sso.resourceserver.auth.CustomerAccessTokenConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    ResourceServerProperties resourceServerProperties;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore());
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        tokenConverter.setVerifierKey(getPubKey());
        tokenConverter.setAccessTokenConverter(customerAccessTokenConverter());
        return tokenConverter;
    }

    private String getPubKey() {
        Resource resource = new ClassPathResource("rbac-sso-publickey.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            System.out.println("本地公钥");
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException ioe) {
            return getKeyFromAuthorizationServer();
        }
    }

    private String getKeyFromAuthorizationServer() {
        ObjectMapper objectMapper = new ObjectMapper();
        String pubKey = new RestTemplate().getForObject(resourceServerProperties.getJwt().getKeyUri(), String.class);
        try {
            Map map = objectMapper.readValue(pubKey, Map.class);
            System.out.println("联网公钥");
            return map.get("value").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public CustomerAccessTokenConverter customerAccessTokenConverter(){
        return new CustomerAccessTokenConverter();
    }
}
