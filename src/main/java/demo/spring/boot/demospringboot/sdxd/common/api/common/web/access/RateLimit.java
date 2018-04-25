package demo.spring.boot.demospringboot.sdxd.common.api.common.web.access;

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
 * 2017/12/7     melvin                 Created
 */
public @interface RateLimit {

    double permitsPerSecond();
}
