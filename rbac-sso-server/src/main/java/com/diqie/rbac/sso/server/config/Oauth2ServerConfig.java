package com.diqie.rbac.sso.server.config;

import com.diqie.rbac.sso.server.auth.CustomTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableAuthorizationServer
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 注入authenticationManager
     * 来支持 password grant type
     */
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.realm("oauth")
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("ugs")
             .secret("1b25ea4e3ede6fc047c018cb9b5ddc8b")//71214
            //.resourceIds("oauth")
            .authorizedGrantTypes("password","authorization_code", "refresh_token")//设置验证方式
            .accessTokenValiditySeconds(60 * 60 * 12 * 3)//过期时间
            .refreshTokenValiditySeconds(60 * 60 * 24 * 7)
             //.redirectUris("http://localhost/callback")//可指定也可以由客户端携带
            .scopes("all")
            .autoApprove(true);//默认登录后可直接授权
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancer = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<TokenEnhancer>();
        delegates.add(customTokenEnhancer());
        delegates.add(jwtAccessTokenConverter());
        tokenEnhancer.setTokenEnhancers(delegates);

        endpoints.tokenStore(tokenStore()).accessTokenConverter(jwtAccessTokenConverter()).tokenEnhancer(tokenEnhancer)
                .authenticationManager(authenticationManager);//配置支持的授权类型
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        KeyStoreKeyFactory keyFactory = new KeyStoreKeyFactory(new ClassPathResource("diqie.keystore"),"diqierbac".toCharArray());
        tokenConverter.setKeyPair(keyFactory.getKeyPair("rbac-sso"));

        //配置自定义用户提取器
        /*DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
        defaultAccessTokenConverter.setUserTokenConverter(new CustomUserAuthenticationConverter());
        tokenConverter.setAccessTokenConverter(defaultAccessTokenConverter);*/

        return tokenConverter;
    }

    @Bean
    public TokenEnhancer customTokenEnhancer() {
        return new CustomTokenEnhancer();
    }

}
