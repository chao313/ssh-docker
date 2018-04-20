package demo.spring.boot.demospringboot.service.docker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import demo.spring.boot.demospringboot.command.options.stream.Base;
import demo.spring.boot.demospringboot.command.options.stream.Create;
import demo.spring.boot.demospringboot.command.options.stream.Images;
import demo.spring.boot.demospringboot.command.options.stream.Inspect;
import demo.spring.boot.demospringboot.command.options.stream.PS;
import demo.spring.boot.demospringboot.command.options.stream.help.Env;
import demo.spring.boot.demospringboot.command.options.stream.help.PortMap;
import demo.spring.boot.demospringboot.util.SSHUtil;
import demo.spring.boot.demospringboot.util.help.SSHResInfo;
import demo.spring.boot.demospringboot.vo.ContainerVo;
import demo.spring.boot.demospringboot.vo.ImageVo;
import demo.spring.boot.demospringboot.vo.InfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Info;

/**
 * 2018/4/15    Created by   chao
 */
@Service
public class DockerService {

    private Logger LOGGER = LoggerFactory.getLogger(DockerService.class);

    @Autowired
    private SSHUtil sshUtil;

    public String getInspectByContainerName(String containerName) {
        String cmd =
                new Inspect(containerName).toString();
        SSHResInfo resInfo = sshUtil.execute(cmd);
        return resInfo.getOutRes();
    }


    /**
     * 转换成ImageVo
     */
    public List<ImageVo> getImagesVo() throws IOException {
        String cmd =
                new Images().addOPtions(Images.options.__format).toString() + "'{{.Repository}}:{{.Tag}}:{{.ID}}:{{.Size}}'";
        SSHResInfo resInfo = sshUtil.execute(cmd);
        LOGGER.info("原始数据:{}", resInfo.getOutRes());
        LineNumberReader reader = new LineNumberReader(new StringReader(resInfo.getOutRes()));
        List<ImageVo> list = new ArrayList<>();
        String result;
        while (null != (result = reader.readLine())) {
            ImageVo imageVo = new ImageVo();
            String[] split = result.split(":");
            imageVo.setREPOSITORY(split[0]);
            imageVo.setTAG(split[1]);
            imageVo.setIMAGE_ID(split[2]);
            imageVo.setSIZE(split[3]);
            list.add(imageVo);
        }
        LOGGER.info("list数据:{}", list);
        return list;
    }

    /**
     * 获取ContainerVo
     */
    public List<ContainerVo> getContainers(List<PS.options> options) throws IOException {
        PS ps = new PS();
        if (null != options) {
            options.stream().forEach(option -> {
                ps.addOPtions(option);
            });
        }
        String cmd = ps.addOPtions(PS.options.__format) + "'{{.ID}}:{{.Image}}:{{.Command}}:{{.Status}}:{{.Ports}}:{{.Names}}'";
        SSHResInfo resInfo = sshUtil.execute(cmd);
        LOGGER.info("原始数据:{}", resInfo.getOutRes());
        LineNumberReader reader = new LineNumberReader(new StringReader(resInfo.getOutRes()));
        List<ContainerVo> list = new ArrayList<>();
        String result;
        while (null != (result = reader.readLine())) {
            ContainerVo containerVo = new ContainerVo();
            String[] split = result.split(":");
            containerVo.setCONTAINER_ID(split[0]);
            containerVo.setIMAGE(split[1]);
            containerVo.setCOMMAND(split[2]);
            containerVo.setSTATUS(split[3]);
            containerVo.setPORTS(split[4]);
            containerVo.setNAMES(split[5]);
            list.add(containerVo);
        }
        LOGGER.info("list数据:{}", list);
        return list;
    }

    /**
     * 获取正在运行的容器
     */
    public List<ContainerVo> getContainersRunning() throws IOException {
        return this.getContainers(null);
    }

    /**
     * 获取正在运行的容器
     */
    public List<ContainerVo> getContainersAll() throws IOException {
        List<PS.options> options = new ArrayList<>();
        options.add(PS.options._a);
        return this.getContainers(options);
    }

    /**
     * 创建容器
     */
    public SSHResInfo containerCreate(
            @ApiParam(value = "镜像名字") String imageName,
            @ApiParam(value = "端口号") List<PortMap> ports,
            @ApiParam(value = "附加命令") List<String> cmds,
            @ApiParam(value = "容器名字") String containerName,
            @ApiParam(value = "主机名字") String hostName,
            @ApiParam(value = "环境变量") List<Env> envs) {
        Create create = new Create();
        create.ContainerCreate(ports, cmds, containerName, hostName, envs, imageName);
        String cmd = create.toString();
        return sshUtil.execute(cmd);

    }

    public SSHResInfo containerCreate(String imageName, List<PortMap> ports, List<String> cmds, String containerName,
                                      String hostName) {
        return this.containerCreate(imageName, ports, cmds, containerName, hostName, null);

    }

    public SSHResInfo containerCreate(String imageName, List<PortMap> ports, List<String> cmds, String containerName) {
        return this.containerCreate(imageName, ports, cmds, containerName, null, null);

    }

    public SSHResInfo containerCreate(String imageName, List<PortMap> ports, List<String> cmds) {
        return this.containerCreate(imageName, ports, cmds, null, null, null);

    }

    public SSHResInfo containerCreate(String imageName, List<PortMap> ports) {
        return this.containerCreate(imageName, ports, null, null, null, null);

    }

    public SSHResInfo containerCreate(String imageName) {
        return this.containerCreate(imageName, null, null, null, null, null);

    }


//    public InfoVo getInfoVo(){
//
//        String cmd = new Base().toString();
//        sshUtil.execute()
//
//    }

}
