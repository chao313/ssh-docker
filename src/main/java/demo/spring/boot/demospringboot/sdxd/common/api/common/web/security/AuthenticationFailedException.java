package demo.spring.boot.demospringboot.sdxd.common.api.common.web.security;

import org.springframework.security.core.AuthenticationException;

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
public class AuthenticationFailedException extends AuthenticationException {

    private AuthenticationStatus status;

    public AuthenticationFailedException(AuthenticationStatus status) {
        super(status.getMessage());

        this.status = status;
    }

    public String getStatus() {
        return status.getCode();
    }
}
