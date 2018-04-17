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

import demo.spring.boot.demospringboot.command.options.stream.Images;
import demo.spring.boot.demospringboot.command.options.stream.Inspect;
import demo.spring.boot.demospringboot.command.options.stream.PS;
import demo.spring.boot.demospringboot.util.SSHUtil;
import demo.spring.boot.demospringboot.util.help.SSHResInfo;
import demo.spring.boot.demospringboot.vo.ContainerVo;
import demo.spring.boot.demospringboot.vo.ImageVo;

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
     * 转换成ImageVo
     */
    public List<ContainerVo> getContainersRunning() throws IOException {
        String cmd =
                new PS().addOPtions(PS.options.__format) + "'{{.ID}}:{{.Image}}:{{.Command}}:{{.Status}}:{{.Ports}}:{{.Names}}'";
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


}
