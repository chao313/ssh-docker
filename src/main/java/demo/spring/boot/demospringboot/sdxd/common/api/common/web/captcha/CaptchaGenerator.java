package demo.spring.boot.demospringboot.sdxd.common.api.common.web.captcha;

import com.octo.captcha.Captcha;
import com.octo.captcha.service.captchastore.CaptchaStore;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.captcha.engine.TextCaptcha;

public class CaptchaGenerator implements ICaptchaGenerator {

    /*

    <bean id="captchaGenerator" class="demo.spring.boot.demospringboot.sdxd.common.api.common.web.captcha.CaptchaGenerator">
        <property name="imageCaptchaService" ref="imageCaptchaService" />
        <property name="captchaStore" ref="captchaStore" />
    </bean>

    <bean id="imageCaptchaService" class="demo.spring.boot.demospringboot.sdxd.common.api.common.web.captcha.ImageCaptchaService">
        <constructor-arg index="0" ref="captchaStore" />
        <constructor-arg index="1" ref="captchaEngine" />
        <constructor-arg index="2" value="180" />
        <constructor-arg index="3" value="100000" />
        <constructor-arg index="4" value="75000" />
        <property name="minGuarantedStorageDelayInSeconds" value="30" />
    </bean>
    <bean id="captchaEngine" class="demo.spring.boot.demospringboot.sdxd.common.api.common.web.captcha.engine.CaptchaEngine" />
    <bean id="captchaStore" class="demo.spring.boot.demospringboot.sdxd.common.api.common.web.captcha.store.DefaultCaptchaStore">
        <constructor-arg name="redis" ref="redisClientTemplate"/>
    </bean>

     */

    private ImageCaptchaService imageCaptchaService;

    private CaptchaStore captchaStore;

    public ImageCaptchaService getImageCaptchaService() {
        return imageCaptchaService;
    }

    public void setImageCaptchaService(ImageCaptchaService imageCaptchaService) {
        this.imageCaptchaService = imageCaptchaService;
    }

//    public CaptchaStore getCaptchaStore() {
//        return captchaStore;
//    }
//
//    public void setCaptchaStore(CaptchaStore captchaStore) {
//        this.captchaStore = captchaStore;
//    }

    @Override
    public CaptchaImage question(String id, String format) throws IOException {
        if (format == null) {
            format = "png";
        }
        Captcha captcha = imageCaptchaService.generateAndStoreCaptcha(Locale.getDefault(), id);
        TextCaptcha textCaptcha = TextCaptcha.class.cast(captcha);
        BufferedImage image = textCaptcha.getImageChallenge();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        ImageIO.write(image, format, Base64.getEncoder().wrap(os));
        ImageIO.write(image, format, os);
        byte[] img = os.toByteArray();
        return new CaptchaImage(id, img, textCaptcha.getExpiresIn());

//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        String challenge = GraphicUtil.drawNumber(format, out);
//        byte[] img = out.toByteArray();
//        Captcha holder = new Captcha(id, img, getCaptchaExpiresIn());
//        captchaStore.storeCaptcha(id, new TextCaptcha(challenge), null);
//        return holder;
    }

    @Override
    public boolean answer(String id, String content) {
        try {
            Boolean result = imageCaptchaService.validateResponseForID(id, content);
            return result != null && result;
        } catch (Exception e) {
            return false;
        }
//        try {
//            TextCaptcha captcha = (TextCaptcha) captchaStore.getCaptcha(id);
//            return captcha != null && captcha.toString().equals(content);
//        } catch (Exception e) {
//            return false;
//        }
    }

    @Override
    public boolean hasQuestion(String id) {
        return captchaStore.hasCaptcha(id);
    }

    public static void main(String[] args) {
//        CaptchaGenerator generator = new CaptchaGenerator();
//        CaptchaStore store = new CaptchaStore() {
//            @Override
//            public boolean hasCaptcha(String id) {
//                return false;
//            }
//
//            @Override
//            public void storeCaptcha(String id, Captcha captcha) throws CaptchaServiceException {
//
//            }
//
//            @Override
//            public void storeCaptcha(String id, Captcha captcha, Locale locale) throws CaptchaServiceException {
//
//            }
//
//            @Override
//            public boolean removeCaptcha(String id) {
//                return false;
//            }
//
//            @Override
//            public Captcha getCaptcha(String id) throws CaptchaServiceException {
//                return null;
//            }
//
//            @Override
//            public Locale getLocale(String id) throws CaptchaServiceException {
//                return null;
//            }
//
//            @Override
//            public int getSize() {
//                return 0;
//            }
//
//            @Override
//            public Collection getKeys() {
//                return null;
//            }
//
//            @Override
//            public void empty() {
//
//            }
//        };
//        CaptchaEngine engine = new CaptchaEngine();
//        ImageCaptchaService imageCaptchaService = new ImageCaptchaService(store, engine, 180, 100000, 75000);
//        imageCaptchaService.setMinGuarantedStorageDelayInSeconds(30);
//        generator.setImageCaptchaService(imageCaptchaService);
//        try {
//            CaptchaImage image = generator.question(BillNoUtils.GenerateBillNo(), null);
//            System.out.println(JsonUtil.toJson(image));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
