package demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.MultiNameHeaderParam;

import io.swagger.annotations.ApiParam;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/1/23     melvin                 Created
 */
public class SignatureRequest extends RestRequest {

    @ApiParam(value = "设备ID", required = true)
    @MultiNameHeaderParam(value = "x-device-id", alias = {"device_id"})
    private String deviceId;

    @ApiParam(value = "时间戳", required = true)
    @MultiNameHeaderParam(value = "x-timestamp", alias = {"timestamp"})
    private String timestamp;

    @ApiParam(value = "随机码", required = true)
    @MultiNameHeaderParam(value = "x-nonce", alias = {"nonce"})
    private String nonce;

    @ApiParam(value = "签名", required = true)
    @MultiNameHeaderParam(value = "x-sign", alias = {"sign"})
    private String sign;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
