package demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：com.wanmei.biz.vo.common
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 16/4/7     melvin                 Created
 */
public class ErrorCode {

    private String code;
    private String message;

    public ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", code, message);
    }

    public interface SystemError {
        ErrorCode SERVER_INTERNAL_ERROR = new ErrorCode("500001", "( >﹏<。)～ 系统有点累，请稍候再试。");
    }

    public interface AuthenticationError {
        ErrorCode UNAUTHORIZED = new ErrorCode("400001", "未授权资源");
        ErrorCode UNKNOWN_CREDENTIAL = new ErrorCode("400002", "未知的凭证");
        ErrorCode TOKEN_EXPIRED = new ErrorCode("400003", "Token已失效");
        ErrorCode INVALID_TOKEN = new ErrorCode("400004", "无效的Token");
        ErrorCode INVALID_CREDENTIAL = new ErrorCode("400005", "用户名或密码错误");
        ErrorCode TOKEN_NOT_FOUND = new ErrorCode("400006", "Token未填写");
    }

    public interface ResourceError {
        ErrorCode CAN_NOT_ACCESS = new ErrorCode("410001", "资源不可访问");
    }

    public interface ApiError {
        ErrorCode PARAMETER_VALIDATION_ERROR = new ErrorCode("600001", "参数验证失败");
        ErrorCode RPC_CALL_VALIDATION_ERROR = new ErrorCode("600002", "调用RPC接口参数验证失败");
        ErrorCode NO_RESPONSE_ERROR = new ErrorCode("600003", "调用RPC接口无响应");
        ErrorCode NO_RESPONSE_STATUS_ERROR = new ErrorCode("600004", "调用RPC接口无响应状态码");
        ErrorCode NO_RESPONSE_ERROR_ERROR = new ErrorCode("600005", "调用RPC接口无响应错误码");
        ErrorCode INVALID_SIGNATURE = new ErrorCode("600006", "签名验证失败");
        ErrorCode REQUEST_TOO_BUSY = new ErrorCode("600007", "请求太频繁");
        ErrorCode DENIED_BY_RULE = new ErrorCode("600008", "请求被拒");
    }
}
