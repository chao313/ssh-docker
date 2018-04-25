package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.*;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.JwtAuthentication;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ProcessBizException;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.RestRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ClassUtil.isArrayField;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ClassUtil.isListField;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConvertUtil.*;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConvertUtil.ARRAY_TRANSFORM;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConvertUtil.BASE_TYPES;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConvertUtil.BASE_TYPE_TRANSFORM;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConvertUtil.LIST_TRANSFORM;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.MapUtil.$;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.util
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 16/11/3     melvin                 Created
 */
public class RequestUtil {

    private static final Logger log = LoggerFactory.getLogger(RequestUtil.class);

    private static final Map<String, BiFunctionE<String, HttpServletRequest, Object, ProcessBizException>> CONTEXT_PARAMS = $(
            "remote_ip", (name, request) -> HttpUtil.getRemoteIp(request),
            "authentication", (name, request) -> HttpUtil.getAuthentication(),
            "token", (name, request) -> HttpUtil.getToken(request),
            "access_principal", (name, request) -> getPrincipal(true),
            "principal", (name, request) -> getPrincipal(false),
            "subject", (name, request) -> {
                Object subject = request.getAttribute("subject");
                return subject == null ? getSubject() : subject;
            },
            "header_fetcher", (name, request) -> new Function<String, String>() {
                @Override
                public String apply(String name) {
                    return request == null ? null : request.getHeader(name);
                }
            }
    );

    private static Object getPrincipal(boolean access) throws ProcessBizException {
        JwtAuthentication authentication = HttpUtil.getAuthentication();
        if (authentication == null || authentication.isAccess() != access) {
            throw new ProcessBizException(ErrorCode.AuthenticationError.INVALID_TOKEN);
        }
        return authentication.getPrincipal();
    }

    private static Object getSubject() throws ProcessBizException {
        JwtAuthentication authentication = HttpUtil.getAuthentication();
        if (authentication == null || authentication.isAccess()) {
            throw new ProcessBizException(ErrorCode.AuthenticationError.INVALID_TOKEN);
        }
        return authentication.getDetails();
    }

    public static Object getContextParam(String name, HttpServletRequest request, boolean required) throws ProcessBizException {
        BiFunctionE<String, HttpServletRequest, Object, ProcessBizException> function = CONTEXT_PARAMS.get(name);
        try {
            return function == null ? null : function.apply(name, request);
        } catch (ProcessBizException e) {
            if (required) {
                throw e;
            }
            return null;
        }
    }

