package demo.spring.boot.demospringboot.sdxd.common.api.common.web.inform;

import org.springframework.util.Assert;

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
public class ReportAdjuster {

    private ReportConfigure configure;

    public ReportAdjuster(ReportConfigure configure) {
        Assert.notNull(configure, "report configure can not be null.");
        this.configure = configure;
    }

    public void adjust(String field, String value) {
        if (this.configure != null) {
            this.configure.setMailConfigure(field, value);
        }
    }
}
