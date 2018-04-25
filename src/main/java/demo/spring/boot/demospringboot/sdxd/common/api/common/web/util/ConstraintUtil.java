package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

import com.alibaba.dubbo.rpc.RpcException;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ParameterError;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ProcessBizException;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.RestResponse;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode.ApiError.RPC_CALL_VALIDATION_ERROR;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.RestResponse.fail;

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
 * 17/1/8     melvin                 Created
 */
public class ConstraintUtil {

    public static boolean isConstraintViolationException(Throwable t) {
        return (t instanceof RpcException && t.getCause() instanceof ConstraintViolationException);
    }

    public static <T> RestResponse<T> toValidationError(Exception ex) {
        RpcException rpcException = (RpcException) ex;
        ConstraintViolationException constraintViolationException = (ConstraintViolationException) rpcException.getCause();
        Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
        return fail(
                RPC_CALL_VALIDATION_ERROR,
                constraintViolations.stream().
                        map(constraintViolation -> new ParameterError(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage())).
                        collect(Collectors.toList()));
    }

    public static ProcessBizException getValidationException(Exception ex) {
        RpcException rpcException = (RpcException) ex;
        ConstraintViolationException constraintViolationException = (ConstraintViolationException) rpcException.getCause();
        Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
        return new ProcessBizException(RPC_CALL_VALIDATION_ERROR,
                constraintViolations.stream().
                        map(constraintViolation -> new ParameterError(
                                constraintViolation.getPropertyPath().toString(),
                                constraintViolation.getMessage())).
                        collect(Collectors.toList()));
    }

    public static void throwValidationException(Exception ex) throws ProcessBizException {
        throw getValidationException(ex);
    }
}
