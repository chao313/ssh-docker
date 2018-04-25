package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Type;
import java.util.List;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.RequestUtil.convert;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.RequestUtil.getContextParam;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 16/10/31     melvin                 Created
 */
public class ContextParamHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger log = LoggerFactory.getLogger(ContextParamHandlerMethodArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ContextParam.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        ContextParam contextParam = parameter.getParameterAnnotation(ContextParam.class);
        String name = contextParam.value();
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Object value = getContextParam(name, request, contextParam.required());
        Class<?> type = parameter.getParameterType();
        Type genericType = List.class.isAssignableFrom(type) ? parameter.getGenericParameterType() : null;
        value = convert(value, type, genericType);
        return value;
    }
}
