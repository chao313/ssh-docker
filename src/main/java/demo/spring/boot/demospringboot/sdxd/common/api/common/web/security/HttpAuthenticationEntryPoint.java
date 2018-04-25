package demo.spring.boot.demospringboot.sdxd.common.api.common.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.graphql.GraphQLResult;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.RestContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ResponseUtil.authenticationException;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ResponseUtil.failure;

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
public class HttpAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    @Autowired
    HttpAuthenticationEntryPoint(MappingJackson2HttpMessageConverter jackson2HttpMessageConverter) {
        this.mapper = jackson2HttpMessageConverter.getObjectMapper();
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String path = String.format("%s:%s", request.getPathInfo(), request.getMethod());
        if (path. equals(RestContext.getGraphqlApi())) {
            GraphQLResult graphQLResult = new GraphQLResult(authException);
            failure(response, mapper, graphQLResult);
            return;
        }
        authenticationException(mapper, response, authException);
    }
}