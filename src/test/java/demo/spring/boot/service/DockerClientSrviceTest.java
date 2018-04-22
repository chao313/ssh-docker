package demo.spring.boot.service;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.PushResponseItem;

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
import demo.spring.boot.demospringboot.service.docker.DockerClientService;
import demo.spring.boot.demospringboot.service.docker.DockerService;
import demo.spring.boot.demospringboot.util.DockerClientUtil;
import demo.spring.boot.demospringboot.util.help.SSHResInfo;
import demo.spring.boot.demospringboot.vo.ImageVo;

/**
 * 2018/4/17    Created by   chao
 */


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = DemoSpringBootApplication.class)
public class DockerClientSrviceTest {

    private Logger LOGGER = LoggerFactory.getLogger(DockerClientSrviceTest.class);

    @Autowired
    private DockerClientService dockerService;

    @Test
    public void pullImage() {
        ResultCallback<PullResponseItem>
                responseItemResultCallback = dockerService.pullImage("120.77.37.251", "nodejs");
        PullResponseItem item = new PullResponseItem();

        responseItemResultCallback.onNext(item);

        LOGGER.info("result:{}", item);
    }
}
