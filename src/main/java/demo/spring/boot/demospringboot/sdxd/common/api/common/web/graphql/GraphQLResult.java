package demo.spring.boot.demospringboot.sdxd.common.api.common.web.graphql;

import com.google.common.collect.Lists;

import com.alibaba.dubbo.rpc.RpcException;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.GlobalHandlerExceptionResolver;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.RestContext;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.AuthenticationFailedException;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.InvalidTokenException;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.JwtTokenMissingException;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ProcessBizException;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.RestResponse;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.RestResponseError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import graphql.ExceptionWhileDataFetching;
import graphql.ExecutionResult;
import graphql.GraphQLError;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConstraintUtil.isConstraintViolationException;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConstraintUtil.toValidationError;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode.AuthenticationError.TOKEN_NOT_FOUND;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode.SystemError.SERVER_INTERNAL_ERROR;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.api.vo.graphql
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 2017/8/7     melvin                 Created
 */
public class GraphQLResult implements ExecutionResult {

    private static final Logger log = LoggerFactory.getLogger(GlobalHandlerExceptionResolver.class);

    private static List<GraphQLError> getErrors(ExceptionWhileDataFetching exceptionWhileDataFetching) {
        if (exceptionWhileDataFetching != null) {
            Throwable throwable = exceptionWhileDataFetching.getException();
            if (isProcessBizException(throwable)) {
                //noinspection ThrowableResultOfMethodCallIgnored
                ProcessBizException processBizException = getProcessBizException(throwable);
                if (processBizException != null) {
                    RestResponse response = processBizException.toResult();
                    return toGraphQLErrors(response);
                }
            } else if (isConstraintViolationException(throwable)) {
                RpcException rpcException = (RpcException) throwable;
                ConstraintViolationException constraintViolationException = (ConstraintViolationException) rpcException.getCause();
                Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
                if (constraintViolations == null || constraintViolations.isEmpty()) {
                    return Lists.newArrayList();
                }
                return constraintViolations.stream().
                        map(constraintViolation -> new QueryError(
                                constraintViolation.getPropertyPath().toString(),
                                constraintViolation.getMessage())).
                        collect(Collectors.toList());
            } else {
                log.error("Global exception({},{},{}): ", RestContext.getContextId(), RestContext.getPrincipal(), RestContext.getRemoteAddress(), throwable);
                return Lists.newArrayList(new QueryError(SERVER_INTERNAL_ERROR.getCode(), SERVER_INTERNAL_ERROR.getMessage()));
            }
        }
        return Lists.newArrayList();
    }

    private static List<GraphQLError> toGraphQLErrors(RestResponse response) {
        RestResponseError error = response.getError();
        if (error.getErrors() == null || error.getErrors().isEmpty()) {
            return Lists.newArrayList(new QueryError(response.getCode(), error.getMessage()));
        }
        return error.getErrors().stream().
                map(e -> new QueryError(response.getCode(), e.getParameter(), e.getMessage())).
                collect(Collectors.toList());
    }

    private static boolean isProcessBizException(Throwable t) {
        return t instanceof ProcessBizException || t.getCause() instanceof ProcessBizException;
    }

    private static ProcessBizException getProcessBizException(Throwable t) {
        if (t instanceof ProcessBizException) {
            return (ProcessBizException) t;
        }
        if (t.getCause() instanceof ProcessBizException) {
            return (ProcessBizException) t.getCause();
        }
        return null;
    }

    private ExecutionResult result;
    private List<GraphQLError> errors;

    public GraphQLResult(AuthenticationException exception) {
        if (isConstraintViolationException(exception.getCause())) {
            RestResponse restResponse = toValidationError((Exception) exception.getCause());
            this.errors = toGraphQLErrors(restResponse);
        } else if (AuthenticationFailedException.class.isInstance(exception)) {
            AuthenticationFailedException authenticationFailedException = (AuthenticationFailedException) exception;
            this.errors = Lists.newArrayList(new QueryError(authenticationFailedException.getStatus(), authenticationFailedException.getMessage()));
        } else if (InvalidTokenException.class.isInstance(exception)) {
            InvalidTokenException invalidTokenException = (InvalidTokenException) exception;
            ErrorCode errorCode = invalidTokenException.getErrorCode();
            this.errors = Lists.newArrayList(new QueryError(errorCode.getCode(), errorCode.getMessage()));
        } else if (JwtTokenMissingException.class.isInstance(exception)) {
            this.errors = Lists.newArrayList(new QueryError(TOKEN_NOT_FOUND.getCode(), TOKEN_NOT_FOUND.getMessage()));
        } else {
            this.errors = Lists.newArrayList(new QueryError(SERVER_INTERNAL_ERROR.getCode(), SERVER_INTERNAL_ERROR.getMessage()));
        }
    }

    public GraphQLResult(ExecutionResult result) {
        this.result = result;
        List<GraphQLError> errors = result.getErrors().isEmpty() ? null : result.getErrors();
        if (errors != null) {
            this.errors = errors.stream().
                    map(error -> error instanceof ExceptionWhileDataFetching ?
                            getErrors(ExceptionWhileDataFetching.class.cast(error)) :
                            Lists.newArrayList(error)).
                    flatMap(Collection::stream).
                    collect(Collectors.toList());
            if (this.errors.isEmpty()) {
                this.errors = null;
            }
        }
    }

    @Override
    public <T> T getData() {
        return this.result == null ? null : this.result.getData();
    }

    @Override
    public List<GraphQLError> getErrors() {
        return this.errors;
    }

    @Override
    public Map<Object, Object> getExtensions() {
        return this.result == null ? null : this.result.getExtensions();
    }

    @Override
    public Map<String, Object> toSpecification() {
        return this.result == null ? null : this.result.toSpecification();
    }
}
