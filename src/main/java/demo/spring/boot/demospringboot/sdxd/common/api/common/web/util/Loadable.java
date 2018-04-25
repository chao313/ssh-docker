package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.RestContext;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.Subject;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ProcessBizException;

import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConstraintUtil.isConstraintViolationException;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConstraintUtil.throwValidationException;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.Throwables.throwProcessBizException;

public class Loadable<T> {

    public static <T> Loadable<T> of(T bean) {
        return of(bean, true);
    }

    public static <T> Loadable<T> of(T bean, boolean allCorrectReturn) {
        return new Loadable<>(bean, allCorrectReturn);
    }

    private T bean;
    private boolean allCorrectReturn;

    private List<Tuple2<ConsumerE<Object, ProcessBizException>, Class<?>>> processors;
    private Map<Class<?>, Object> processBeans;

    private Map<Class<?>, BiConsumer<T, Object>> joined;

    private Loadable(T bean, boolean allCorrectReturn) {
        this.bean = bean;
        this.allCorrectReturn = allCorrectReturn;

        this.processors = Lists.newArrayList();
        this.processBeans = Maps.newHashMap();
        this.joined = Maps.newHashMap();

        this.processBeans.put(bean.getClass(), bean);
    }

    public <P> Loadable<T> join(Loadable<P> loadable, BiConsumer<T, P> consumer) {
        List<Tuple2<ConsumerE<Object, ProcessBizException>, Class<?>>> list = loadable.processors;
        list.forEach(tuple -> {
            ConsumerE<Object, ProcessBizException> v1 = tuple.v1();
            Class<?> v2 = tuple.v2();
            this.processors.add(Tuple.tuple(v1, v2));
        });

        P p = loadable.bean;
        Class<?> type = p.getClass();
        processBeans.put(type, p);
        //noinspection unchecked
        joined.put(type, (BiConsumer<T, Object>) consumer);
        return this;
    }

    public Loadable<T> fetch(ConsumerE<T, ProcessBizException> processor) {
        //noinspection unchecked
        this.processors.add(Tuple.tuple((ConsumerE<Object, ProcessBizException>) processor, bean.getClass()));
        return this;
    }

    public T take() throws ProcessBizException {
        String contextId = RestContext.getContextId();
        Date entryTime = RestContext.getRequestEntry();
        Subject subject = RestContext.getSubject();
        Tuple3<Boolean, Boolean, Boolean> hideBody = RestContext.getLogging();
        try {
            processors.parallelStream().forEach(tuple -> {
                RestContext.setContextId(contextId, entryTime);
                RestContext.setLogging(hideBody);
                RestContext.setSubject(subject);
                ConsumerE<Object, ProcessBizException> processor = tuple.v1();
                Class<?> beanType = tuple.v2();
                Object bean = processBeans.get(beanType);
                try {
                    processor.accept(bean);
                } catch (ProcessBizException e) {
                    if (allCorrectReturn) {
                        throw new RuntimeException(e);
                    }
                }
            });
            joined.forEach((type, consumer) -> {
                Object p = processBeans.get(type);
                consumer.accept(bean, p);
            });
        } catch (RuntimeException e) {
            if (isConstraintViolationException(e)) {
                throwValidationException(e);
            }
            throwProcessBizException(e);
        }
        return bean;
    }
}
