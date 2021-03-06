package demo.spring.boot.demospringboot.sdxd.common.api.common.web.security;

import org.springframework.security.core.Authentication;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.security
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 16/11/3     melvin                 Created
 */
public interface IAuthentication<T> extends Authentication {

    T getToken();

    boolean valid();

    AuthenticationStatus status();

    boolean isAccess();
}
