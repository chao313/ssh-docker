/**
  * Copyright 2018 bejson.com 
  */
package demo.spring.boot.demospringboot.vo.docker.inspect;

import lombok.Data;
import lombok.ToString;

/* created by chao*/
@Data
@ToString
public class LogConfig {
    private String Type;
    private Config Config;

}