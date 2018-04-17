/**
 * Copyright 2018 bejson.com
 */
package demo.spring.boot.demospringboot.vo;

import java.util.List;

import lombok.Data;
import lombok.ToString;

/* created by chao*/
@Data
@ToString
public class InspectJsonVo {

    private String Path;
    private List<String> Args;
    private String ProcessLabel;
    private List<demo.spring.boot.demospringboot.vo.docker.inspect.Mounts> Mounts;
    private String Platform;
    private demo.spring.boot.demospringboot.vo.docker.inspect.Config Config;
    private String Driver;
    private String AppArmorProfile;
    private String HostsPath;
    private String HostnamePath;
    private String Image;
    //    private Date Created;
    private String Name;
    private int RestartCount;
    private demo.spring.boot.demospringboot.vo.docker.inspect.NetworkSettings NetworkSettings;
    private String MountLabel;
    private demo.spring.boot.demospringboot.vo.docker.inspect.State State;
    private String ResolvConfPath;
    private demo.spring.boot.demospringboot.vo.docker.inspect.HostConfig HostConfig;
    private String Id;
    private String LogPath;

}