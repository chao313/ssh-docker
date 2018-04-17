/**
  * Copyright 2018 bejson.com 
  */
package demo.spring.boot.demospringboot.vo.docker.inspect;

import lombok.ToString;

/* created by chao*/
@lombok.Data
@ToString
public class Data {

    private String MergedDir;
    private String UpperDir;
    private String WorkDir;
    private String LowerDir;
}