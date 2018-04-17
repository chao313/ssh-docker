/**
 * Copyright 2018 bejson.com
 */
package demo.spring.boot.demospringboot.vo.docker.inspect;

import java.util.List;

import lombok.Data;
import lombok.ToString;

/* created by chao*/
@Data
@ToString
public class HostConfig {

    private boolean ReadonlyRootfs;
    private int DiskQuota;
    private List<String> DnsOptions;
    private List<String> BlkioWeightDevice;
    private int Memory;
    private int CpuPeriod;
    //    private PortBindings PortBindings;
    private List<String> Dns;
    private List<String> Devices;
    private int BlkioWeight;
    private String Runtime;
    private String UsernsMode;
    private String CpusetCpus;
    private int MemoryReservation;
    private String UTSMode;
    private String Cgroup;
    private List<String> DnsSearch;
    private int CpuQuota;
    private int CpuRealtimeRuntime;
    private int CpuShares;
    private String ContainerIDFile;
    private int OomScoreAdj;
    private LogConfig LogConfig;
    private int KernelMemory;
    private boolean OomKillDisable;
    private boolean Privileged;
    private int IOMaximumBandwidth;
    private boolean AutoRemove;
    private int CpuPercent;
    private List<Integer> ConsoleSize;
    private int IOMaximumIOps;
    private String VolumeDriver;
    private int CpuCount;
    private boolean PublishAllPorts;
    private int CpuRealtimePeriod;
    private String IpcMode;
    private List<String> Binds;
    private String CpusetMems;
    private RestartPolicy RestartPolicy;
    private String NetworkMode;
    private String PidMode;
    private String CgroupParent;
    private int NanoCpus;
    private int MemorySwap;
    private String Isolation;
    private int PidsLimit;
    private List<String> SecurityOpt;
    private long ShmSize;


}