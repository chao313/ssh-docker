package demo.spring.boot.demospringboot.sdxd.common.api.common.web.inform;

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
 * 17/3/6     melvin                 Created
 */
public interface IReportService {

    void reportByEmail(Postman postman, String title, String body);
}
