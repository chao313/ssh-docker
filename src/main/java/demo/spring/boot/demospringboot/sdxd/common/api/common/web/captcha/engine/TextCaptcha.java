package demo.spring.boot.demospringboot.sdxd.common.api.common.web.captcha.engine;

import com.octo.captcha.image.ImageCaptcha;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class TextCaptcha extends ImageCaptcha implements Serializable {

    private static final long serialVersionUID = 8951519131827499831L;

    private String response;

    private int expiresIn;

    TextCaptcha(String question, BufferedImage challenge, String response, int expiresIn) {
        super(question, challenge);
        this.response = response;
        this.expiresIn = expiresIn;
    }

    public TextCaptcha(String response) {
        this(response, -1);
    }

    public TextCaptcha(String response, int expiresIn) {
        super(null, null);
        this.response = response;
        this.expiresIn = expiresIn;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    @Override
    public String toString() {
        return response;
    }

    @Override
    public Boolean validateResponse(Object arg0) {
        return null != response ? validateResponse((String) arg0)
                : Boolean.FALSE;
    }

    private Boolean validateResponse(final String response) {
        return response.toLowerCase().equals(this.response.toLowerCase());
    }
}
