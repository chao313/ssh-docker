package demo.spring.boot.demospringboot.sdxd.common.api.common.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ResponseUtil.success;

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
public class AuthenticateSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(AuthenticateSuccessHandler.class);

    private final ObjectMapper mapper;

    @Autowired
    AuthenticateSuccessHandler(MappingJackson2HttpMessageConverter jackson2HttpMessageConverter) {
        this.mapper = jackson2HttpMessageConverter.getObjectMapper();
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);

        Object principal = authentication.getPrincipal();

        JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication;
        log.info("{} is sign in, token: {}", principal, jwtAuthentication.getToken());

        success(response, mapper, jwtAuthentication.getToken());
    }
}
