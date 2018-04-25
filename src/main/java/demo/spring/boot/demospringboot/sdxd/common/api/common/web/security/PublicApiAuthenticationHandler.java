package demo.spring.boot.demospringboot.sdxd.common.api.common.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.interceptor.PreHandler;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.token.TokenValidator;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.HttpUtil;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.JwtToken;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ResponseUtil.failure;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.RestResponse.fail;

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
 * 17/1/3     melvin                 Created
 */
public class PublicApiAuthenticationHandler implements PreHandler {

    private TokenValidator validator;
    private ObjectMapper mapper;

    public PublicApiAuthenticationHandler(
            TokenValidator validator, MappingJackson2HttpMessageConverter jackson2HttpMessageConverter) {
        this.validator = validator;
        this.mapper = jackson2HttpMessageConverter.getObjectMapper();
    }

    @Override
    public boolean handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!HandlerMethod.class.isInstance(handler)) {
            return true;
        }
        HandlerMethod handlerMethod = HandlerMethod.class.cast(handler);
        Package pack = handlerMethod.getBeanType().getPackage();
        if (!pack.getName().contains(".pub.")) {
            return true;
        }
        String token = HttpUtil.getToken(request);
        if (StringUtils.isBlank(token)) {
            SecurityContextHolder.clearContext();
            return true;
        }
        try {
            IAuthentication<JwtToken> validation = validator.validate(token);

            if (!validation.valid()) {
                SecurityContextHolder.clearContext();
                return true;
//                failure(response, mapper, fail(INVALID_TOKEN));
//                return false;
            }

            SecurityContextHolder.getContext().setAuthentication(validation);
        } catch (InvalidTokenException e) {
            failure(response, mapper, fail(e.getErrorCode()));
            return false;
        }

        return true;
    }
}
