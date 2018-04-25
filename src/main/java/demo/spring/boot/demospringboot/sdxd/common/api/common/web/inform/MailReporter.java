package demo.spring.boot.demospringboot.sdxd.common.api.common.web.inform;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.inform.ReportConfigure.*;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.inform.ReportConfigure.MAIL_ACCOUNT_FIELD_KEY;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.inform.ReportConfigure.MAIL_CONFIGURE_UPDATE_EVENT_TYPE;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.inform.ReportConfigure.MAIL_PASSWORD_FIELD_KEY;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.inform.ReportConfigure.MAIL_RECIPIENT_FIELD_KEY;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.inform.ReportConfigure.MAIL_SENDER_FIELD_KEY;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace.HttpTracer.DEBUG;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.MapUtil.$;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.inform
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/3/6     melvin                 Created
 */
public class MailReporter implements IReport {

    private static final Logger log = LoggerFactory.getLogger(MailReporter.class);

    private Map<String, Consumer<String>> consumers = $(
            MAIL_ACCOUNT_FIELD_KEY, this::setAccount,
            MAIL_PASSWORD_FIELD_KEY, this::setPassword,
            MAIL_SENDER_FIELD_KEY, this::setSender,
            MAIL_RECIPIENT_FIELD_KEY, this::setRecipient);

    private ReportConfigure configure;

    @Autowired
    private IReportService reportService;

    private String account;
    private String password;
    private String sender;
    private List<String> recipient;

    public MailReporter(ReportConfigure configure) {
        this.configure = configure;
        this.configure.consume(MAIL_CONFIGURE_UPDATE_EVENT_TYPE, event -> {
            Consumer<String> consumer = consumers.get(event.getName());
            if (consumer != null) {
                consumer.accept(event.getValue());
            }
        });
    }

    @Override
    public void report(String title, String body) {
        if (reportService == null) {
            return;
        }
        String account = getAccount();
        String password = getPassword();
        String sender = getSender();
        List<String> recipient = getRecipient();
        if (recipient == null || recipient.size() == 0) {
            DEBUG(log, "Mail configure is not complete, mail will not send");
            return;
        }
        Postman postman = new Postman(new MailAccount(account, password), sender, recipient);
        reportService.reportByEmail(postman, title, body);
    }

    private String getAccount() {
        if (StringUtils.isBlank(account)) {
            setAccount(configure.getMailAccount());
        }
        return account;
    }

    private String getPassword() {
        if (StringUtils.isBlank(password)) {
            setPassword(configure.getMailPassword());
        }
        return password;
    }

    private String getSender() {
        if (StringUtils.isBlank(sender)) {
            setSender(configure.getMailSender());
        }
        return sender;
    }

    private List<String> getRecipient() {
        if (recipient == null || recipient.size() == 0) {
            setRecipient(configure.getMailRecipient());
        }
        return recipient;
    }

    private void setAccount(String account) {
        this.account = account;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    private void setSender(String value) {
        this.sender = value;
    }

    private void setRecipient(String value) {
        this.recipient = StringUtils.isBlank(value) ? null : Lists.newArrayList(value.split(","));
    }
}
