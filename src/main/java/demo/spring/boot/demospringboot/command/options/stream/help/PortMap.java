package demo.spring.boot.demospringboot.command.options.stream.help;

import lombok.Data;
import lombok.ToString;

/**
 * 2018/4/17    Created by   chao
 */
@Data
@ToString
public class PortMap {
    private Integer hostPort;
    private Integer containerPort;

    public PortMap() {
    }

    public PortMap(Integer hostPort, Integer containerPort) {
        this.hostPort = hostPort;
        this.containerPort = containerPort;
    }
}
