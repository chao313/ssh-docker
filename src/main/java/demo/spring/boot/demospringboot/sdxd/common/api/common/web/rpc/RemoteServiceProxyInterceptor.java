package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rpc;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.RestContext;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.Subject;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ApiUtil;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.JsonUtil;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.RestResponse;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.stream.Stream;

import demo.spring.boot.demospringboot.sdxd.common.pojo.dubbo.BaseRiskControlRequest;
import demo.spring.boot.demospringboot.sdxd.common.pojo.dubbo.DubboResponse;
import lombok.Setter;

import static demo.spring.boot.demospringboot.sdxd.framework.constant.Constants.System.OK;
import static demo.spring.boot.demospringboot.sdxd.framework.constant.Constants.System.SERVER_SUCCESS;
import static demo.spring.boot.demospringboot.sdxd.framework.constant.Constants.System.SERVER_SUCCESS_MSG;


@Setter
public class RemoteServiceProxyInterceptor implements MethodInterceptor {

    private String rootPath;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        RequestMapping mapping = method.getDeclaredAnnotation(RequestMapping.class);
        if (mapping == null) {
            return invocation.proceed();
        }

        String[] paths = mapping.path();

        String fullPath = paths.length > 0 ? paths[0] : "";
        if (StringUtils.isNotBlank(rootPath)) {
            String format = (fullPath.startsWith("/") || rootPath.endsWith("/")) ? "%s%s" : "%s/%s";
            fullPath = String.format(format, rootPath, fullPath);
        }

        Object[] arguments = invocation.getArguments();
        Subject subject = RestContext.getSubject();
        if (subject == null) {
            return skip();
        }
        if (arguments != null) {
            Stream.of(arguments).
                    filter(Objects::nonNull).
                    filter(BaseRiskControlRequest.class::isInstance).
                    map(BaseRiskControlRequest.class::cast).
                    forEach(request -> {
                        String merchantNo = subject.get("merchantNoInRiskControl");
                        request.setMerchantNo(StringUtils.isBlank(merchantNo) ? "SDXD" : merchantNo);
                        if (request.getUserId() == null) {
                            request.setUserId(Long.parseLong(subject.getValue()));
                        }
                        if (StringUtils.isBlank(request.getPhone())) {
                            request.setPhone(subject.get("phone"));
                            if (StringUtils.isBlank(request.getPhone())) {
                                request.setPhone("13524612345");
                            }
                        }
                        String platform = subject.get("merchantNo");
                        if (StringUtils.isBlank(request.getPlatformName())) {
                            platform = StringUtils.isBlank(platform) ? "DXD" : platform;
                            request.setPlatformName(platform);
                        }
                    });
        }
        Object argument = (arguments != null && arguments.length == 1) ? arguments[0] : arguments;
        String body = JsonUtil.toJson(argument);

        String value = ApiUtil.postJson(fullPath, null, body);

        //{"code":"1000007","error":{"message":"系统错误，请联系管理员!"}}

        Type genericReturnType = method.getGenericReturnType();
        Class<?> genericFor = null;
        Class<?> genericType = null;
        if (ParameterizedType.class.isInstance(genericReturnType)) {
            ParameterizedType parameterizedType = ParameterizedType.class.cast(genericReturnType);
            Type[] types = parameterizedType.getActualTypeArguments();
            genericFor = types.length > 0 ?
                    (Class<?>) (ParameterizedType.class.isInstance(types[0]) ?
                            ParameterizedType.class.cast(types[0]).getRawType() :
                            types[0]) :
                    null;
            for (Type type : types) {
                if (ParameterizedType.class.isInstance(type)) {
                    ParameterizedType pType = ParameterizedType.class.cast(type);
                    Type[] ppTypes = pType.getActualTypeArguments();
                    genericType = ppTypes.length > 0 ?
                            (Class<?>) (ParameterizedType.class.isInstance(ppTypes[0]) ?
                                    ParameterizedType.class.cast(ppTypes[0]).getRawType() :
                                    ppTypes[0]) :
                            null;
                }
            }
        }

        RestResponse restResponse = genericType == null ?
                JsonUtil.fromJson(value, RestResponse.class, genericFor) :
                JsonUtil.fromJson(value, RestResponse.class, genericFor, genericType);
        Class<?> returnType = method.getReturnType();
        if (DubboResponse.class != returnType) {
            return JsonUtil.fromJson(value, returnType, (Class<?>) genericReturnType);
        }
        if (DubboResponse.class == returnType && restResponse != null) {
            DubboResponse dubboResponse = new DubboResponse();
            dubboResponse.setStatus(restResponse.isSuccessful() ? OK : restResponse.getCode());
            dubboResponse.setError(restResponse.isSuccessful() ? SERVER_SUCCESS : restResponse.getCode());
            dubboResponse.setMsg(restResponse.isSuccessful() ? SERVER_SUCCESS_MSG : restResponse.getErrorMessage());
            dubboResponse.setData(restResponse.getContent());
            return dubboResponse;
        }
        return null;
    }

    private static DubboResponse skip() {
        DubboResponse dubboResponse = new DubboResponse();
        dubboResponse.setStatus(OK);
        dubboResponse.setError(SERVER_SUCCESS);
        dubboResponse.setMsg("跳过调用");
        return dubboResponse;
    }
}
