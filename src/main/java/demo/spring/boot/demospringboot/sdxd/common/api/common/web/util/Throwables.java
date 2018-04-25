package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ProcessBizException;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.RestResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConstraintUtil.isConstraintViolationException;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConstraintUtil.toValidationError;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode.SystemError.SERVER_INTERNAL_ERROR;

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
 * 16/11/18     melvin                 Created
 */
public class Throwables {

    private static final Logger log = LoggerFactory.getLogger(Throwables.class);

    public interface ExceptionWrapper<E extends Throwable> {
        E wrap(Exception e);
    }

    public static <T>T propagate(Callable<T> callable) throws RuntimeException {
        return propagate(callable, RuntimeException::new);
    }

    public static <T, E extends Throwable> T propagate(Callable<T> callable, ExceptionWrapper<E> wrapper) throws E {
        try {
            return callable.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw wrapper.wrap(e);
        }
    }

    public static <T extends Throwable> T getCause(Throwable t, Class<T> causeType) {
        if (t.getClass().isInstance(NullPointerException.class)) {
            return null;
        }
        Throwable cause = t.getCause();
        if (cause == null) {
            return null;
        }
        if (causeType.isInstance(cause)) {
            return causeType.cast(cause);
        }
        return getCause(cause, causeType);
    }

    public static ProcessBizException getProcessBizException(Exception e) {
        ProcessBizException processBizException = getCause(e, ProcessBizException.class);
        if (processBizException != null) {
            return processBizException;
        }
        log.error("Unknown error when parallel processing loading from dubbo.", e);
        return new ProcessBizException(SERVER_INTERNAL_ERROR, e.getMessage());
    }

    public static void throwProcessBizException(Exception e) throws ProcessBizException {
        throw getProcessBizException(e);
    }

    public static <T> RestResponse<T> toResponse(Exception e) {
        if (isConstraintViolationException(e)) {
            return toValidationError(e);
        }
        //noinspection ThrowableResultOfMethodCallIgnored
        ProcessBizException processBizException = getCause(e, ProcessBizException.class);
        if (processBizException != null) {
            return processBizException.toResult();
        }
        log.error("Unknown error when parallel processing loading from dubbo.", e);
        return new ProcessBizException(SERVER_INTERNAL_ERROR, e.getMessage()).toResult();
    }
}
