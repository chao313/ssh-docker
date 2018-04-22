package demo.spring.boot.demospringboot.vo.request;

import com.github.dockerjava.api.model.ExposedPorts;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volumes;
import com.github.dockerjava.core.command.CreateContainerCmdImpl;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 2018/4/21    Created by   chao
 */
@ApiModel
@Data
@ToString
public class ContainerRequest {
    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "Hostname")
    private String hostName;

    @ApiModelProperty(value = "domainName")
    private String domainName;

    @ApiModelProperty(value = "user")
    private String user;

    @ApiModelProperty(value = "AttachStdin")
    private Boolean attachStdin;

    @ApiModelProperty(value = "AttachStdout")
    private Boolean attachStdout;

    @ApiModelProperty(value = "AttachStderr")
    private Boolean attachStderr;

    @ApiModelProperty(value = "PortSpecs")
    private String[] portSpecs;

    @ApiModelProperty(value = "Tty")
    private Boolean tty;

    @ApiModelProperty(value = "OpenStdin")
    private Boolean stdinOpen;

    @ApiModelProperty(value = "StdinOnce")
    private Boolean stdInOnce;

    @ApiModelProperty(value = "Env")
    private String[] env;

    @ApiModelProperty(value = "Cmd")
    private String[] cmd;

    @ApiModelProperty(value = "Entrypoint")
    private String[] entrypoint;

    @ApiModelProperty(value = "Image")
    private String image;

    @ApiModelProperty(value = "Volumes")
    private Volumes volumes = new Volumes();

    @ApiModelProperty(value = "WorkingDir")
    private String workingDir;

    @ApiModelProperty(value = "MacAddress")
    private String macAddress;

    @ApiModelProperty(value = "NetworkDisabled")
    private Boolean networkDisabled;

    @ApiModelProperty(value = "ExposedPorts")
    private ExposedPorts exposedPorts = new ExposedPorts();

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_21}
     */
    @ApiModelProperty(value = "StopSignal")
    private String stopSignal;

    @ApiModelProperty(value = "HostConfig")
    private HostConfig hostConfig = new HostConfig();

    @ApiModelProperty(value = "Labels")
    private Map<String, String> labels;

    @ApiModelProperty(value = "NetworkingConfig")
    private CreateContainerCmdImpl.NetworkingConfig networkingConfig;

    @ApiModelProperty(value = "ipv4Address")
    private String ipv4Address = null;

    @ApiModelProperty(value = "ipv6Address")
    private String ipv6Address = null;

    @ApiModelProperty(value = "aliases")
    private List<String> aliases = null;

}
