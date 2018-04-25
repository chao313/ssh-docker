package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.util
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/2/7     melvin                 Created
 */
public interface ConsumerE<T, E extends Exception> {

    void accept(T t) throws E;
}
