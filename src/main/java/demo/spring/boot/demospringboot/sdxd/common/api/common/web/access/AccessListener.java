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
 * 2017/7/11     melvin                 Created
 */
public interface AccessListener {

    void onEvent(AccessEvent event);
}
