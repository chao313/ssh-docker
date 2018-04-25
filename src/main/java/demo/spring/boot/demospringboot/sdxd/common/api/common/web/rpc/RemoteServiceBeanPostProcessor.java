package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rpc;

import com.alibaba.dubbo.config.annotation.Reference;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.access.AccessListener;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.stream.Stream;

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
 * 16/12/19     melvin                 Created
 */
public class RemoteServiceBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    private static final Logger log = LoggerFactory.getLogger(RemoteServiceBeanPostProcessor.class);

    private String scanPackage;

    private RemoteServiceTraceInterceptor traceInterceptor;

    private RemoteServiceProxyInterceptor proxyInterceptor;

    public RemoteServiceBeanPostProcessor() {
        traceInterceptor = new RemoteServiceTraceInterceptor();
        traceInterceptor.setHideProxyClassNames(true);
        traceInterceptor.setEnterMessage("RPC>REQ#$[contextId]#$[headers]#$[principal]|$[targetClassName].$[methodName]#$[arguments]");
        traceInterceptor.setExitMessage("RPC>RES#$[contextId]#$[headers]#$[principal]#$[invocationTime]|$[targetClassName].$[methodName]#$[arguments]|$[returnValue]");
        traceInterceptor.setExceptionMessage("RPC>EX#$[contextId]#$[headers]#$[principal]|$[targetClassName].$[methodName]#$[arguments]|$[exception]");
    }

    public String getScanPackage() {
        return scanPackage;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    public void setAccessListener(AccessListener accessListener) {
        this.traceInterceptor.setAccessListener(accessListener);
    }

    public void setProxyInterceptor(RemoteServiceProxyInterceptor proxyInterceptor) {
        this.proxyInterceptor = proxyInterceptor;
    }

    public RemoteServiceProxyInterceptor getProxyInterceptor() {
        return proxyInterceptor;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> clazz = bean.getClass();
        if (StringUtils.isNotBlank(getScanPackage())) {
            Package pack = clazz.getPackage();
            if (pack == null || pack.getName() == null || !pack.getName().startsWith(getScanPackage())) {
                return bean;
            }
        }
        Field[] fields = clazz.getDeclaredFields();
        Stream.of(fields).
                filter(field -> field.isAnnotationPresent(Reference.class)).
                map(field -> {
                    field.setAccessible(true);
                    return field;
                }).
                forEach(field -> {
                    try {
                        Object value = field.get(bean);
//                        if (value == null) {
//                            throw new NullArgumentException(field.getName());
//                        }
                        if (value == null) {
                            value = proxy(field.getType());
                        }
                        if (value != null) {
                            ProxyFactory proxyFactory = new ProxyFactory(value);
                            proxyFactory.addAdvice(traceInterceptor);
                            if (proxyInterceptor != null) {
                                proxyFactory.addAdvice(proxyInterceptor);
                            }
                            Object proxiedObject = proxyFactory.getProxy();
                            field.set(bean, proxiedObject);
                        }
                    } catch (NullArgumentException e) {
                        throw new ApplicationContextException("", e);
                    } catch (Exception e) {
                        log.error("Set proxied object failed", e);
                    }
                });

        return bean;
    }

    private static Object proxy(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        boolean requiredProxy = Stream.of(methods).
                filter(method -> Modifier.isPublic(method.getModifiers())).
                filter(method -> !Modifier.isStatic(method.getModifiers())).
                anyMatch(method -> method.isAnnotationPresent(RequestMapping.class));
        if (requiredProxy) {
            log.debug("Class: {} required proxy", clazz.getName());
            return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> null);
        }
        return null;
    }
}
