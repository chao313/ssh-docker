/**
  * Copyright 2018 bejson.com 
  */
package demo.spring.boot.demospringboot.vo.docker.inspect;

import lombok.Data;
import lombok.ToString;

/* created by chao*/
@Data
@ToString
public class NetworkSettings {

    private boolean HairpinMode;
    private Networks Networks;
    private String SandboxKey;
    private String GlobalIPv6Address;
//    private Ports Ports;
    private int GlobalIPv6PrefixLen;
    private int IPPrefixLen;
    private String MacAddress;
    private String SandboxID;
    private String LinkLocalIPv6Address;
    private int LinkLocalIPv6PrefixLen;
    private String Gateway;
    private String EndpointID;
    private String IPv6Gateway;
    private String IPAddress;
    private String Bridge;
}