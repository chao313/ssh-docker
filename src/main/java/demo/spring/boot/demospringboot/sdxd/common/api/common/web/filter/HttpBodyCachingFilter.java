package demo.spring.boot.demospringboot.sdxd.common.api.common.web.filter;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.access.AccessEvent;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.access.AccessListener;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace.HttpAccess;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace.HttpBodyCachingRequestWrapper;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace.HttpBodyCachingResponseWrapper;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.access.AccessEvent.CATEGORY_HTTP;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace.HttpTracer.traceRequest;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace.HttpTracer.traceResponse;

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
 * 17/1/24     melvin                 Created
 */
public class HttpBodyCachingFilter extends RestApiFilter {

    private AccessListener accessListener;

    public HttpBodyCachingFilter() {
        super();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);

        WebApplicationContext applicationContext =
                WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
        accessListener = applicationContext.getBean(AccessListener.class);
    }

    @Override
    public void destroy() {

    }

    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpBodyCachingRequestWrapper httpBodyCachingRequestWrapper =
                HttpBodyCachingRequestWrapper.class.isInstance(request) ?
                        (HttpBodyCachingRequestWrapper) httpServletRequest :
                        new HttpBodyCachingRequestWrapper(httpServletRequest);
        HttpBodyCachingResponseWrapper httpBodyCachingResponseWrapper = new HttpBodyCachingResponseWrapper(httpServletResponse);

        if (accessListener != null) {
            accessListener.onEvent(AccessEvent.in(CATEGORY_HTTP, httpBodyCachingRequestWrapper.getRequestPath(), httpBodyCachingRequestWrapper));
        }
        traceRequest(httpBodyCachingRequestWrapper);
        chain.doFilter(httpBodyCachingRequestWrapper, httpBodyCachingResponseWrapper);
        traceResponse(httpBodyCachingRequestWrapper, httpBodyCachingResponseWrapper);
        if (accessListener != null) {
            HttpAccess access = new HttpAccess(httpBodyCachingRequestWrapper, httpBodyCachingResponseWrapper);
            accessListener.onEvent(AccessEvent.out(CATEGORY_HTTP, httpBodyCachingRequestWrapper.getRequestPath(), access));
        }
    }
}
