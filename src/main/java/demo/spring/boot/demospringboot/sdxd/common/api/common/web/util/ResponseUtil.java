package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.AuthenticationFailedException;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.InvalidTokenException;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.JwtTokenMissingException;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ProcessBizException;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.RestResponse;
import demo.spring.boot.demospringboot.sdxd.common.pojo.dubbo.DubboResponse;
import demo.spring.boot.demospringboot.sdxd.framework.constant.Constants;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletResponse;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConstraintUtil.isConstraintViolationException;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConstraintUtil.toValidationError;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode.ApiError.NO_RESPONSE_ERROR_ERROR;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode.ApiError.NO_RESPONSE_STATUS_ERROR;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode.AuthenticationError.TOKEN_NOT_FOUND;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode.SystemError.SERVER_INTERNAL_ERROR;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.RestResponse.fail;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.RestResponse.ok;
import static demo.spring.boot.demospringboot.sdxd.framework.constant.Constants.System.SYSTEM_ERROR_CODE;
import static demo.spring.boot.demospringboot.sdxd.framework.constant.Constants.System.SYSTEM_ERROR_MSG;

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
 * 16/10/27     melvin                 Created
 */
public class ResponseUtil {

    private static final Logger log = LoggerFactory.getLogger(ResponseUtil.class);

    public static boolean isSuccessfulResponse(DubboResponse response) {
        return response != null &&
                Constants.System.OK.equals(response.getStatus()) &&
                Constants.System.SERVER_SUCCESS.equals(response.getError());
    }

    public static <T> RestResponse<T> failure(DubboResponse response) {
        if (response == null) {
            return fail(SYSTEM_ERROR_CODE, SYSTEM_ERROR_MSG);
        }
        return fail(response.getError(), response.getMsg());
    }

    public static <T> RestResponse<T> rest(DubboResponse<T> response) {
        return rest(response, t -> t);
    }

    public static <T, R> RestResponse<R> rest(DubboResponse<T> response, Function<T, R> function) {
        if (response == null) {
            return fail(SYSTEM_ERROR_CODE, SYSTEM_ERROR_MSG);
        }
        if (StringUtils.isBlank(response.getStatus())) {
            return fail(NO_RESPONSE_STATUS_ERROR);
        }
        if (StringUtils.isBlank(response.getError())) {
            return fail(NO_RESPONSE_ERROR_ERROR);
        }
        if (isSuccessfulResponse(response)) {
            R result = (function == null || response.getData() == null) ? null : function.apply(response.getData());
            return ok(result);
        }
        return fail(response.getError(), response.getMsg());
    }

    public static <T> T data(DubboResponse<T> response) throws ProcessBizException {
        return data(response, t -> t);
    }

    public static <T, R> R data(DubboResponse<T> response, Function<T, R> function) throws ProcessBizException {
        if (response == null) {
            throw new ProcessBizException(new ErrorCode(SYSTEM_ERROR_CODE, SYSTEM_ERROR_MSG));
        }
        if (StringUtils.isBlank(response.getStatus())) {
            throw new ProcessBizException(NO_RESPONSE_STATUS_ERROR);
        }
        if (StringUtils.isBlank(response.getError())) {
            throw new ProcessBizException(NO_RESPONSE_ERROR_ERROR);
        }
        if (isSuccessfulResponse(response)) {
            return function == null ? null : function.apply(response.getData());
        }
        throw new ProcessBizException(new ErrorCode(response.getError(), response.getMsg()));
    }

    public static <F, T> T copy(F from, T to) {
        if (from == null || to == null) {
            return null;
        }
        try {
            PropertyUtils.copyProperties(to, from);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.warn("Copy value failed, {}", e.getMessage());
        }
        return to;
    }

    public static <T, P> T transform(Map<String, P> map, T bean) {
        try {
            org.apache.commons.beanutils.BeanUtils.populate(bean, map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.warn("Copy value failed, {}", e.getMessage());
        }
       return bean;
    }

    public static void setProperty(Object bean, String name, Object value) {
        try {
            BeanUtils.setProperty(bean, name, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.warn("Set property failed, {}", e.getMessage());
        }
    }

    public static void success(HttpServletResponse response, ObjectMapper mapper, Object content) throws IOException {
        RestResponse ok = ok(content);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();
        mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        mapper.writeValue(writer, ok);
        writer.flush();
    }

    public static void failure(HttpServletResponse response, ObjectMapper mapper, String status, String message) throws IOException {
        RestResponse ok = fail(status, message);
        failure(response, mapper, ok);
    }

    public static void failure(HttpServletResponse response, ObjectMapper mapper, ErrorCode errorCode) throws IOException {
        RestResponse ok = fail(errorCode);
        failure(response, mapper, ok);
    }

    public static <T> void failure(HttpServletResponse response, ObjectMapper mapper, T value) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();
        mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        mapper.writeValue(writer, value);
        writer.flush();
    }

    public static void authenticationException(
            ObjectMapper mapper,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException {
        if (isConstraintViolationException(exception.getCause())) {
            failure(response, mapper, toValidationError((Exception) exception.getCause()));
            return;
        }
        if (AuthenticationFailedException.class.isInstance(exception)) {
            AuthenticationFailedException authenticationFailedException = (AuthenticationFailedException) exception;
            failure(response, mapper, authenticationFailedException.getStatus(), authenticationFailedException.getMessage());
            return;
        }
        if (InvalidTokenException.class.isInstance(exception)) {
            InvalidTokenException invalidTokenException = (InvalidTokenException) exception;
            ErrorCode errorCode = invalidTokenException.getErrorCode();
            failure(response, mapper, errorCode.getCode(), errorCode.getMessage());
            return;
        }
        if (JwtTokenMissingException.class.isInstance(exception)) {
            failure(response, mapper, TOKEN_NOT_FOUND);
            return;
        }
        failure(response, mapper, SERVER_INTERNAL_ERROR);
    }
}
