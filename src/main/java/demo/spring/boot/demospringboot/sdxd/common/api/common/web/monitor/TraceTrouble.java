package demo.spring.boot.demospringboot.sdxd.common.api.common.web.monitor;

import java.util.Map;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.api.service
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 2017/7/13     melvin                 Created
 */
public class TraceTrouble {

    private Map<String, String> period;
    private Map<String, String> timeout;
    private Map<String, String> blocking;

    public TraceTrouble(Map<String, String> period, Map<String, String> timeout) {
        this.period = period;
        this.timeout = timeout;
    }

    public TraceTrouble(Map<String, String> period, Map<String, String> timeout, Map<String, String> blocking) {
        this.period = period;
        this.timeout = timeout;
        this.blocking = blocking;
    }

    public Map<String, String> getPeriod() {
        return period;
    }

    public Map<String, String> getTimeout() {
        return timeout;
    }

    public Map<String, String> getBlocking() {
        return blocking;
    }
}
