/**
  * Copyright 2018 bejson.com 
  */
package demo.spring.boot.demospringboot.vo.docker.inspect;

import lombok.Data;
import lombok.ToString;

/* created by chao*/
@Data
@ToString
public class Mounts {

    private String Destination;
    private String Type;
    private String Propagation;
    private boolean RW;
    private String Mode;
    private String Source;
}