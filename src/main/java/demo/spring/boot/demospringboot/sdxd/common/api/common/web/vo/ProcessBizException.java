package demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

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
public class ProcessBizException extends Exception {

    private ErrorCode code;
    private RestResponseError error;

    public ProcessBizException(ErrorCode code) {
        this(code, "");
    }

    public ProcessBizException(ErrorCode code, String message) {
        this(code, null, null, message);
    }

    public ProcessBizException(ErrorCode code, String source, String error) {
        this(code, source, error, null);
    }

    public ProcessBizException(ErrorCode code, String source, String error, String details) {
        super(code.getMessage());
        List<ParameterError> parameterErrors =
                (StringUtils.isBlank(source) || StringUtils.isBlank(error)) ?
                        null : Lists.newArrayList(new ParameterError(source, error));
        this.code = code;
        this.error = new RestResponseError(code.getMessage(), details, parameterErrors);
    }

    public ProcessBizException(ErrorCode code, List<ParameterError> errors) {
        super(code.getMessage());
        this.code = code;
        this.error = new RestResponseError(code.getMessage(), null, errors);
    }

    public ErrorCode getCode() {
        return code;
    }

    public void setCode(ErrorCode code) {
        this.code = code;
    }

    public RestResponseError getError() {
        return error;
    }

    public void setError(RestResponseError error) {
        this.error = error;
    }

    public <T> RestResponse<T> toResult() {
        RestResponse<T> response = new RestResponse<>();
        response.setCode(this.code.getCode());
        response.setError(this.error);
        return response;
    }

    public List<String> getErrorMessages() {
        RestResponseError error = getError();
        if (error != null) {
            List<ParameterError> errors = error.getErrors();
            if (errors != null) {
                return errors.stream().map(ParameterError::getMessage).collect(Collectors.toList());
            }
            return Lists.newArrayList(error.getMessage());
        }
        return Lists.newArrayList();
    }

    @Override
    public String toString() {
        return String.format("code: %s, message: %s", code.getCode(), code.getMessage());
    }
}
