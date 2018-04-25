package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.inform.IReport;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.HttpUtil;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.Tasks;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ParameterError;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ProcessBizException;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.RestResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConstraintUtil.isConstraintViolationException;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConstraintUtil.toValidationError;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode.ApiError.PARAMETER_VALIDATION_ERROR;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.RestResponse.fail;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：com.wanmei.biz.consumer.addons.exception
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 16/4/19     melvin                 Created
 */
public class GlobalHandlerExceptionResolver implements HandlerExceptionResolver {

    private static final Logger log = LoggerFactory.getLogger(GlobalHandlerExceptionResolver.class);

    private final ObjectMapper mapper;

    @Autowired(required = false)
    private IReport reporter;

    GlobalHandlerExceptionResolver(MappingJackson2HttpMessageConverter jackson2HttpMessageConverter) {
        this.mapper = jackson2HttpMessageConverter.getObjectMapper();
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        RestResponse failure = fail(ErrorCode.SystemError.SERVER_INTERNAL_ERROR, ex.getMessage());
        int status = HttpServletResponse.SC_OK;

        if (BindException.class.isInstance(ex)) {
            BindException bindException = (BindException) ex;
            List<FieldError> errors = bindException.getFieldErrors();
            failure = createFieldErrorsResponse(errors);
        } else if (MethodArgumentNotValidException.class.isInstance(ex)) {
            MethodArgumentNotValidException argumentException = (MethodArgumentNotValidException) ex;
            BindingResult bindingResult = argumentException.getBindingResult();
            List<FieldError> errors = bindingResult.getFieldErrors();
            failure = createFieldErrorsResponse(errors);
        } else if (isConstraintViolationException(ex)) {
            failure = toValidationError(ex);
        } else if (isProcessBizException(ex)) {
            //noinspection ThrowableResultOfMethodCallIgnored
            ProcessBizException exception = getProcessBizException(ex);
            failure = exception == null ? null : exception.toResult();
        }/* else {
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }*/

        if (!BindException.class.isInstance(ex) &&
                !MethodArgumentNotValidException.class.isInstance(ex) &&
                !isProcessBizException(ex) &&
                !isConstraintViolationException(ex)) {
            if (HttpMediaTypeNotSupportedException.class.isInstance(ex)) {
                log.error("Media type error, handler is {}", handler);
            }

            if (reporter != null) {
                Tasks.execute(() -> inform(request, ex));
            }
            log.error("Global exception({},{},{}): ", RestContext.getContextId(), RestContext.getPrincipal(), HttpUtil.getRemoteIp(request), ex);
        }

        response.setStatus(status);
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            PrintWriter writer = response.getWriter();
            mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
            mapper.writeValue(writer, failure);
            writer.flush();
        } catch (Exception e) {
            log.error("Write exception error: ", e);
        }
        return new ModelAndView();
    }

    private void inform(HttpServletRequest request, Exception ex) {
        reporter.report(String.format(
                "Exception in context: %s,%s,%s",
                RestContext.getContextId(),
                RestContext.getPrincipal(),
                HttpUtil.getRemoteIp(request)),
                ExceptionUtils.getStackTrace(ex));
    }

    private static RestResponse createFieldErrorsResponse(List<FieldError> errors) {
        return fail(
                PARAMETER_VALIDATION_ERROR,
                errors.stream().
                        map(error -> new ParameterError(error.getField(), error.getDefaultMessage())).
                        collect(Collectors.toList()));
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
}
