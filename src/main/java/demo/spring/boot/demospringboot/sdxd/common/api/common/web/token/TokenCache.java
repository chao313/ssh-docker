package demo.spring.boot.demospringboot.sdxd.common.api.common.web.token;


import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.Cache;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.JwtToken;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.KryoSerializer;
import demo.spring.boot.demospringboot.sdxd.common.redis.template.RedisClientTemplate;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

import static java.lang.System.currentTimeMillis;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.api.service
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 16/11/23     melvin                 Created
 */
public class TokenCache extends Cache {

    private static final Logger log = LoggerFactory.getLogger(TokenCache.class);

    public static class JwtTokenHolder extends JwtToken {

        private Supplier<String> tokenSupplier;
        private LongSupplier expiresInSupplier;
        private Supplier<Map<String, Object>> paramsSupplier;

        public JwtTokenHolder(
                Supplier<String> tokenSupplier,
                LongSupplier expiresInSupplier,
                Supplier<Map<String, Object>> paramsSupplier) {
            super(null, -1);
            this.tokenSupplier = tokenSupplier;
            this.expiresInSupplier = expiresInSupplier;
            this.paramsSupplier = paramsSupplier;
        }

        @Override
        public String getToken() {
            if (StringUtils.isBlank(super.getToken())) {
                super.setToken(tokenSupplier.get());
            }
            return super.getToken();
        }

        @Override
        public long getExpiresIn() {
            if (super.getExpiresIn() <= 0) {
                super.setExpiresIn(expiresInSupplier.getAsLong());
            }
            return super.getExpiresIn();
        }

        @Override
        public Map<String, Object> getParams() {
            if (super.getParams() == null) {
                super.merge(paramsSupplier.get());
            }
            return super.getParams();
        }
    }

    public static final String DEFAULT_TOKEN_KEY_TEMPLATE = "token:user:%s";

    private static final String TOKEN_FIELD_KEY = "token";
    private static final String TOKEN_EXPIRES_IN_FIELD_KEY = "expiresIn";
    private static final String TOKEN_EXPIRE_FIELD_KEY = "expireTime";
    private static final String TOKEN_PARAMS_FIELD_KEY = "params";

    private String tokenKeyTemplate;

    public TokenCache(RedisClientTemplate redisClientTemplate, String tokenKeyTemplate) {
        super(redisClientTemplate);
        this.tokenKeyTemplate = tokenKeyTemplate;
    }

    public String getTokenKeyTemplate() {
        return tokenKeyTemplate;
    }

    public JwtToken get(String key) {
        String cacheKey = String.format(getTokenKeyTemplate(), key);
        return new JwtTokenHolder(
                () -> getRedis().hget(cacheKey, TOKEN_FIELD_KEY),
                () -> {
                    String value = getRedis().hget(cacheKey, TOKEN_EXPIRE_FIELD_KEY);
                    return StringUtils.isBlank(value) ? 0 : Long.parseLong(value);
                },
                () -> {
                    byte[] cacheKeyBytes = KryoSerializer.writeString(cacheKey);
                    byte[] paramsFieldBytes = KryoSerializer.writeString(TOKEN_PARAMS_FIELD_KEY);
                    byte[] paramsBytes = getRedis().hget(cacheKeyBytes, paramsFieldBytes);
                    Object value = KryoSerializer.read(paramsBytes);
                    //noinspection unchecked
                    return (Map<String, Object>) value;
                });
    }

    public void touch(String key) {
        touch(key, -1);
    }

    public void put(String key, JwtToken token) {
        String cacheKey = String.format(getTokenKeyTemplate(), key);
        getRedis().hdel(cacheKey, TOKEN_FIELD_KEY);
        getRedis().hdel(cacheKey, TOKEN_EXPIRES_IN_FIELD_KEY);
        getRedis().hdel(cacheKey, TOKEN_EXPIRE_FIELD_KEY);
        getRedis().hdel(cacheKey, TOKEN_PARAMS_FIELD_KEY);

        getRedis().hset(cacheKey, TOKEN_FIELD_KEY, token.getToken());

        if (token.getParams() != null && token.getParams().size() > 0) {
            byte[] cacheKeyBytes = KryoSerializer.writeString(cacheKey);
            byte[] paramsFieldBytes = KryoSerializer.writeString(TOKEN_PARAMS_FIELD_KEY);
            byte[] paramsBytes = KryoSerializer.write(token.getParams());
            getRedis().hset(cacheKeyBytes, paramsFieldBytes, paramsBytes);
        }

        touch(key, token.getExpiresIn());
    }

    public boolean remove(String key) {
        String cacheKey = String.format(getTokenKeyTemplate(), key);
        Long delTokenResult = getRedis().hdel(cacheKey, TOKEN_FIELD_KEY);
        Long delExpiresInResult = getRedis().hdel(cacheKey, TOKEN_EXPIRES_IN_FIELD_KEY);
        Long delExpireResult = getRedis().hdel(cacheKey, TOKEN_EXPIRE_FIELD_KEY);
        Long delParamsResult = getRedis().hdel(cacheKey, TOKEN_PARAMS_FIELD_KEY);
        return delTokenResult != null || delExpiresInResult != null || delExpireResult != null || delParamsResult != null;
    }

    private void touch(String key, long expiresIn) {
        String cacheKey = String.format(getTokenKeyTemplate(), key);
        if (expiresIn < 0) {
            String value = getRedis().hget(cacheKey, TOKEN_EXPIRES_IN_FIELD_KEY);
            expiresIn = TokenConfiguration.getDefaultTokenExpireTime();
            try {
                if (StringUtils.isNotBlank(value)) {
                    expiresIn = Long.parseLong(value);
                }
            } catch (Exception e) {
                log.warn("Field \"expiresIn\" not found in demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.cache, set default value {}", TokenConfiguration.getDefaultTokenExpireTime());
            }
        }
        long expires = expiresIn * 1000;
        long currentTime = currentTimeMillis();
        long expireTime = currentTime + expires;
        String value = String.valueOf(expireTime);

        getRedis().hset(cacheKey, TOKEN_EXPIRES_IN_FIELD_KEY, String.valueOf(expiresIn));
        getRedis().hset(cacheKey, TOKEN_EXPIRE_FIELD_KEY, value);
        getRedis().expire(cacheKey, (int) expiresIn);
    }
}
