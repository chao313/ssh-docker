package demo.spring.boot.demospringboot.sdxd.common.api.common.web.security;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.token.TokenGenerator;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.token.TokenValidation;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.token.TokenValidator;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace.HttpBodyCachingRequestWrapper;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.HttpUtil;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.JwtToken;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
public class JwtTokenFilter extends GenericFilterBean {

    private TokenValidator validator;

    private AuthenticationEntryPoint entryPoint;
    private AuthenticationManager authenticationManager;

    private RequestMatcher requiresAuthenticationRequestMatcher;

    public JwtTokenFilter(
            String defaultFilterProcessesUrl,
            AuthenticationManager authenticationManager,
            AuthenticationEntryPoint entryPoint,
            TokenValidator validator) {
        this.requiresAuthenticationRequestMatcher = new AntPathRequestMatcher(defaultFilterProcessesUrl, null);
        this.authenticationManager = authenticationManager;
        this.entryPoint = entryPoint;
        this.validator = validator;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (!requiresAuthentication(httpServletRequest, httpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            IAuthentication<JwtToken> validation =
                    validator.validate(() -> tryGetTokenValidation(httpServletRequest));

            if (!validation.valid()) {
                throw new AuthenticationFailedException(validation.status());
            }

            Authentication authentication = authenticationManager.authenticate(validation);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            if (entryPoint != null) {
                entryPoint.commence(httpServletRequest, httpServletResponse, e);
            }
        }
    }

    protected boolean requiresAuthentication(HttpServletRequest request,
                                             HttpServletResponse response) {
        return requiresAuthenticationRequestMatcher.matches(request);
    }

    private TokenValidation tryGetTokenValidation(HttpServletRequest request) {
        if (!HttpBodyCachingRequestWrapper.class.isInstance(request)) {
            String token = HttpUtil.getToken(request);
            if (StringUtils.isBlank(token)) {
                throw new JwtTokenMissingException();
            }
            return TokenGenerator.isValid(validator.getTokenConfiguration(), token);
        }
        HttpBodyCachingRequestWrapper httpBodyCachingRequestWrapper = HttpBodyCachingRequestWrapper.class.cast(request);
        return httpBodyCachingRequestWrapper.getTokenValidation();
    }
}
