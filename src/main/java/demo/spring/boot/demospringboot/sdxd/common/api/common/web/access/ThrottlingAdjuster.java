package demo.spring.boot.demospringboot.sdxd.common.api.common.web.access;

import org.springframework.util.Assert;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.access
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/2/20     melvin                 Created
 */
public class ThrottlingAdjuster {

    private ThrottlingConfigure configure;

    public ThrottlingAdjuster(ThrottlingConfigure configure) {
        Assert.notNull(configure, "throttling configure can not be null.");
        this.configure = configure;
    }

    public void adjust(String api, double threshold) {
        if (this.configure != null) {
            this.configure.put(api, threshold);
        }
    }
}
