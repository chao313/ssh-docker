package demo.spring.boot.demospringboot.sdxd.common.api.common.web.security;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.auth.AuthenticationApiHolder;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.auth.IAuthenticationApi;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.auth.ICredential;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.JsonUtil;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.JwtToken;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ProcessBizException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.api.app.security
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 16/10/27     melvin                 Created
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private boolean postOnly = true;

    private AuthenticationRequestMatcher requestMatcher = new AuthenticationRequestMatcher();

    @Autowired
    private AuthenticationService authenticationService;

    public void addApi(AuthenticationApiHolder holder) {
        requestMatcher.addApi(holder);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        try {
            AuthenticationApiHolder holder = this.requestMatcher.getMatchedPathApi(request);
            ICredential credential = holder.getCredential(request);
            IAuthenticationApi api = holder.getApi();
            IAuthentication<JwtToken> authentication = authenticationService.authenticate(credential, api::doAuthenticate);

            log.debug("Authentication for {} result: {}", credential, JsonUtil.toJson(authentication));

            if (!authentication.valid()) {
                throw new AuthenticationFailedException(authentication.status());
            }

            return this.getAuthenticationManager().authenticate(authentication);
        } catch (ProcessBizException e) {
            throw new AuthenticationFailedException(new AuthenticationStatus(e.getCode().getCode(), e.getError().getMessage()));
        } catch (AuthenticationFailedException e) {
            throw e;
        } catch (Throwable e) {
            log.error("Access remote service error.", e);
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    @Override
    public void afterPropertiesSet() {
        this.setRequiresAuthenticationRequestMatcher(this.requestMatcher);
    }
}
