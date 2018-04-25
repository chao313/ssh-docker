package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.dubbo;

import com.google.common.collect.Lists;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.RestContext;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.Subject;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ProcessBizException;

import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConstraintUtil.isConstraintViolationException;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConstraintUtil.throwValidationException;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.Throwables.propagate;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.Throwables.throwProcessBizException;

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
 * 16/11/17     melvin                 Created
 */
public class DubboLoading<T> {

    private static final Logger log = LoggerFactory.getLogger(DubboLoading.class);

    public static <T> DubboLoading<T> forValue(T bean) {
        return new DubboLoading<>(bean, true);
    }

    public static <T> DubboLoading<T> forValue(T bean, boolean allCorrectReturn) {
        return new DubboLoading<>(bean, allCorrectReturn);
    }

    private T bean;
    private boolean allCorrectReturn;

    private List<Tuple2<BiConsumer<T, Object>, ServiceSupplier<Object>>> processors;
    private List<Tuple3<BiConsumer<T, Object>, Function<T, Object>, ParameterServiceSupplier<Object, Object>>> depProcessors;

    public DubboLoading(T bean, boolean allCorrectReturn) {
        this.bean = bean;
        this.allCorrectReturn = allCorrectReturn;
        this.processors = Lists.newArrayList();
        this.depProcessors = Lists.newArrayList();
    }

    public <R> DubboLoading<T> parallel(ServiceSupplier<R> supplier, BiConsumer<T, R> setter) {
        //noinspection unchecked
        processors.add(new Tuple2(setter, supplier));
        return this;
    }

    public <P, R> DubboLoading<T> dep(Function<T, P> dependency, ParameterServiceSupplier<P, R> supplier, BiConsumer<T, R> setter) {
        //noinspection unchecked
        depProcessors.add(new Tuple3(setter, dependency, supplier));
        return this;
    }

    public T start() throws ProcessBizException {
        String contextId = RestContext.getContextId();
        Date entryTime = RestContext.getRequestEntry();
        Subject subject = RestContext.getSubject();
        Tuple3<Boolean, Boolean, Boolean> hideBody = RestContext.getLogging();
        try {
            processors.parallelStream().forEach(tuple -> {
                RestContext.setContextId(contextId, entryTime);
                RestContext.setLogging(hideBody);
                RestContext.setSubject(subject);
                ServiceSupplier<Object> supplier = tuple.v2();
                try {
                    Object result = supplier.get();
                    BiConsumer<T, Object> setter = tuple.v1();
                    if (result != null) {
                        setter.accept(bean, result);
                    }
                } catch (ProcessBizException e) {
                    if (allCorrectReturn) {
                        throw new RuntimeException(e);
                    }
                }
            });
            depProcessors.parallelStream().forEach(tuple -> {
                RestContext.setContextId(contextId, entryTime);
                Function<T, Object> dep = tuple.v2();
                Object depValue = propagate(() -> dep.apply(bean));
                if (depValue != null) {
                    ParameterServiceSupplier<Object, Object> supplier = tuple.v3();
                    try {
                        Object result = supplier.get(depValue);
                        BiConsumer<T, Object> setter = tuple.v1();
                        if (result != null) {
                            setter.accept(bean, result);
                        }
                    } catch (ProcessBizException e) {
                        if (allCorrectReturn) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        } catch (RuntimeException e) {
            if (isConstraintViolationException(e)) {
                throwValidationException(e);
            }
            throwProcessBizException(e);
        }
        return bean;
    }

    public static <T> T invoke(Callable<T> callable) throws ProcessBizException {
        try {
            return callable.call();
        } catch (Exception e) {
            throwProcessBizException(e);
        }
        return null;
    }
}
