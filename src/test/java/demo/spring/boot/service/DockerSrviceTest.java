package demo.spring.boot.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import demo.spring.boot.demospringboot.DemoSpringBootApplication;
import demo.spring.boot.demospringboot.command.options.stream.help.PortMap;
import demo.spring.boot.demospringboot.service.docker.DockerService;
import demo.spring.boot.demospringboot.util.help.SSHResInfo;
import demo.spring.boot.demospringboot.vo.ImageVo;

/**
 * 2018/4/17    Created by   chao
 */


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = DemoSpringBootApplication.class)
public class DockerSrviceTest {

    private Logger LOGGER = LoggerFactory.getLogger(DockerSrviceTest.class);

    @Autowired
    private DockerService dockerService;

    @Test
    public void getImagesVo() {
        LOGGER.info("start");
        List<ImageVo> imagesVo = null;
        try {
            imagesVo = dockerService.getImagesVo();
            imagesVo.stream().forEach(vo -> {
                LOGGER.info("vo:{}", vo);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getContainersRunning() {
        LOGGER.info("start");

        try {
            dockerService.getContainersRunning()
                    .stream().forEach(vo -> {
                LOGGER.info("vo:{}", vo);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getContainersAll() {
        LOGGER.info("start");

        try {
            dockerService.getContainersAll()
                    .stream().forEach(vo -> {
                LOGGER.info("vo:{}", vo);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createContainer() {
        LOGGER.info("start");


        SSHResInfo resInfo = dockerService.containerCreate("docker_centos_5");

        LOGGER.info("end:{}", resInfo);

    }

    @Test
    public void createContainerName() {
        LOGGER.info("start");


        SSHResInfo sshResInfo = dockerService.containerCreate("docker_centos_5",
                null, null, "container1");

        LOGGER.info("end:{}", sshResInfo);

    }

    @Test
    public void createContainerPortName() {
        LOGGER.info("start");


        List<PortMap> list = new ArrayList<>();
        list.add(new PortMap(1200, 1200));
        list.add(new PortMap(1300, 1300));

        SSHResInfo sshResInfo = dockerService.containerCreate("docker_centos_5",
                list, null, "container2");

        LOGGER.info("end:{}", sshResInfo);

    }
}
