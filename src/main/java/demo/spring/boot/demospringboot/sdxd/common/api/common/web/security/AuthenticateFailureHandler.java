package demo.spring.boot.demospringboot.sdxd.common.api.common.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ResponseUtil.authenticationException;

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
 * 16/10/26     melvin                 Created
 */
@Component
public class AuthenticateFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper mapper;

    @Autowired
    AuthenticateFailureHandler(MappingJackson2HttpMessageConverter jackson2HttpMessageConverter) {
        this.mapper = jackson2HttpMessageConverter.getObjectMapper();
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        authenticationException(mapper, response, exception);
    }
}