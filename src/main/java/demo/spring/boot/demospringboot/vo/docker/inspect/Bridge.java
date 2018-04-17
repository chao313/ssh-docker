/**
  * Copyright 2018 bejson.com 
  */
package demo.spring.boot.demospringboot.vo.docker.inspect;

import lombok.Data;
import lombok.ToString;

/* created by chao*/
@Data
@ToString
public class Bridge {
    private String GlobalIPv6Address;
    private int IPPrefixLen;
    private int GlobalIPv6PrefixLen;
    private String NetworkID;
    private String MacAddress;
    private String Gateway;
    private String EndpointID;
    private String IPv6Gateway;
    private String IPAddress;
}