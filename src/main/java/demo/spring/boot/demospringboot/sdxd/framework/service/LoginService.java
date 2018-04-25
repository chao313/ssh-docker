package demo.spring.boot.demospringboot.sdxd.framework.service;


import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import demo.spring.boot.demospringboot.sdxd.common.exception.DataCommitException;
import demo.spring.boot.demospringboot.sdxd.framework.constant.enums.PermissionResult;
import demo.spring.boot.demospringboot.sdxd.framework.dto.LoginResponse;
import demo.spring.boot.demospringboot.sdxd.framework.dto.LoginUserDto;

public interface LoginService {

    LoginUserDto userLoginSuccess(LoginUserDto user, String token, String loginIp, HttpServletRequest request, HttpServletResponse response) throws DataCommitException;

    @Transactional(readOnly = false)
    boolean userLoginSuccess(LoginUserDto user, HttpServletRequest request, HttpServletResponse response);

    /**
     * 过滤器检查用户登陆情况和权限
     */
    public PermissionResult checkPermission(HttpServletRequest request);


    /**
     * <p>用户登录</p>
     *
     * @author QiuYangjun
     * @date 2014-5-6 下午4:04:47
     * @see
     */
    public LoginResponse login(String loginName, String password, String loginIp, HttpServletRequest request, HttpServletResponse response) throws DataCommitException;

    /**
     * 用户登出
     */
    public void logout(String userId, String loginIp) throws DataCommitException;

}
