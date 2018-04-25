package demo.spring.boot.demospringboot.sdxd.common.api.common.web.captcha;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.captcha
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/1/3     melvin                 Created
 */
public class CaptchaImage {

    private String id;
    private byte[] img;
    private int expiresIn;

    public CaptchaImage(String id, byte[] img, int expiresIn) {
        this.id = id;
        this.img = img;
        this.expiresIn = expiresIn;
    }

    public String getId() {
        return id;
    }

    public byte[] getImg() {
        return img;
    }

    public int getExpiresIn() {
        return expiresIn;
    }
}
