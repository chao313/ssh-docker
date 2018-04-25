package demo.spring.boot.demospringboot.sdxd.common.api.common.web.filter;

import com.google.common.collect.Sets;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.RestContext;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace.HttpBodyCachingRequestWrapper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

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
public class RestApiFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RestApiFilter.class);

    private Set<String> excludePaths;

    public RestApiFilter() {
        this.excludePaths = Sets.newHashSet();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String excludePaths = filterConfig.getInitParameter("exclude.paths");
        if (StringUtils.isNotBlank(excludePaths)) {
            this.excludePaths = Sets.newHashSet(excludePaths.split(","));
        }

        if (StringUtils.isBlank(RestContext.getWebRoot())) {
            String webRoot = filterConfig.getServletContext().getRealPath("/");
            RestContext.setWebRoot(webRoot);
            log.info("Set web root: {}", webRoot);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String path = httpServletRequest.getRequestURI();
        if (exclude(path)) {
            chain.doFilter(request, response);
            return;
        }

        doFilterInternal(request, response, chain);
    }

    @Override
    public void destroy() {

    }

    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpBodyCachingRequestWrapper httpBodyCachingRequestWrapper = new HttpBodyCachingRequestWrapper(httpServletRequest);
        chain.doFilter(httpBodyCachingRequestWrapper, response);
    }

    private boolean exclude(String path) {
        long matchCount = excludePaths.stream().
                filter(path::startsWith).count();
        return matchCount > 0;
    }
}
