package demo.spring.boot.demospringboot.sdxd.common.api.common.web.inform;


import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.configure.ConfigureEvent;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.configure.ConfigureSubscriber;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.configure.InCacheConfigure;
import demo.spring.boot.demospringboot.sdxd.common.redis.template.RedisClientTemplate;

import java.util.Map;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.report
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/3/6     melvin                 Created
 */
public class ReportConfigure extends InCacheConfigure {

    static final String MAIL_CONFIGURE_UPDATE_EVENT_TYPE = "mail-configure-changed";

    static final String MAIL_ACCOUNT_FIELD_KEY = "account";
    static final String MAIL_PASSWORD_FIELD_KEY = "password";
    static final String MAIL_SENDER_FIELD_KEY = "sender";
    static final String MAIL_RECIPIENT_FIELD_KEY = "recipient";

    private static final String MAIL_CONFIGURE_KEY = "report:email";

    public ReportConfigure(RedisClientTemplate redis, ConfigureSubscriber subscriber) {
        super(redis, subscriber);
    }

    String getMailAccount() {
        return getRedis().hget(MAIL_CONFIGURE_KEY, MAIL_ACCOUNT_FIELD_KEY);
    }

    String getMailPassword() {
        return getRedis().hget(MAIL_CONFIGURE_KEY, MAIL_PASSWORD_FIELD_KEY);
    }

    String getMailSender() {
        return getRedis().hget(MAIL_CONFIGURE_KEY, MAIL_SENDER_FIELD_KEY);
    }

    String getMailRecipient() {
        return getRedis().hget(MAIL_CONFIGURE_KEY, MAIL_RECIPIENT_FIELD_KEY);
    }

    void setMailConfigure(String field, String value) {
        getRedis().hset(MAIL_CONFIGURE_KEY, field, value);
        configureChanged(MAIL_CONFIGURE_KEY,
                new ConfigureEvent(MAIL_CONFIGURE_UPDATE_EVENT_TYPE, field, value));
    }

    public Map<String, String> getAll() {
        return getRedis().hgetAll(MAIL_CONFIGURE_KEY);
    }
}
