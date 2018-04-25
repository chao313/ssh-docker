package demo.spring.boot.demospringboot.sdxd.common.api.common.web.security;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.RestResponse.isSuccessful;

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
 * 16/12/1     melvin                 Created
 */
public class AuthenticationStatus {

    private String code;
    private String message;

    public AuthenticationStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean valid() {
        return isSuccessful(this.code);
    }
}
