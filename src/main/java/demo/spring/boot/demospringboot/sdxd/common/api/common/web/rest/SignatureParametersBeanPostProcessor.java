package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest;

import com.google.common.collect.Sets;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.graphql.GraphQLAPI;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.RestRequest;

import org.apache.commons.lang3.StringUtils;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ClassUtil.*;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ClassUtil.isArrayField;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ClassUtil.isArrayParameter;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ClassUtil.isListField;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ClassUtil.isListParameter;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.RestUtil.*;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.RestUtil.PATH_CONCAT;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.RestUtil.notApiMethod;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.RestUtil.productMethod;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.RestUtil.productPath;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;

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
 * 17/1/22     melvin                 Created
 */
public class SignatureParametersBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    private PackageURLRequestMappingHandlerMapping mapping;

    public PackageURLRequestMappingHandlerMapping getMapping() {
        return mapping;
    }

    public void setMapping(PackageURLRequestMappingHandlerMapping mapping) {
        this.mapping = mapping;
        RestContext.setMapping(mapping);
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
        boolean isIgnore = beanType.isAnnotationPresent(SignatureIgnore.class);
        if (isIgnore) {
            return result;
        }

        String prefix = mapping.getPrefix(beanType);
        RequestMapping baseMapping = beanType.getDeclaredAnnotation(RequestMapping.class);
        String[] basePath = baseMapping != null ? baseMapping.value() : new String[]{""};
        Method[] methods = beanType.getDeclaredMethods();
        Stream.of(methods).
                filter(method -> !notApiMethod(method)).
                filter(method -> !method.isAnnotationPresent(SignatureIgnore.class) || method.isAnnotationPresent(GraphQLAPI.class)).
                filter(method -> method.isAnnotationPresent(RequestMapping.class)).
                forEach(method -> {
                    RequestMapping mapping = method.getDeclaredAnnotation(RequestMapping.class);
                    String[] methodPath = mapping.value();
                    RequestMethod[] httpMethods = mapping.method();
                    String[] path = productPath(basePath, methodPath);
                    List<Tuple2<String, String>> fullPath = productMethod(path, Stream.of(httpMethods).map(Enum::name).toArray(String[]::new));

                    GraphQLAPI graphQLAPI = method.getDeclaredAnnotation(GraphQLAPI.class);
                    if (graphQLAPI != null) {
                        fullPath.forEach(p -> RestContext.setGraphqlApi(PATH_CONCAT.apply(prefix, p.v1())));
                        return;
                    }

                    Set<RestParameter> parameterNames = Sets.newHashSet();
                    Parameter[] parameters = method.getParameters();
                    fetchNormalFieldName(parameters, parameterNames);
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    fetchFieldsName(parameterTypes, parameterNames);
                    addParameters(prefix, fullPath, parameterNames);
                });
        return result;
    }

    private void addParameters(String prefix, List<Tuple2<String, String>> fullPath, Set<RestParameter> parameterNames) {
        fullPath.forEach(path -> RestContext.addParameters(PATH_CONCAT.apply(prefix, path.v1()), parameterNames));
    }

    private void fetchNormalFieldName(Parameter[] parameters, Set<RestParameter> parameterNames) {
        Stream.of(parameters).
                filter(parameter ->
                        parameter.isAnnotationPresent(RequestParam.class) ||
                                parameter.isAnnotationPresent(PathVariable.class) ||
                                parameter.isAnnotationPresent(RequestBody.class)).
                forEach(parameter -> {
                    RequestParam param = parameter.getDeclaredAnnotation(RequestParam.class);
                    if (param != null) {
                        String name = StringUtils.isBlank(param.value()) ? param.name() : param.value();
                        boolean array = (isArrayParameter(parameter) || isListParameter(parameter));
                        parameterNames.add(array ? new RestArrayParameter(name) : new RestRequestParameter(name));
                    }
                    PathVariable pathVariable = parameter.getDeclaredAnnotation(PathVariable.class);
                    if (pathVariable != null) {
                        String name = StringUtils.isBlank(pathVariable.value()) ? pathVariable.name() : pathVariable.value();
                        parameterNames.add(new RestPathParameter(name));
                    }
                    RequestBody requestBody = parameter.getDeclaredAnnotation(RequestBody.class);
                    if (requestBody != null) {
                        parameterNames.add(new RestBodyParameter());
                    }
                });
    }

    private void fetchFieldsName(Class<?>[] parameterTypes, Set<RestParameter> parameterNames) {
        for (Class<?> parameterType : parameterTypes) {
            if (!RestRequest.class.isAssignableFrom(parameterType)) {
                continue;
            }
            fetchFieldsName(parameterType, parameterNames);
        }
    }

    private void fetchFieldsName(Class<?> type, Set<RestParameter> parameterNames) {
        if (type == RestRequest.class) {
            return;
        }
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            if (isStatic(field.getModifiers()) || isPublic(field.getModifiers())) {
                continue;
            }
            if (!field.isAnnotationPresent(QueryParam.class) &&
                    !field.isAnnotationPresent(FormParam.class) &&
                    !field.isAnnotationPresent(BeanParam.class)) {
                continue;
            }

            BeanParam beanParam = field.getDeclaredAnnotation(BeanParam.class);
            if (beanParam != null) {
                fetchFieldsName(field.getType(), parameterNames);
                continue;
            }

            QueryParam queryParam = field.getDeclaredAnnotation(QueryParam.class);
            if (queryParam != null) {
                String name = queryParam.value();
                boolean array = (isArrayField(field) || isListField(field));
                parameterNames.add(array ? new RestArrayParameter(name) : new RestRequestParameter(name));
                continue;
            }
            FormParam formParam = field.getDeclaredAnnotation(FormParam.class);
            if (formParam != null) {
                String name = formParam.value();
                boolean array = (isArrayField(field) || isListField(field));
                parameterNames.add(array ? new RestArrayParameter(name) : new RestRequestParameter(name));
            }
        }

        Class<?> superClass = type.getSuperclass();
        fetchFieldsName(superClass, parameterNames);
    }
}
