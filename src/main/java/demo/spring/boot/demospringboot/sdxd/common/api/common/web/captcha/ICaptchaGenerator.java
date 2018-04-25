package demo.spring.boot.demospringboot.sdxd.common.api.common.web.captcha;

import java.io.IOException;

public interface ICaptchaGenerator {

    CaptchaImage question(String id, String format) throws IOException;

    boolean answer(String id, String content);

    boolean hasQuestion(String id);
}
