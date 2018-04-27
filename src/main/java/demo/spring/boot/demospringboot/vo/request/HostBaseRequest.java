package demo.spring.boot.demospringboot.vo.request;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 2018/4/27    Created by   chao
 */
@Data
@ToString
@ApiModel
public class HostBaseRequest {
    @ApiModelProperty(value = "id", name = "host的id")
    @NotBlank
    private Integer id;
}
