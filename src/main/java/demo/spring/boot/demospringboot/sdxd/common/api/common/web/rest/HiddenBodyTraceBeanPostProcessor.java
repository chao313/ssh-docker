package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace.Logging;

import org.jooq.lambda.tuple.Tuple2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.RestUtil.*;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.RestUtil.PATH_CONCAT;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.RestUtil.notApiMethod;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.RestUtil.productMethod;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.RestUtil.productPath;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/2/4     melvin                 Created
 */
public class HiddenBodyTraceBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    private PackageURLRequestMappingHandlerMapping mapping;

    public PackageURLRequestMappingHandlerMapping getMapping() {
        return mapping;
    }

    public void setMapping(PackageURLRequestMappingHandlerMapping mapping) {
        this.mapping = mapping;
    }

    public String getHideBodyMethod() {
        return RestContext.getHideBodyMethod();
    }

    public void setHideBodyMethod(String hideBodyMethod) {
        RestContext.setHideBodyMethod(hideBodyMethod);
    }

    @Override
    public boolean postProcessAfterInstantiation(final Object bean, String beanName) throws BeansException {
        boolean result = super.postProcessAfterInstantiation(bean, beanName);
        Class<?> beanType = bean.getClass();
        boolean matchPackageBase = mapping.isMatchPackageBase(beanType);
        if (!matchPackageBase) {
            return result;
        }
        boolean isController = beanType.isAnnotationPresent(Controller.class);
        if(!isController) {
            return result;
        }

//        HideBody typeHideBody = beanType.getDeclaredAnnotation(HideBody.class);
//        boolean hideBodyOnType = typeHideBody != null;
        Logging typeLogging = beanType.getDeclaredAnnotation(Logging.class);
        boolean loggingOnType = typeLogging != null;

        String prefix = mapping.getPrefix(beanType);
        RequestMapping baseMapping = beanType.getDeclaredAnnotation(RequestMapping.class);
        String[] basePath = baseMapping != null ? baseMapping.value() : new String[]{""};
        Method[] methods = beanType.getDeclaredMethods();
        Stream.of(methods).
                filter(method -> !notApiMethod(method)).
                filter(method -> method.isAnnotationPresent(RequestMapping.class)).
//                filter(method -> hideBodyOnType || method.isAnnotationPresent(HideBody.class)).
                filter(method -> loggingOnType || method.isAnnotationPresent(Logging.class)).
                forEach(method -> {
//                    HideBody methodHideBody = method.getDeclaredAnnotation(HideBody.class);
//                    HideBody hideBody = methodHideBody != null ? methodHideBody : typeHideBody;

                    Logging methodLogging = method.getDeclaredAnnotation(Logging.class);
                    Logging logging = methodLogging != null ? methodLogging : typeLogging;

                    RequestMapping mapping = method.getDeclaredAnnotation(RequestMapping.class);
                    String[] methodPath = mapping.value();
                    RequestMethod[] httpMethods = mapping.method();
                    String[] path = productPath(basePath, methodPath);
                    List<Tuple2<String, String>> fullPath = productMethod(path, Stream.of(httpMethods).map(Enum::name).toArray(String[]::new));

//                    addHiddenBodyApi(prefix, fullPath, hideBody);
                    addLoggingApi(prefix, fullPath, logging);
                });

        return result;
    }

//    private void addHiddenBodyApi(String prefix, List<Tuple2<String, String>> fullPath, HideBody hideBody) {
//        fullPath.forEach(path -> {
//            boolean hidingMethod = StringUtils.isNotBlank(getHideBodyMethod()) && getHideBodyMethod().equals(path.v2());
//            boolean hideAll = hideBody.all() || hidingMethod;
//            RestContext.addHiddenBodyApi(
//                    PATH_CONCAT.apply(prefix, path.v1()),
//                    hideBody.request(), hideBody.response(), hideAll);
//        });
//    }

    private void addLoggingApi(String prefix, List<Tuple2<String, String>> fullPath, Logging logging) {
        fullPath.forEach(path -> {
            RestContext.addLoggingApi(
                    PATH_CONCAT.apply(prefix, path.v1()),
                    logging.hideRequestBody(), logging.hideResponseBody());
        });
    }
}
