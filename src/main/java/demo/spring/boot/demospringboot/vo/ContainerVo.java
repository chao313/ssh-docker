package demo.spring.boot.demospringboot.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 2018/4/17    Created by   chao
 */

@Data
@ToString
public class ContainerVo {

    private String CONTAINER_ID;

    private String IMAGE;

    private String COMMAND;

    private String CREATED;

    private String STATUS;

    private String PORTS;

    private String NAMES;
}
