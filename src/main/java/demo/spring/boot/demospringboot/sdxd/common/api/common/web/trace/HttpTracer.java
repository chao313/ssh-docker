package demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace;

import com.google.common.collect.ObjectArrays;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.RestContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;
import java.util.function.BiConsumer;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.filter
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/2/4     melvin                 Created
 */
public class HttpTracer {

    private static final Logger log = LoggerFactory.getLogger(HttpTracer.class);

    private static final String FIELD_SEPARATOR = ":";
    private static final String LINE_SEPARATOR = "|";

    private static void LOG(BiConsumer<String, Object[]> consumer, String format, Object... objects) {
        if (!RestContext.isLogging()) {
            return;
        }
        String full = "REC#{}#{}: ".concat(format);
        Object[] arrays = ObjectArrays.concat(
                new Object[]{RestContext.getContextId(), RestContext.getPrincipal()},
                objects, Object.class);
        consumer.accept(full, arrays);
    }

    public static void TRACE(Logger logger, String format, Object... objects) {
        LOG(logger::trace, format, objects);
    }

    public static void DEBUG(Logger logger, String format, Object... objects) {
        LOG(logger::debug, format, objects);
    }

    public static void WARN(Logger logger, String format, Object... objects) {
        String full = "REC#{}#{}: ".concat(format);
        Object[] arrays = ObjectArrays.concat(
                new Object[]{RestContext.getContextId(), RestContext.getPrincipal()},
                objects, Object.class);
        logger.warn(full, arrays);
    }

    public static void ERROR(Logger logger, String format, Object... objects) {
        String full = "REC#{}#{}: ".concat(format);
        Object[] arrays = ObjectArrays.concat(
                new Object[]{RestContext.getContextId(), RestContext.getPrincipal()},
                objects, Object.class);
        logger.error(full, arrays);
    }

    public static void traceRequest(HttpBodyCachingRequestWrapper request) {
        if (request.isHideAll()) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP>REQ").append(FIELD_SEPARATOR).
                append(request.getContextId()).append(FIELD_SEPARATOR).
                append(request.getRemoteAddress()).append(FIELD_SEPARATOR).
                append(request.getPrincipal()).append(FIELD_SEPARATOR).
                append(request.getMethod()).append(FIELD_SEPARATOR).
                append(request.getRequestURI());

        if (StringUtils.isNotBlank(request.getQueryString())) {
            builder.append("?").append(request.getQueryString());
        }

        builder.append(FIELD_SEPARATOR).append(request.getProtocol()).append(LINE_SEPARATOR);

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String header = request.getHeader(headerName);
            builder.append(headerName).append(": ").append(header).append(LINE_SEPARATOR);
        }

        builder.append(LINE_SEPARATOR).append(request.isHideBody() ? request.getHiddenBody() : request.getBody()).append(LINE_SEPARATOR);

        log.debug("{}", builder.toString());
    }

    public static void traceResponse(HttpBodyCachingRequestWrapper request, HttpBodyCachingResponseWrapper response) {
        if (request.isHideAll()) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP>RES").append(FIELD_SEPARATOR).
                append(request.getContextId()).append(FIELD_SEPARATOR).
                append(request.getPrincipal()).append(FIELD_SEPARATOR).
                append(request.getPeriod()).append(FIELD_SEPARATOR).
                append(request.getProtocol()).append(FIELD_SEPARATOR).
                append(response.getStatus()).append(LINE_SEPARATOR);

        response.getHeaderNames().forEach(headerName -> {
            String header = response.getHeader(headerName);
            builder.append(headerName).append(": ").append(header).append(LINE_SEPARATOR);
        });

        builder.append(LINE_SEPARATOR).append(response.isHideBody() ? response.getHiddenBody() : response.getBody()).append(LINE_SEPARATOR);

        log.debug("{}", builder.toString());
    }
}
