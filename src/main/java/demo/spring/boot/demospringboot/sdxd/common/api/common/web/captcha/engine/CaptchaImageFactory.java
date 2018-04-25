package demo.spring.boot.demospringboot.sdxd.common.api.common.web.captcha.engine;

import com.octo.captcha.CaptchaException;
import com.octo.captcha.CaptchaQuestionHelper;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.image.ImageCaptcha;
import com.octo.captcha.image.gimpy.GimpyFactory;

import java.awt.image.BufferedImage;
import java.util.Locale;

public class CaptchaImageFactory extends GimpyFactory {

    private static final int DEFAULT_CAPTCHA_EXPIRES_IN = 3600;

    private Integer captchaExpiresIn;

    public CaptchaImageFactory(WordGenerator generator, WordToImage word2image) {
        super(generator, word2image);
    }

    public Integer getCaptchaExpiresIn() {
        return captchaExpiresIn == null ? DEFAULT_CAPTCHA_EXPIRES_IN : captchaExpiresIn;
    }

    public void setCaptchaExpiresIn(Integer captchaExpiresIn) {
        this.captchaExpiresIn = captchaExpiresIn;
    }

    @Override
    public ImageCaptcha getImageCaptcha(Locale locale) {

        // length
        Integer wordLength = getRandomLength();

        String word = getWordGenerator().getWord(wordLength, locale);

        BufferedImage image = null;
        try {
            image = getWordToImage().getImage(word);
        } catch (Throwable e) {
            throw new CaptchaException(e);
        }

        return new TextCaptcha(
                CaptchaQuestionHelper.getQuestion(locale, BUNDLE_QUESTION_KEY),
                image, word, getCaptchaExpiresIn());
    }
}
