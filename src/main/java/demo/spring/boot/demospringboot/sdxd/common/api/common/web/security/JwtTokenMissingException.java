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
 * 16/10/27     melvin                 Created
 */
public class JwtTokenMissingException extends AuthenticationException {

    public JwtTokenMissingException() {
        super("JWT Token not found in request header");
    }
}
