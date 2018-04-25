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
 * 17/1/5     melvin                 Created
 */
public interface FunctionE<T, R, E extends Exception> {

    R apply(T t) throws E;
}
