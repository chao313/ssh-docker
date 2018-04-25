package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.dubbo;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ProcessBizException;

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
 * 16/11/18     melvin                 Created
 */
public interface ParameterServiceSupplier<P, R> {

    R get(P param) throws ProcessBizException;
}
