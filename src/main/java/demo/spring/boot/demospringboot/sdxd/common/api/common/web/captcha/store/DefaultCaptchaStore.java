package demo.spring.boot.demospringboot.sdxd.common.api.common.web.captcha.store;

import com.octo.captcha.Captcha;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.CaptchaAndLocale;
import com.octo.captcha.service.captchastore.CaptchaStore;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.captcha.engine.TextCaptcha;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.Cache;
import demo.spring.boot.demospringboot.sdxd.common.redis.template.RedisClientTemplate;

import java.util.Collection;
import java.util.Locale;

public class DefaultCaptchaStore extends Cache implements CaptchaStore {

    private static final String CAPTCHA_KEY_TEMPLATE = "captcha:store:%s";

    public DefaultCaptchaStore(RedisClientTemplate redis) {
        super(redis);
    }

    @Override
    public void empty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Captcha getCaptcha(String id) throws CaptchaServiceException {
        CaptchaHolder holder = getBean(String.format(CAPTCHA_KEY_TEMPLATE, id), CaptchaHolder.class);
        return holder == null ? null : new TextCaptcha(holder.getChallenge(), holder.getExpiresIn());
    }

    @Override
    public Collection<?> getKeys() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Locale getLocale(String id) throws CaptchaServiceException {
        Object captchaAndLocale = getCaptcha(id);
        return captchaAndLocale != null ? ((CaptchaAndLocale) captchaAndLocale)
                .getLocale() : null;
    }

    @Override
    public int getSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasCaptcha(String id) {
        return getRedis().hexists(String.format(CAPTCHA_KEY_TEMPLATE, id), "id");
    }

    @Override
    public boolean removeCaptcha(String id) {
        if (hasCaptcha(id)) {
            getRedis().del(String.format(CAPTCHA_KEY_TEMPLATE, id));
            return true;
        }
        return false;
    }

    @Override
    @Deprecated
    public void storeCaptcha(String id, Captcha captcha)
            throws CaptchaServiceException {
    }

    @Override
    public void storeCaptcha(String id, Captcha captcha, Locale locale)
            throws CaptchaServiceException {
        TextCaptcha textCaptcha = TextCaptcha.class.cast(captcha);
        CaptchaHolder holder = new CaptchaHolder(id, textCaptcha.toString(), textCaptcha.getExpiresIn());
        String key = String.format(CAPTCHA_KEY_TEMPLATE, id);
        this.putBean(key, holder);
        getRedis().expire(key, holder.getExpiresIn());
    }
}
