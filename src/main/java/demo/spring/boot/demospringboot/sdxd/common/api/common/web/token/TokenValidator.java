package demo.spring.boot.demospringboot.sdxd.common.api.common.web.token;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.RestContext;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.IAuthentication;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.InvalidTokenException;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.Subject;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.JwtToken;

import java.util.function.Supplier;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.JwtAuthentication.fail;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.JwtAuthentication.success;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode.AuthenticationError.INVALID_TOKEN;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode.AuthenticationError.TOKEN_EXPIRED;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.security
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 16/11/24     melvin                 Created
 */
public class TokenValidator {

    private TokenConfiguration tokenConfiguration;

    private TokenCache tokenCache;

    public TokenValidator(TokenConfiguration tokenConfiguration, TokenCache tokenCache) {
        this.tokenConfiguration = tokenConfiguration;
        this.tokenCache = tokenCache;
    }

    public TokenConfiguration getTokenConfiguration() {
        return tokenConfiguration;
    }

    public IAuthentication<JwtToken> validate(String token) {
        return validate(() -> TokenGenerator.isValid(tokenConfiguration, token));
    }

    public IAuthentication<JwtToken> validate(Supplier<TokenValidation> supplier) {
        TokenValidation validation = supplier.get();
        if (!validation.isValid()) {
            throw new InvalidTokenException(validation.getErrorCode());
        }

        Subject subject = validation.getSubject();
        JwtToken jwt = tokenCache.get(subject.getKey());
        if (!jwt.exists()) {
            return fail(TOKEN_EXPIRED.getCode(), TOKEN_EXPIRED.getMessage());
        }
        if (!validation.getToken().equals(jwt.getToken())) {
            return fail(INVALID_TOKEN.getCode(), INVALID_TOKEN.getMessage());
        }

        long expireTime = jwt.getExpiresIn();
        long currentTime = System.currentTimeMillis();
        boolean expired = currentTime >= expireTime;
        if (expired) {
            return fail(TOKEN_EXPIRED.getCode(), TOKEN_EXPIRED.getMessage());
        }
        tokenCache.touch(subject.getKey());
        RestContext.setSubject(subject);
        return success(
                subject,
                new JwtToken(validation.getToken(), validation.getExpiresIn()).merge(jwt.getParams()),
                validation.isAccessToken());
    }
}
