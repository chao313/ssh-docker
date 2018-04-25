package demo.spring.boot.demospringboot.sdxd.common.api.common.web.captcha.store;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.captcha.store
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/1/3     melvin                 Created
 */
public class CaptchaHolder {

    private String id;
    private String challenge;
    private int expiresIn;

    public CaptchaHolder() {}

    public CaptchaHolder(String id, String challenge, int expiresIn) {
        this.id = id;
        this.challenge = challenge;
        this.expiresIn = expiresIn;
    }

    public String getId() {
        return id;
    }

    public String getChallenge() {
        return challenge;
    }

    public int getExpiresIn() {
        return expiresIn;
    }
}
