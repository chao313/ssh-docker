package demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
@ApiModel("Rest响应结果")
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect(isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, fieldVisibility = Visibility.ANY)
public class RestResponse<T> implements Serializable {

    public static final String SUCCESS_CODE = "0";

    public static boolean isSuccessful(String code) {
        return SUCCESS_CODE.equals(code);
    }

    public static <T> RestResponse nothing() {
        return null;
    }

    public static <T> RestResponse<T> ok() {
        return ok(null);
    }

    public static <T> RestResponse<T> ok(T result) {
        return new RestResponse<>("0", result);
    }

    public static <T> RestResponse<T> fail(String code, String errorMessage) {
        return fail(new ErrorCode(code, errorMessage));
    }

    public static <T> RestResponse<T> fail(ErrorCode code) {
        return new RestResponse<>(code.getCode(), new RestResponseError(code.getMessage(), null, null));
    }

    public static RestResponse<String> fail(ErrorCode code, String details) {
        return new RestResponse<>(code.getCode(), new RestResponseError(code.getMessage(), details, null));
    }

    public static <T> RestResponse<T> fail(ErrorCode code, List<ParameterError> errors) {
        return new RestResponse<>(code.getCode(), new RestResponseError(code.getMessage(), null, errors));
    }

    public static RestResponse<String> fail(ErrorCode code, RestResponseError error) {
        return new RestResponse<>(code.getCode(), error);
    }

    @ApiModelProperty("状态码，成功为0")
    private String code;
    @ApiModelProperty("响应内容")
    private T content;

    private RestResponseError error;

    public RestResponse() {
    }

    protected RestResponse(String code, T content) {
        this.code = code;
        this.content = content;
    }

    private RestResponse(String code, RestResponseError error) {
        this.code = code;
        this.error = error;
    }

    private RestResponse(String code, T content, RestResponseError error) {
        this.code = code;
        this.content = content;
        this.error = error;
    }

    public boolean isSuccessful() {
        return SUCCESS_CODE.equals(code);
    }

    public String getCode() {
        return code;
    }

    void setCode(String code) {
        this.code = code;
    }

    public T getContent() {
        return content;
    }

    void setContent(T content) {
        this.content = content;
    }

    public RestResponseError getError() {
        return error;
    }

    void setError(RestResponseError error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return error == null ? null : error.getMessage();
    }

    public ErrorCode toErrorCode() {
        return new ErrorCode(code, getErrorMessage());
    }

    public RestResponse ignoreContent() {
        this.content = null;
        return this;
    }
}
