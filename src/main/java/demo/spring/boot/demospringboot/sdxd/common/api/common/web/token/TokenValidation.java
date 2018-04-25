package demo.spring.boot.demospringboot.sdxd.common.api.common.web.token;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.Subject;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.util
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/2/20     melvin                 Created
 */
public class TokenValidation {

    private String token;

    private boolean valid;
    private Subject subject;
    private String audience;

    private long expiresIn;

    private boolean accessToken;

    private ErrorCode errorCode;

    public TokenValidation(
            String token, boolean valid, Subject subject,
            String audience, long expiresIn, boolean accessToken,
            ErrorCode errorCode) {
        this.token = token;
        this.valid = valid;
        this.subject = subject;
        this.audience = audience;
        this.expiresIn = expiresIn;
        this.accessToken = accessToken;

        this.errorCode = errorCode;
    }

    public String getToken() {
        return token;
    }

    public boolean isValid() {
        return valid;
    }

    public Subject getSubject() {
        return subject;
    }

    public String getAudience() {
        return audience;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public boolean isAccessToken() {
        return accessToken;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
