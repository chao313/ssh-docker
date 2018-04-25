package demo.spring.boot.demospringboot.sdxd.common.api.common.web.captcha;

import com.octo.captcha.Captcha;
import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.CaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class ImageCaptchaService extends DefaultManageableImageCaptchaService {

    private Logger log = LoggerFactory.getLogger(ImageCaptchaService.class);

    public ImageCaptchaService(CaptchaStore captchaStore, CaptchaEngine captchaEngine,
                               int minGuarantedStorageDelayInSeconds, int maxCaptchaStoreSize,
                               int captchaStoreLoadBeforeGarbageCollection) {
        super(captchaStore, captchaEngine, minGuarantedStorageDelayInSeconds, maxCaptchaStoreSize,
                captchaStoreLoadBeforeGarbageCollection);
    }

    @Override
    protected Captcha generateAndStoreCaptcha(Locale locale, String ID) {
        Captcha captcha = engine.getNextCaptcha(locale);
        this.store.storeCaptcha(ID, captcha, locale);

        log.debug("生成图片验证码: {}, id: {}", captcha.toString(), ID);
        return captcha;
    }

    @Override
    public Boolean validateResponseForID(String ID, Object response) throws CaptchaServiceException {
        try {
            boolean result = super.validateResponseForID(ID, response);
            log.debug("验证图片验证码: {}, id: {}, 结果: {}", response, ID, result);
            return result;
        } catch (CaptchaServiceException e) {
            log.error(String.format("验证图片验证码错误: %s, id: %s", response, ID), e);
            throw e;
        }
    }
}
