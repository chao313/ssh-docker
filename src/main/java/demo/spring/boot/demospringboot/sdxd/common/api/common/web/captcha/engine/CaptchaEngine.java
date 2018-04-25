package demo.spring.boot.demospringboot.sdxd.common.api.common.web.captcha.engine;

import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.component.image.backgroundgenerator.GradientBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.image.gimpy.BasicGimpyEngine;

import java.awt.*;

public class CaptchaEngine extends BasicGimpyEngine {

    private String acceptedChars;

    public CaptchaEngine() {
        this(null);
    }

    public CaptchaEngine(String acceptedChars) {
        // 字体格式
        Font[] fontsList =
                new Font[]{new Font("Arial", 0, 15), new Font("Tahoma", 0, 15),
                        new Font("Verdana", 0, 15),};
        // 文字的大小
        FontGenerator fonts = new RandomFontGenerator(18, 18, fontsList);
        // DeformedRandomFontGenerator fonts = new DeformedRandomFontGenerator(18, 18);
        GradientBackgroundGenerator background =
                new GradientBackgroundGenerator(85, 25, Color.white, Color.white);
        RandomListColorGenerator colorGenerator =
                new RandomListColorGenerator(new Color[]{Color.decode("#87CEEB"), Color.decode("#4682B4"),
                        Color.decode("#6A5ACD")});
        TextPaster textPaster =
                new TextPaster(6, 6, colorGenerator);
        ComposedWordToImage word2image = new ComposedWordToImage(fonts, background, textPaster);
        this.acceptedChars =
                (acceptedChars == null || acceptedChars.trim().length() == 0) ? "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"//"abcdefghijklmnopqrstuvwxyz0123456789"
                        : acceptedChars;
        RandomWordGenerator words = new RandomWordGenerator(this.acceptedChars);
        CaptchaImageFactory captchaImageFactory = new CaptchaImageFactory(words, word2image);

        setFactories(new CaptchaFactory[]{captchaImageFactory});
    }
}
