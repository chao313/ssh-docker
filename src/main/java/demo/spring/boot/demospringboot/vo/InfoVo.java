package demo.spring.boot.demospringboot.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 2018/4/18    Created by   chao
 */
@Data
@ToString
public class InfoVo {
    //容器数量
    private String Containers;
    //运行容器数量
    private String Running;
    //堵塞容器
    private String Paused;
    //停止容器
    private String Stopped;
    //镜像数量
    private String Images;
    //服务版本
    private String Server_Version;
    //存储驱动
    private String Storage_Driver;
    //支持文件系统
    private String Backing_Filesystem;
    //是否支持d_type
    private String Supports_d_type;
    //日志驱动
    //器日志收集的方式通常有以下几种：
    //1、容器外收集。将宿主机的目录挂载为容器的日志目录，然后在宿主机上收集。
    //2、容器内收集。在容器内运行一个后台日志收集服务。
    //3、单独运行日志容器。单独运行一个容器提供共享日志卷，在日志容器中收集日志。
    //4、网络收集。容器内应用将日志直接发送到日志中心，比如java程序可以使用log4j 2转换日志格式并发送到远端
    private String Logging_Driver;
    //Cgroup 驱动
    private String Cgroup_Driver;
    //卷
    private String Volume;
    //网络
    private String Network;
    private String Log;
    //集群
    private String Swarm;
    //Runtimes
    private String Runtimes;
    //默认是Runtimes
    private String Default_Runtime;
    //初始化二进制
    private String Init_Binary;
    //核心版本
    private String Kernel_Version;
    //宿主机操作系统
    private String Operating_System;
    //系统类型
    private String OSType;
    //32位/64位
    private String Architecture;
    //cpu数量
    private String CPUs;
    //total 内存
    private String Total_Memory;
    //名字
    private String Name;
    //id
    private String ID;
    //docker的根目录文件夹
    private String Docker_Root_Dir;
    //客户端debug模式
    private String Debug_Mode_client;
    //service模式
    private String Debug_Mode_server;
    //文件描述
    private String File_Descriptors;
    //系统时间
    private String System_Time;
    //事件监听
    private String EventsListeners;
    //http代理
    private String HTTP_Proxy;
    //https代理
    private String HTTPS_Proxy;
    //注册
    private String Registry;
    //实验性的
    private String Experimental;
    //不稳定的注册地址
    private String Insecure_Registries;
    //用户名
    private String userName;
}
