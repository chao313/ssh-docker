package demo.spring.boot.demospringboot.config;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.ProviderManager;

import java.util.ArrayList;
import java.util.List;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.HttpAuthenticationEntryPoint;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.JwtAuthenticationProvider;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.JwtTokenFilter;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.token.TokenCache;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.token.TokenConfiguration;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.token.TokenValidator;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.JwtToken;
import demo.spring.boot.demospringboot.sdxd.common.redis.template.RedisClientTemplate;

/**
 * 2018/4/25    Created by   chao
 */
@Configuration
@Order(9)
public class TokenConfig {

    @Value(value = "${token.key.key}")
    private String key;
    @Value(value = "${token.key.path}")
    private String path;
    @Value(value = "${token.key.in.classpath}")
    private Boolean classPath;
    @Value(value = "${token.key.issuer}")
    private String issuer;
    @Value(value = "${token.key.expire_time}")
    private Long exprieTime;
    @Value(value = "${token.key.access.expire_time}")
    private Long accessExpireTime;


    HttpAuthenticationEntryPoint httpAuthenticationEntryPoint;

    /**
     * 配置token的密过期时间eg.
     */
    @Bean
    public TokenConfiguration getTokenConfiguration() {
        return new TokenConfiguration(key, path, classPath, issuer, exprieTime, accessExpireTime);
    }

    @Bean
    public TokenCache getTokenCache(RedisClientTemplate redisClientTemplate) {
        return new TokenCache(redisClientTemplate, "authentication:user:%");
    }

    @Bean
    public TokenValidator getTokenCache(TokenConfiguration tokenConfiguration, TokenCache tokenCache) {
        return new TokenValidator(tokenConfiguration, tokenCache);
    }

    @Bean
    public JwtTokenFilter getJwtTokenFilter(HttpAuthenticationEntryPoint httpAuthenticationEntryPoint, TokenValidator tokenValidator) {
        List arrayList = new ArrayList();
        arrayList.add(new JwtAuthenticationProvider());
        return new JwtTokenFilter("/api/pvt/**",
                new ProviderManager(arrayList),
                httpAuthenticationEntryPoint,
                tokenValidator);
    }


}
