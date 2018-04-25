package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

import com.google.common.collect.Maps;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

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
 * 16/11/3     melvin                 Created
 */
public class JwtToken {

    private String token;
    private long expiresIn;
    private String tokenType;
    private String scope;

    private Map<String, Object> params;

    public JwtToken(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.tokenType = "Bearer";
        this.scope = "app";
    }

    public JwtToken merge(Map<String, Object> others) {
        if (others == null || others.size() == 0) {
            return this;
        }
        if (this.params == null) {
            this.params = Maps.newHashMap();
        }
        this.params.putAll(others);
        return this;
    }

    public boolean exists() {
        return StringUtils.isNotBlank(getToken());
    }

    public String getToken() {
        return token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getScope() {
        return scope;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    protected void setToken(String token) {
        this.token = token;
    }

    protected void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }


    @Override
    public String toString() {
        return String.format("token: %s, expires_in: %s, type: %s, scope: %s", token, expiresIn, tokenType, scope);
    }
}
