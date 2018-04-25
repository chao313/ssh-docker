package demo.spring.boot.demospringboot.sdxd.common.api.common.web.captcha.engine;

import com.octo.captcha.CaptchaException;
import com.octo.captcha.component.image.color.ColorGenerator;
import com.octo.captcha.component.image.textpaster.SimpleTextPaster;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.AttributedString;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.captcha.engine
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/1/3     melvin                 Created
 */
public class TextPaster extends SimpleTextPaster {

    public TextPaster(Integer minAcceptedWordLength, Integer maxAcceptedWordLength,
                            Color textColor) {
        super(minAcceptedWordLength, maxAcceptedWordLength, textColor);
    }

    public TextPaster(Integer minAcceptedWordLength, Integer maxAcceptedWordLength,
                            ColorGenerator colorGenerator) {
        super(minAcceptedWordLength, maxAcceptedWordLength, colorGenerator);
    }

    public TextPaster(Integer minAcceptedWordLength, Integer maxAcceptedWordLength,
                            ColorGenerator colorGenerator, Boolean manageColorPerGlyph) {
        super(minAcceptedWordLength, maxAcceptedWordLength, colorGenerator, manageColorPerGlyph);
    }

    /**
     * Pastes the attributed string on the backround image and return the final image. Implementation must take into
     * account the fact that the text must be readable by human and non by programs. Pastes the text at width/20 and
     * height/2
     *
     * @return the final image
     *
     * @throws com.octo.captcha.CaptchaException
     *          if any exception accurs during paste routine.
     */
    public BufferedImage pasteText(final BufferedImage background,
                                   final AttributedString attributedWord) throws CaptchaException {
        int x = (background.getWidth()) / 20;
        int y = background.getHeight() - (background.getHeight()) / 3;
        BufferedImage out = copyBackground(background);
        Graphics2D g2 = pasteBackgroundAndSetTextColor(out, background);
        //pie.drawString(attributedWord.getIterator(), x, y);
        //pie.dispose();

        // convert string into a series of glyphs we can work with
        TextAttributedString newAttrString = new TextAttributedString(g2,
                attributedWord, 2);

        // space out the glyphs with a little kerning
        newAttrString.useMinimumSpacing(1);
        //newAttrString.useMonospacing(0);
        // shift string to a random spot in the output imge
        newAttrString.moveTo(x, y);
        // now draw each glyph at the appropriate spot on the image.
        if (isManageColorPerGlyph())
            newAttrString.drawString(g2, getColorGenerator());
        else
            newAttrString.drawString(g2);

        g2.dispose();
        return out;
    }

    private BufferedImage copyBackground(final BufferedImage background) {
        return new BufferedImage(background.getWidth(), background.getHeight(), background.getType());
    }

    private Graphics2D pasteBackgroundAndSetTextColor(BufferedImage out, final BufferedImage background) {
        Graphics2D pie = (Graphics2D) out.getGraphics();
        //paste background
        pie.drawImage(background, 0, 0, out.getWidth(), out.getHeight(), null);
        //pie.setColor(getTextColor());
        pie.setColor(getColorGenerator().getNextColor());
        return pie;
    }
}