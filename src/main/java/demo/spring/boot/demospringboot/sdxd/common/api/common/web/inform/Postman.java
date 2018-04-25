package demo.spring.boot.demospringboot.sdxd.common.api.common.web.inform;

import java.util.List;

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
 * 17/3/7     melvin                 Created
 */
public class Postman {

    private MailAccount account;
    private String from;
    private List<String> to;

    public Postman(MailAccount account, String from, List<String> to) {
        this.account = account;
        this.from = from;
        this.to = to;
    }

    public String getUser() {
        return this.account == null ? null : this.account.getUser();
    }

    public String getPassword() {
        return this.account == null ? null : this.account.getPassword();
    }

    public String getFrom() {
        return from;
    }

    public List<String> getTo() {
        return to;
    }
}
