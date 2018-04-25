package demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.MultiNameHeaderParam;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class SignatureParameters extends RestRequest {

    @ApiParam(value = "商户号", required = true)
    @MultiNameHeaderParam(value = "x-merchant-no", alias = {"merchant_no"})
    private String merchantNo;

    @ApiParam(value = "时间戳", required = true)
    @MultiNameHeaderParam(value = "x-timestamp", alias = {"timestamp"})
    private String timestamp;

    @ApiParam(value = "随机码", required = true)
    @MultiNameHeaderParam(value = "x-nonce", alias = {"nonce"})
    private String nonce;

    @ApiParam(value = "签名", required = true)
    @MultiNameHeaderParam(value = "x-sign", alias = {"sign"})
    private String sign;
}
