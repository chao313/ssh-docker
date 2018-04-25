package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.validation.Valid;
import javax.ws.rs.BeanParam;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ClassUtil.newInstance;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.RequestUtil.bindValues;

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
public class BeanParamHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger log = LoggerFactory.getLogger(BeanParamHandlerMethodArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(BeanParam.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        Class<?> type = parameter.getParameterType();
        Object bean = newInstance(type);
        if (bean == null) {
            return null;
        }
        bindValues(bean, webRequest);

        WebDataBinder binder = binderFactory.createBinder(webRequest, bean, parameter.getParameterName());
        if (binder != null) {
            Valid methodValid = parameter.getMethodAnnotation(Valid.class);
            Valid parameterValid = parameter.getParameterAnnotation(Valid.class);
            if (methodValid != null || parameterValid != null) {
                binder.validate();
                if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
                    throw new BindException(binder.getBindingResult());
                }
            }
        }
        return bean;
    }

    protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter methodParam) {
        int i = methodParam.getParameterIndex();
        Class<?>[] paramTypes = methodParam.getMethod().getParameterTypes();
        boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
        return !hasBindingResult;
    }
}
