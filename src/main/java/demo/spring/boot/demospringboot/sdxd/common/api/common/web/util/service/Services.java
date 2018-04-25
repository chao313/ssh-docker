package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.RestContext;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.dubbo.ParameterServiceSupplier;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ProcessBizException;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConstraintUtil.getValidationException;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConstraintUtil.isConstraintViolationException;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.Throwables.getProcessBizException;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.service.SupplierProcessor.processor;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode.SystemError.SERVER_INTERNAL_ERROR;

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
public class Services<T> {

    public static <T> Services<T> of(T bean) {
        return new Services<>(bean, true);
    }

    public static <T> Services<T> of(T bean, boolean allCorrectReturn) {
        return new Services<>(bean, allCorrectReturn);
    }

    private ExecutorService executorService =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private T bean;
    private boolean allCorrectReturn;
    private List<SupplierProcessor<T, ?>> processors = Lists.newArrayList();
    private int processorCount;

    private List<ProcessBizException> exceptions = Lists.newArrayList();

    private Services(T bean, boolean allCorrectReturn) {
        this.bean = bean;
        this.allCorrectReturn = allCorrectReturn;
    }

    public <R> Services<T> parallel(
            ParameterServiceSupplier<T, R> supplier, BiConsumer<T, R> setter) {
        return parallel(supplier, setter, null);
    }

    public <R, N> Services<T> parallel(
            ParameterServiceSupplier<T, R> supplier, BiConsumer<T, R> setter, SupplierProcessor<T, N> next) {
        SupplierProcessor<T, R> processor = processor(supplier, setter, next);
        this.processors.add(processor);
        this.processorCount += processor.depth();
        return this;
    }

    public T serve() throws ProcessBizException {
        CountDownLatch latch = new CountDownLatch(this.processorCount);
        for (SupplierProcessor<T, ?> processor : processors) {
            processor.setContextId(RestContext.getContextId());
            //noinspection unchecked
            SupplierProcessor<T, Object> p = (SupplierProcessor<T, Object>) processor;
            function(latch).accept(p, bean);
        }
        try {
            latch.await();
        } catch (InterruptedException ignored) {
        }
        executorService.shutdownNow();
        if (exceptions.size() > 0 && allCorrectReturn) {
            throw exceptions.get(0);
        }
        return bean;
    }

    private <N> BiConsumer<SupplierProcessor<T, N>, T> function(CountDownLatch latch) {
        return (processor, bean) -> {
            executorService.submit(() -> {
                try {
                    processor.process(bean, function(latch));
                } catch (Exception e) {
                    onException(e);
                }
                latch.countDown();
            });
        };
    }

    private void onException(Exception e) {
        if (isConstraintViolationException(e)) {
            exceptions.add(getValidationException(e));
        } else {
            exceptions.add(getProcessBizException(e));
        }
    }

    public static void main(String[] args) {
        p();
//        p();
    }

    private static void p() {
        try {
            Services.of(Maps.newHashMap()).
                    parallel(param -> proc(1), (value, o) -> {}).
                    parallel(param -> proc(2, 3000, true), (value, o) -> {}).
                    parallel(param -> proc(3), (value, o) -> {},
                            processor(param -> proc(6, 10000), (value, o) -> {},
                                    processor(param -> proc(8, 5000), (value, o) -> {}))).
                    parallel(param -> proc(4), (value, o) -> {}).
                    parallel(param -> proc(5), (value, o) -> {}, processor(param -> proc(7, 20000), (value, o) -> {})).
                    serve();
        } catch (ProcessBizException e) {
            System.out.println(e);
        }
        System.out.println(new Date() + " complete all");
    }

    private static int proc(int i) throws ProcessBizException {
        return proc(i, 3000, false);
    }

    private static int proc(int i, long millis) throws ProcessBizException {
        return proc(i, millis, false);
    }

    private static int proc(int i, long millis, boolean ex) throws ProcessBizException {
        System.out.println(new Date() + " start: " + i);
        try {
            Thread.sleep(millis);
            if (ex) {
                throw new ProcessBizException(SERVER_INTERNAL_ERROR);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(new Date() + " complete: " + i);
        }
        return i;
    }
}
