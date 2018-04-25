package demo.spring.boot.demospringboot.sdxd.common.pojo.dubbo;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class BaseRiskControlRequest extends BaseRequest {

    @NotNull
    private String merchantNo;
    @NotNull
    private Long userId;
    @NotNull
    private String phone;
    private String platformName;
}
