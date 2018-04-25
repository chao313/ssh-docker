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
public class TraceSummary {

    private Map<String, String> totalReq;
    private Map<String, String> totalRes;
    private Map<String, String> totalErr;
    private Map<String, String> totalPeriod;

    public TraceSummary(Map<String, String> totalReq, Map<String, String> totalRes, Map<String, String> totalErr, Map<String, String> totalPeriod) {
        this.totalReq = totalReq;
        this.totalRes = totalRes;
        this.totalErr = totalErr;
        this.totalPeriod = totalPeriod;
    }

    public Map<String, String> getTotalReq() {
        return totalReq;
    }

    public Map<String, String> getTotalRes() {
        return totalRes;
    }

    public Map<String, String> getTotalErr() {
        return totalErr;
    }

    public Map<String, String> getTotalPeriod() {
        return totalPeriod;
    }
}
