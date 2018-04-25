package demo.spring.boot.demospringboot.sdxd.common.api.common.web.access;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.RestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
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
public class ThrottlingRuleBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    private static final Logger log = LoggerFactory.getLogger(ThrottlingRuleBeanPostProcessor.class);

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> clazz = bean.getClass();
        if (!clazz.isAnnotationPresent(Service.class)) {
            return bean;
        }
        Method[] methods = clazz.getDeclaredMethods();
        Stream.of(methods).
                filter(method -> Modifier.isPublic(method.getModifiers())).
                filter(method -> !Modifier.isAbstract(method.getModifiers())).
                filter(method -> !Modifier.isStatic(method.getModifiers())).
                filter(method -> method.isAnnotationPresent(ThrottlingRule.class)).
                forEach(method -> {
                    Callable<Boolean> callable = () -> {
                        Object value = method.invoke(bean);
                        return Boolean.class.cast(value);
                    };
                    Predicate<?> predicate = t -> {
                        try {
                            return callable.call();
                        } catch (Exception e) {
                            log.error("Invoke throttling rule error.", e);
                            return false;
                        }
                    };
                    ThrottlingRule rule = method.getDeclaredAnnotation(ThrottlingRule.class);
                    RestContext.addRule(rule.name(), predicate, rule.error());
                    log.debug("Add throttling rule: {}", rule.name());
                });

        return bean;
    }
}
