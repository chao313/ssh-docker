package demo.spring.boot.demospringboot.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.ToString;

/**
 * 2018/4/27    Created by   chao
 */
@Data
@ToString
@ApiModel
public class HostRequest {
    @ApiParam(value = "host的id")
    @ApiModelProperty(value="id")
    private Integer id;

    @ApiParam(value = "主机ip")
    @ApiModelProperty(value="host-ip")
    private String hostIp;


    @ApiParam(value = "主机端口号")
    @ApiModelProperty(value="port")
    private Integer port;


    @ApiParam(value = "主机用户名")
    @ApiModelProperty(value="account")
    private String account;


    @ApiParam(value = "主机密码")
    @ApiModelProperty(value="password")
    private String password;


    @ApiParam(value = "所属者")
    @ApiModelProperty(value="user-id")
    private String userId;
}
