package demo.spring.boot.demospringboot.sdxd.common.api.common.web.interceptor;

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
public interface PreHandler {

    boolean handle(HttpServletRequest request,
                   HttpServletResponse response,
                   Object handler) throws Exception;
}
