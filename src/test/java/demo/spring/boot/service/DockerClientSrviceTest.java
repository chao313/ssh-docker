package demo.spring.boot.service;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.core.command.AttachContainerResultCallback;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.StringBufferInputStream;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import demo.spring.boot.demospringboot.DemoSpringBootApplication;
import demo.spring.boot.demospringboot.command.options.stream.help.PortMap;
import demo.spring.boot.demospringboot.service.docker.DockerClientService;
import demo.spring.boot.demospringboot.service.docker.DockerService;
import demo.spring.boot.demospringboot.util.DockerClientUtil;
import demo.spring.boot.demospringboot.util.help.SSHResInfo;
import demo.spring.boot.demospringboot.vo.ImageVo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

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

    @Test
    public void attachContainerById() {
        Boolean
                result = dockerService.
                attachContainerById("120.77.37.251", "server-mysql");

        LOGGER.info("result:{}", result);
    }

    @Test
    public void startLog() throws InterruptedException {

//        CreateContainerResponse container = DockerClientUtil.getRemoteClient("120.77.37.251")
//                .createContainerCmd("tomcat")
//                .withCmd("/bin/sh", "-c", "while true; do echo hello; sleep 1; done")
//                .withTty(true)
//                .exec();
//
//        LOGGER.info("Created container: {}", container.toString());
//        assertThat(container.getId(), not(isEmptyString()));

        DockerClientUtil.getRemoteClient("120.77.37.251")
                .startContainerCmd("tomcat_java_2")
                .exec();

        LogContainerTestCallback loggingCallback = new LogContainerTestCallback(true);

        // this essentially test the since=0 case
        DockerClientUtil.getRemoteClient("120.77.37.251").
                logContainerCmd("tomcat_java_2")
                .withStdErr(true)
                .withStdOut(true)
                .withFollowStream(true)
                .withTailAll()
                .exec(loggingCallback);

        loggingCallback.awaitCompletion(3, TimeUnit.SECONDS);
    }




    @Test
    public void attachLog() throws InterruptedException, IOException {

        CreateContainerResponse container = DockerClientUtil.getRemoteClient("120.77.37.251")
                .createContainerCmd("tomcat")
                .withCmd("/bin/sh", "-c", "while true; do echo hello; sleep 1; done")
                .withTty(true)
                .exec();

        InputStream in = new StringBufferInputStream("ls");
        AttachLog resultCallback = new AttachLog();
        DockerClientUtil.getRemoteClient("120.77.37.251")
                .attachContainerCmd("tomcat_java_2")
                .exec(resultCallback);
        byte[] bytes = new byte[1024];
        int i;
        while ((i = in.read(bytes)) != -1) {
            LOGGER.info("日志", new String(bytes));
        }
        resultCallback.awaitCompletion(3, TimeUnit.SECONDS);
    }

}