    public static String getPathVariable(NativeWebRequest webRequest, String paramName) {
        @SuppressWarnings("unchecked")
        Map<String, String> uriTemplateVars =
                (Map<String, String>) webRequest.getAttribute(
                        HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        return uriTemplateVars.get(paramName);
    }

    public static void bindValues(Object bean, HttpServletRequest request) throws ProcessBizException {
        bindValues(bean, new ServletWebRequest(request));
    }

    public static void bindValues(Object bean, NativeWebRequest webRequest) throws ProcessBizException {
        Class<?> clazz = bean.getClass();
        if (clazz == Object.class) {
            return;
        }

        bindFieldValue(bean, clazz, clazz.getDeclaredFields(), webRequest);
    }

    private static void bindFieldValue(Object bean, Class<?> clazz, Field[] fields, NativeWebRequest webRequest) throws ProcessBizException {
        for (Field field : fields) {
            Type type = field.getType();
            if (Collection.class.isAssignableFrom(field.getType())) {
                type = getGenericType(field);
            }
            boolean baseType = BASE_TYPES.contains(type);
            boolean multipartFile = type == MultipartFile.class;
            ModelAttribute modelAttribute = field.getAnnotation(ModelAttribute.class);
            if (!baseType && modelAttribute != null) {
                Object nestBean = ClassUtil.newInstance(field.getType());
                bindValues(nestBean, webRequest);
                continue;
            }

            BeanParam beanParam = field.getAnnotation(BeanParam.class);
            if (beanParam != null) {
                Object value = ClassUtil.newInstance(field.getType());
                bindValues(value, webRequest);
                setValue(bean, field, value);
                continue;
            }

            ContextParam contextParam = field.getAnnotation(ContextParam.class);
            boolean isContextParam = contextParam != null;

            if (!baseType && !field.getType().isEnum() && !isCollectionInParam(field) && !multipartFile && !isContextParam) {
                continue;
            }

            PathParam pathParam = field.getAnnotation(PathParam.class);
            if (pathParam != null) {
                setPathVariable(field, pathParam, bean, webRequest);
                continue;
            }
            HeaderParam headerParam = field.getAnnotation(HeaderParam.class);
            if (headerParam != null) {
                setRequestHeader(field, headerParam, bean, webRequest);
                continue;
            }
            MultiNameHeaderParam multiNameHeaderParam = field.getAnnotation(MultiNameHeaderParam.class);
            if (multiNameHeaderParam != null) {
                setRequestHeader(field, multiNameHeaderParam, bean, webRequest);
                continue;
            }
            FormParam formParam = field.getAnnotation(FormParam.class);
            if (formParam != null) {
                setFormParam(field, formParam, bean, webRequest);
                continue;
            }
            QueryParam queryParam = field.getAnnotation(QueryParam.class);
            if (queryParam != null) {
                setQueryParam(field, queryParam, bean, webRequest);
                continue;
            }

            if (isContextParam) {
                setContextParam(field, contextParam, bean, webRequest);
                continue;
            }

            String paramName = field.getName();
            HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
            String value = request.getParameter(paramName);
            setValue(bean, field, value);
        }

        Class<?> superClass = clazz.getSuperclass();
        if (superClass == Object.class || superClass == RestRequest.class) {
            return;
        }
        bindFieldValue(bean, superClass, superClass.getDeclaredFields(), webRequest);
    }

    private static void setContextParam(Field field, ContextParam contextParam, Object bean, NativeWebRequest webRequest) throws ProcessBizException {
        String paramName = contextParam.value();
        if (StringUtils.isBlank(paramName)) {
            paramName = field.getName();
        }
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Object value = getContextParam(paramName, request, contextParam.required());
        setValue(bean, field, value);
    }

    private static void setFormParam(Field field, FormParam formParam, Object bean, NativeWebRequest webRequest) {
        String paramName = formParam.value();
        if (StringUtils.isBlank(paramName)) {
            paramName = field.getName();
        }
        if (field.getType() == MultipartFile.class) {
            setMultipartParam(field, paramName, bean, webRequest);
        } else {
            setRequestParam(field, paramName, bean, webRequest);
        }
    }

    private static void setQueryParam(Field field, QueryParam queryParam, Object bean, NativeWebRequest webRequest) {
        String paramName = queryParam.value();
        if (StringUtils.isBlank(paramName)) {
            paramName = field.getName();
        }
        setRequestParam(field, paramName, bean, webRequest);
    }

    private static void setMultipartParam(Field field, String paramName, Object bean, NativeWebRequest webRequest) {
        RestMultiPartParameter multiPartParameter = new RestMultiPartParameter(paramName);
        Object value = multiPartParameter.getValue(webRequest);
        setValue(bean, field, value);
    }

    private static void setRequestParam(Field field, String paramName, Object bean, NativeWebRequest webRequest) {
        boolean array = (isArrayField(field) || isListField(field));
        RestParameter restParameter = array ? new RestArrayParameter(paramName) : new RestRequestParameter(paramName);
        Object value = restParameter.getValue(webRequest);
//        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
//        Object value = (isArray(field) || isList(field)) ? getArray(request, paramName) : request.getParameter(paramName);
        setValue(bean, field, value);
    }

    private static void setRequestHeader(Field field, HeaderParam headerParam, Object bean, NativeWebRequest webRequest) {
        String paramName = headerParam.value();
        if (StringUtils.isBlank(paramName)) {
            paramName = field.getName();
        }

        Object value = new RestHeaderParameter(paramName).getValue(webRequest);
        setValue(bean, field, value);
    }

    private static void setRequestHeader(Field field, MultiNameHeaderParam headerParam, Object bean, NativeWebRequest webRequest) {
        String paramName = headerParam.value();
        if (StringUtils.isBlank(paramName)) {
            paramName = field.getName();
        }

        Object value = new RestHeaderParameter(paramName, headerParam.alias()).getValue(webRequest);
        setValue(bean, field, value);
    }

    private static void setPathVariable(Field field, PathParam pathParam, Object bean, NativeWebRequest webRequest) {
        String paramName = pathParam.value();
        if (StringUtils.isBlank(paramName)) {
            paramName = field.getName();
        }

        String value = getPathVariable(webRequest, paramName);
        setValue(bean, field, value);
    }

    @SuppressWarnings("unchecked")
    private static void setValue(Object bean, Field field, Object value) {
        try {
            Type genericType = isListField(field) ? getGenericType(field) : null;
            value = convert(value, field.getType(), genericType);

            if (value != null) {
                PropertyUtils.setProperty(bean, field.getName(), value);
            }
        } catch (Exception e) {
            log.error("Set value to field {} failed, value: {}", field.getName(), value);
        }
    }

    private static Type getGenericType(Field field) {
        Type type = null;
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            Type[] actualTypes = ParameterizedType.class.cast(genericType).getActualTypeArguments();
            Type actualType = actualTypes == null || actualTypes.length == 0 ? null : actualTypes[0];
            if (actualType != null) {
                type = actualType;
            }
        }
        return type;
    }

//    private static String[] getArray(HttpServletRequest request, String paramName) {
//        String[] values = request.getParameterValues(paramName);
//        if (values == null || values.length == 0) {
//            values = request.getParameterValues(paramName.concat("[]"));
//        }
//        return values;
//    }

    private static boolean isCollectionInParam(Field field) {
        return Collection.class.isAssignableFrom(field.getType()) &&
                (field.isAnnotationPresent(QueryParam.class) || field.isAnnotationPresent(FormParam.class));
    }

    public static Object convert(Object value, Class<?> type, Type genericType) {
        if (value == null) {
            return null;
        }
        if (String.class.isInstance(value) && type == String.class) {
            value = String.valueOf(value).trim();
        } else if (type.isEnum()) {
            //noinspection unchecked
            value = Enum.valueOf((Class<Enum>) type, value.toString());
        } else if (type.isArray()) {
            String[] values = String[].class.cast(value);
            value = ARRAY_TRANSFORM.entrySet().stream().
                    filter(entry -> entry.getKey().test(type)).
                    map(entry -> entry.getValue().apply(values)).
                    findFirst().orElse(null);
        } else if (List.class.isAssignableFrom(type)) {
            String[] values = String[].class.cast(value);
            value = LIST_TRANSFORM.entrySet().stream().
                    filter(entry -> entry.getKey().test((Class<?>) genericType)).
                    map(entry -> entry.getValue().apply(values)).
                    findFirst().orElse(null);
        } else if (String.class.isInstance(value)) {
            String text = value.toString();
            value = BASE_TYPE_TRANSFORM.entrySet().stream().
                    filter(entry -> entry.getKey().test(type)).
                    map(entry -> entry.getValue().apply(text)).
                    findFirst().orElse(null);
        }
        return value;
    }
}
