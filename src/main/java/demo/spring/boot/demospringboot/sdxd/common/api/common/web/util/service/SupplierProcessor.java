package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.service;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.RestContext;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.dubbo.ParameterServiceSupplier;

import java.util.function.BiConsumer;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.Throwables.propagate;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.service
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/2/16     melvin                 Created
 */
public class SupplierProcessor<T, R> {

    public static <T, R> SupplierProcessor<T, R> processor(ParameterServiceSupplier<T, R> supplier, BiConsumer<T, R> setter) {
        return processor(supplier, setter, null);
    }

    public static <T, R, N> SupplierProcessor<T, R> processor(ParameterServiceSupplier<T, R> supplier, BiConsumer<T, R> setter, SupplierProcessor<T, N> next) {
        return new SupplierProcessor<>(supplier, setter, next);
    }

    private ParameterServiceSupplier<T, ?> supplier;
    private BiConsumer<T, R> setter;

    private SupplierProcessor<T, ?> next;

    private String contextId;

    private SupplierProcessor(ParameterServiceSupplier<T, R> supplier, BiConsumer<T, R> setter, SupplierProcessor<T, ?> next) {
        this.supplier = supplier;
        this.setter = setter;
        this.next = next;
    }

    public <N> void process(T bean, BiConsumer<SupplierProcessor<T, N>, T> consumer) {
        RestContext.setContextId(contextId, null);
        R result = (R) propagate(() -> supplier.get(bean));
        if (result != null) {
            setter.accept(bean, result);
        }
        if (next != null) {
            //noinspection unchecked
            SupplierProcessor<T, N> n = (SupplierProcessor<T, N>) next;
            consumer.accept(n, bean);
        }
    }

    int depth() {
        int depth = 1;
        if (next == null) {
            return depth;
        }
        return depth + next.depth();
    }

    void setContextId(String contextId) {
        this.contextId = contextId;
        if (next != null) {
            next.setContextId(contextId);
        }
    }
}
