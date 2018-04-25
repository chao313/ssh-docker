package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rpc;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.access.AccessEvent;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.access.AccessListener;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.RestContext;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.JsonUtil;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.access.AccessEvent.CATEGORY_RPC;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConstraintUtil.isConstraintViolationException;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 16/12/19     melvin                 Created
 */
public class RemoteServiceTraceInterceptor extends CustomizableTraceInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RemoteServiceTraceInterceptor.class);

    private static final Pattern PATTERN = Pattern.compile("\\$\\[\\p{Alpha}+\\]");

    private static final String CONTEXT_ID = "$[contextId]";
    private static final String HEADERS = "$[headers]";
    private static final String PRINCIPAL = "$[principal]";

    private String enterMessage;
    private String exitMessage;
    private String exceptionMessage;

    private AccessListener accessListener;

    public AccessListener getAccessListener() {
        return accessListener;
    }

    public void setAccessListener(AccessListener accessListener) {
        this.accessListener = accessListener;
    }

    @Override
    public void setEnterMessage(String enterMessage) throws IllegalArgumentException {
        Assert.hasText(enterMessage, "'enterMessage' must not be empty");
        Assert.doesNotContain(enterMessage, PLACEHOLDER_RETURN_VALUE,
                "enterMessage cannot contain placeholder [" + PLACEHOLDER_RETURN_VALUE + "]");
        Assert.doesNotContain(enterMessage, PLACEHOLDER_EXCEPTION,
                "enterMessage cannot contain placeholder [" + PLACEHOLDER_EXCEPTION + "]");
        Assert.doesNotContain(enterMessage, PLACEHOLDER_INVOCATION_TIME,
                "enterMessage cannot contain placeholder [" + PLACEHOLDER_INVOCATION_TIME + "]");
        this.enterMessage = enterMessage;
    }

    @Override
    public void setExitMessage(String exitMessage) {
        Assert.hasText(exitMessage, "'exitMessage' must not be empty");
        Assert.doesNotContain(exitMessage, PLACEHOLDER_EXCEPTION,
                "exitMessage cannot contain placeholder [" + PLACEHOLDER_EXCEPTION + "]");
        this.exitMessage = exitMessage;
    }

    @Override
    public void setExceptionMessage(String exceptionMessage) {
        Assert.hasText(exceptionMessage, "'exceptionMessage' must not be empty");
        Assert.doesNotContain(exceptionMessage, PLACEHOLDER_RETURN_VALUE,
                "exceptionMessage cannot contain placeholder [" + PLACEHOLDER_RETURN_VALUE + "]");
        Assert.doesNotContain(exceptionMessage, PLACEHOLDER_INVOCATION_TIME,
                "exceptionMessage cannot contain placeholder [" + PLACEHOLDER_INVOCATION_TIME + "]");
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        if (Modifier.isStatic(method.getModifiers()) ||
                !Modifier.isPublic(method.getModifiers()) ||
                "toString".equals(method.getName())) {
            return invocation.proceed();
        }
        return super.invoke(invocation);
    }

    @Override
    protected Object invokeUnderTrace(MethodInvocation invocation, Log logger) throws Throwable {
        Method method = invocation.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();
        String name = declaringClass.getName() + "." + method.getName();
        StopWatch stopWatch = new StopWatch(name);
        Object returnValue = null;
        boolean exitThroughException = false;
        try {
            stopWatch.start(name);
            Long invokePeriod = periodAfterRequest();
            access(AccessEvent.in(CATEGORY_RPC, name, invokePeriod));
            writeToLog(logger,
                    replacePlaceholders(this.enterMessage, invocation, null, null, -1));
            returnValue = invocation.proceed();
            return returnValue;
        }
        catch (Throwable ex) {
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }
            exitThroughException = true;
            if (!isConstraintViolationException(ex)) {
                long period = stopWatch.getTotalTimeMillis();
                RemoteServiceInvocation invoke = new RemoteServiceInvocation(period, ex);
                access(AccessEvent.err(CATEGORY_RPC, name, invoke));
                writeToLog(logger,
                        replacePlaceholders(this.exceptionMessage, invocation, null, ex, period), ex);
            }
            throw ex;
        }
        finally {
            if (!exitThroughException) {
                if (stopWatch.isRunning()) {
                    stopWatch.stop();
                }
                long period = stopWatch.getTotalTimeMillis();
                RemoteServiceInvocation invoke = new RemoteServiceInvocation(period, returnValue);
                access(AccessEvent.out(CATEGORY_RPC, name, invoke));
                writeToLog(logger,
                        replacePlaceholders(this.exitMessage, invocation, JsonUtil.toJson(returnValue, true), null, period));
            }
        }
    }

    protected String replacePlaceholders(String message, MethodInvocation methodInvocation,
                                         Object returnValue, Throwable throwable, long invocationTime) {

        Matcher matcher = PATTERN.matcher(message);

        StringBuffer output = new StringBuffer();
        while (matcher.find()) {
            String match = matcher.group();
            if (CONTEXT_ID.equals(match)) {
                matcher.appendReplacement(output, Matcher.quoteReplacement(RestContext.getContextId()));
            } else if (HEADERS.equals(match)) {
                Map<String, String> headers = RestContext.getLoggingHeaders();
                String text = headers == null || headers.isEmpty() ?
                        "" : headers.entrySet().stream().
                        map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue())).
                        reduce(new StringBuilder(), (builder, value) -> builder.length() == 0 ? builder.append(value) : builder.append("|").append(value), (b1, b2) -> b1).
                        toString();
                matcher.appendReplacement(output, Matcher.quoteReplacement(text));
            } else if (PRINCIPAL.equals(match)) {
                matcher.appendReplacement(output, Matcher.quoteReplacement(RestContext.getPrincipal()));
            } else if (PLACEHOLDER_METHOD_NAME.equals(match)) {
                matcher.appendReplacement(output, Matcher.quoteReplacement(methodInvocation.getMethod().getName()));
            } else if (PLACEHOLDER_TARGET_CLASS_NAME.equals(match)) {
                String className = getServiceClass(methodInvocation);
                matcher.appendReplacement(output, Matcher.quoteReplacement(className));
            } else if (PLACEHOLDER_TARGET_CLASS_SHORT_NAME.equals(match)) {
                String shortName = ClassUtils.getShortName(getServiceClass(methodInvocation));
                matcher.appendReplacement(output, Matcher.quoteReplacement(shortName));
            } else if (PLACEHOLDER_ARGUMENTS.equals(match)) {
                boolean hideBody = RestContext.isHideRequestBody();
                String arguments = hideBody ? "..." : JsonUtil.toJson(methodInvocation.getArguments(), true);
                if (arguments == null) arguments = "";
                matcher.appendReplacement(output, Matcher.quoteReplacement(arguments));
            } else if (PLACEHOLDER_ARGUMENT_TYPES.equals(match)) {
                appendArgumentTypes(methodInvocation, matcher, output);
            } else if (PLACEHOLDER_RETURN_VALUE.equals(match)) {
                appendReturnValue(methodInvocation, matcher, output, returnValue);
            } else if (throwable != null && PLACEHOLDER_EXCEPTION.equals(match)) {
                matcher.appendReplacement(output, Matcher.quoteReplacement(throwable.toString()));
            } else if (PLACEHOLDER_INVOCATION_TIME.equals(match)) {
                matcher.appendReplacement(output, Long.toString(invocationTime));
            } else {
                // Should not happen since placeholders are checked earlier.
                throw new IllegalArgumentException("Unknown placeholder [" + match + "]");
            }
        }
        matcher.appendTail(output);

        return output.toString();
    }

    @Override
    protected boolean isLogEnabled(Log logger) {
        return true;
    }

    @Override
    protected void writeToLog(Log logger, String message) {
        if (!RestContext.isLogging()) {
            return;
        }
        super.writeToLog(logger, message);
    }

    @Override
    protected void writeToLog(Log logger, String message, Throwable ex) {
        if (!RestContext.isLogging()) {
            return;
        }
        if (ex != null) {
            log.error(message, ex);
        } else {
            log.debug(message);
        }
    }

    private String getServiceClass(MethodInvocation methodInvocation) {
        Method method = methodInvocation.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();
        return declaringClass.getName();
    }

    private void appendArgumentTypes(MethodInvocation methodInvocation, Matcher matcher, StringBuffer output) {
        Class<?>[] argumentTypes = methodInvocation.getMethod().getParameterTypes();
        String[] argumentTypeShortNames = new String[argumentTypes.length];
        for (int i = 0; i < argumentTypeShortNames.length; i++) {
            argumentTypeShortNames[i] = ClassUtils.getShortName(argumentTypes[i]);
        }
        matcher.appendReplacement(output,
                Matcher.quoteReplacement(StringUtils.arrayToCommaDelimitedString(argumentTypeShortNames)));
    }

    private void appendReturnValue(
            MethodInvocation methodInvocation, Matcher matcher, StringBuffer output, Object returnValue) {

        if (methodInvocation.getMethod().getReturnType() == void.class) {
            matcher.appendReplacement(output, "void");
        }
        else if (returnValue == null) {
            matcher.appendReplacement(output, "null");
        }
        else {
            boolean hideBody = RestContext.isHideResponseBody();
            String value = hideBody ? "..." : returnValue.toString();
            matcher.appendReplacement(output, Matcher.quoteReplacement(value));
        }
    }

    private Long periodAfterRequest() {
        Date entry = RestContext.getRequestEntry();
        if (entry == null) {
            return null;
        }
        long start = entry.getTime();
        long now = System.currentTimeMillis();
        return now - start;
    }

    private void access(AccessEvent event) {
        if (accessListener != null) {
            accessListener.onEvent(event);
        }
    }
}
