package demo.spring.boot.demospringboot.sdxd.common.api.common.web.security;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode;

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
 * 16/10/27     melvin                 Created
 */
public class InvalidTokenException extends AuthenticationException {

    private ErrorCode errorCode;

    public InvalidTokenException(ErrorCode errorCode) {
        super("Invalid token found in request header");

        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
