
package demo.spring.boot.demospringboot.vo.response;

import com.github.dockerjava.api.model.ContainerHostConfig;
import com.github.dockerjava.api.model.ContainerNetworkSettings;
import com.github.dockerjava.api.model.ContainerPort;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ContainerResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String command;

    private Long created;

    private String id;

    private String image;

    private String imageId;

    private String[] names;

    public ContainerPort[] ports;

    public Map<String, String> labels;

    private String status;

    private Long sizeRw;

    private Long sizeRootFs;

    private ContainerHostConfig hostConfig;

    private ContainerNetworkSettings networkSettings;

    private String app;

    private Boolean running;

}
