package demo.spring.boot.demospringboot.sdxd.framework.filter;

import com.alibaba.fastjson.JSONObject;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import demo.spring.boot.demospringboot.sdxd.common.pojo.dto.ResponseEntity;
import demo.spring.boot.demospringboot.sdxd.framework.constant.Constants;
import demo.spring.boot.demospringboot.sdxd.framework.constant.enums.PermissionResult;
import demo.spring.boot.demospringboot.sdxd.framework.context.ContextUtils;
import demo.spring.boot.demospringboot.sdxd.framework.service.LoginService;


/**
 * 登陆检测过滤器
 * <p style="display:none">
 * modifyRecord
 * </p>
 * 
 * @author QiuYangjun
 * @date 2014-5-7 上午10:58:58
 * @since
 * @version
 */
public class LoginFilter implements Filter {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private LoginService loginService;


	/**
	 * @author QiuYangjun
	 * @date 2014-5-7 上午10:58:59
	 * @param filterConfig
	 * @throws javax.servlet.ServletException
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	/**
	 * <p>
	 * session中检测是否登陆,如果没有检测是否有cookie,如果有cookie重建登陆信息
	 * </p>
	 *
	 * @author QiuYangjun
	 * @date 2014-5-7 上午10:58:59
	 * @param chain
	 * @throws IOException
	 * @throws javax.servlet.ServletException
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

        // 如果处理HTTP请求，并且需要访问诸如getHeader或getCookies等在ServletRequest中无法得到的方法，就要把此request对象构造成HttpServletRequest
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        HttpSession session = request.getSession();

        loginService = (LoginService) ContextUtils.getBean(LoginService.class);
        PermissionResult result = null;
        try {
            result = loginService.checkPermission(request);
        }catch (Exception e){
            result = PermissionResult.SESSION_TIMEOUT;
            logger.error(e.getMessage(),e);
        }
        switch (result){
            case PERMISSION_SUCCESS:
                chain.doFilter(servletRequest,servletResponse);
                break;
            case PERMISSION_DENIED:
                response.getOutputStream().println(getPermissionResultEntity());
                break;
            case SESSION_TIMEOUT:
                response.getOutputStream().println(getTimeoutResultEntity());
                break;
            default:
                break;
        }


	}

    private String getPermissionResultEntity() {
        ResponseEntity ret = new ResponseEntity();
        ret.setStatus(Constants.System.FAIL);
        ret.setError(Constants.System.PERMISSION_DENIED);
        ret.setMsg("permission denied");

        return JSONObject.toJSON(ret).toString();
    }


    protected String getTimeoutResultEntity() {
		ResponseEntity ret = new ResponseEntity();
		ret.setStatus(Constants.System.FAIL);
		ret.setError(Constants.System.SESSION_TIMEOUT);
		ret.setMsg("session timeout");

		return JSONObject.toJSON(ret).toString();
	}

	/**
	 * @author QiuYangjun
	 * @date 2014-5-7 上午10:58:59
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
