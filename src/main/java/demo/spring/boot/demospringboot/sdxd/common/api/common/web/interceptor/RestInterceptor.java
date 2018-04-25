package demo.spring.boot.demospringboot.sdxd.common.api.common.web.interceptor;

import com.google.common.collect.Lists;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.interceptor
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 16/12/30     melvin                 Created
 */
public class RestInterceptor extends HandlerInterceptorAdapter {

    private List<PreHandler> preHandlers = Lists.newArrayList();
    private List<PostHandler> postHandlers = Lists.newArrayList();
    private List<CompleteHandler> completeHandlers = Lists.newArrayList();

    public void addPreHandler(PreHandler handler) {
        this.preHandlers.add(handler);
    }

    public void addPostHandler(PostHandler handler) {
        this.postHandlers.add(handler);
    }

    public void addCompleteHandler(CompleteHandler handler) {
        this.completeHandlers.add(handler);
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        for (PreHandler preHandler : preHandlers) {
            boolean result = preHandler.handle(request, response, handler);
            if (!result) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) throws Exception {
        for (PostHandler postHandler : postHandlers) {
            postHandler.handle(request, response, handler, modelAndView);
        }
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) throws Exception {
        for (CompleteHandler completeHandler : completeHandlers) {
            completeHandler.handle(request, response, handler, ex);
        }
    }
}
