package demo.spring.boot.demospringboot.sdxd.common.api.common.web.interceptor;

import org.springframework.web.servlet.ModelAndView;

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
public interface PostHandler {

    void handle(HttpServletRequest request,
                HttpServletResponse response,
                Object handler,
                ModelAndView modelAndView) throws Exception;
}
