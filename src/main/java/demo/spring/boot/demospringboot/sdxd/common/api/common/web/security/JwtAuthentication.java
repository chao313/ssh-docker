package demo.spring.boot.demospringboot.sdxd.common.api.common.web.security;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.JwtToken;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.RestResponse.SUCCESS_CODE;

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
 * 16/10/27     melvin                 Created
 */
public class JwtAuthentication extends AuthenticationStatus implements IAuthentication<JwtToken> {

    public static IAuthentication<JwtToken> fail(String code, String message) {
        return new JwtAuthentication(code, message, null, null, false);
    }

    public static IAuthentication<JwtToken> success(Subject subject, JwtToken token, boolean access) {
        return new JwtAuthentication(SUCCESS_CODE, null, subject, token, access);
    }

    private Subject subject;
    private JwtToken token;
    private boolean access;

    private boolean authenticated;
    private Collection<GrantedAuthority> authorities;

    private JwtAuthentication(String code, String message, Subject subject, JwtToken token, boolean access) {
        super(code, message);

        this.subject = subject;
        this.token = token;
        this.access = access;
    }

    @Override
    public JwtToken getToken() {
        return token;
    }

    @Override
    public AuthenticationStatus status() {
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getDetails() {
        return subject;
    }

    @Override
    public Object getPrincipal() {
        return subject == null ? null : subject.getValue();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return "cellphone";
    }

    @Override
    public boolean isAccess() {
        return access;
    }
}
