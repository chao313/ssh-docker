package demo.spring.boot.demospringboot.command.options.stream;

import java.util.List;
import java.util.Map;

import demo.spring.boot.demospringboot.command.options.stream.help.Env;
import demo.spring.boot.demospringboot.command.options.stream.help.PortMap;
import io.swagger.annotations.ApiParam;

/**
 * @command docker create [OPTIONS] IMAGE [COMMAND] [ARG...]
 * @description 创建一个新的容器但不启动它
 * @OPTIONS -a stdin: 指定标准输入输出内容类型，可选 STDIN/STDOUT/STDERR 三项
 * @OPTIONS -d: 后台运行容器，并返回容器ID
 * @OPTIONS -i: 以交互模式运行容器，通常与 -t 同时使用
 * @OPTIONS -t: 为容器重新分配一个伪输入终端，通常与 -i 同时使用
 * @OPTIONS --name="nginx-lb": 为容器指定一个名称
 * @OPTIONS --dns 8.8.8.8: 指定容器使用的DNS服务器，默认和宿主一致
 * @OPTIONS --dns-search example.com: 指定容器DNS搜索域名，默认和宿主一致
 * @OPTIONS -h "mars": 指定容器的hostname
 * @OPTIONS -e username="ritchie": 设置环境变量
 * @OPTIONS --env-file=[]: 从指定文件读入环境变量
 * @OPTIONS --cpuset="0-2" or --cpuset="0,1,2": 绑定容器到指定CPU运行
 * @OPTIONS -m :设置容器使用内存最大值
 * @OPTIONS ---net="bridge": 指定容器的网络连接类型，支持 bridge/host/none/container: 四种类型
 * @OPTIONS --link=[]: 添加链接到另一个容器
 * @OPTIONS --expose=[]: 开放一个端口或一组端口
 * @OPTIONS --rm   当容器退出时自动移除容器
 * @
 * @
 * @
 * @OPTIONS -p=[]: 开放一个端口或一组端口
 * @OPTIONS -P: 开放所有端口
 * @NOTICE 和run一样
 */
public class Create {
    public enum options {
        _d(" -d "),
        _i(" -i "),
        _t(" -t "),
        __name(" --name "),
        __dns(" --dns "),
        __dns_search(" --dns-search "),
        _h(" -h "),
        _e(" -e "),
        __env_file(" -- env-file "),
        __cpuset(" --cpuset "),
        _m(" -m "),
        __net(" --net "),
        __link(" --link "),
        __expose(" --expose "),
        _p(" -p ");

        private String option;

        options(String option) {
            this.option = option;
        }
    }


    private String buffer = "docker create ";

    public Create Create(Create.options options) {
        this.buffer = this.buffer.concat(options.option);
        return this;
    }

    public Create addOPtions(Create.options options) {
        this.buffer = this.buffer.concat(options.option);
        return this;
    }

    /**
     * 添加容器名字
     */
    private Create addImageName(String imageName) {
        this.buffer = this.buffer.concat("  "+imageName);
        return this;
    }

    /**
     * 添加容器名字
     */
    private Create addContainerName(String containerNme) {
        this.buffer = this.buffer.concat(options.__name.option).concat(containerNme);
        return this;
    }

    /**
     * 添加主机名字
     */
    private Create addHostName(String hostName) {
        this.buffer = this.buffer.concat(options._h.option).concat(hostName);
        return this;
    }

    /**
     * 添加映射端口号
     */
    private Create addPort(List<PortMap> ports) {
        ports.forEach(portMap -> {
            this.buffer += options._p.option + portMap.getHostPort() + ":" + portMap.getContainerPort();
        });
        return this;
    }

    /**
     * 添加映射端口号
     */
    private Create addEnv(List<Env> envs) {
        envs.forEach(portMap -> {
            this.buffer += options._e.option + portMap.getKey() + ":" + portMap.getValue();
        });
        return this;
    }


    private Create addCmd(String cmd) {
        this.buffer = this.buffer.concat(cmd);
        return this;
    }

    @Override
    public String toString() {
        return this.buffer;
    }

    public Create ContainerCreate(@ApiParam(value = "端口号") List<PortMap> ports,
                                  @ApiParam(value = "附加命令") List<String> cmds,
                                  @ApiParam(value = "容器名字") String containerName,
                                  @ApiParam(value = "主机名字") String hostName,
                                  @ApiParam(value = "环境变量") List<Env> envs,
                                  @ApiParam(value = "镜像名字") String imageName) {
        if (null != ports) {
            this.addPort(ports);
        }

        if (null != containerName) {
            this.addContainerName(containerName);
        }

        if (null != hostName) {
            this.addHostName(hostName);
        }

        if (null != envs) {
            this.addEnv(envs);
        }

        if (null != cmds) {
            //处理命令
            cmds.stream().forEach(cmd -> {
                this.addCmd(cmd);
            });
        }

        if (null != imageName) {
            this.addImageName(imageName);
        }
        return this;

    }


}
