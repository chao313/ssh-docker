package demo.spring.boot;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.AttachContainerCmd;
import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.command.PingCmd;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.api.model.AuthResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.Version;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.command.AttachContainerResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import demo.spring.boot.demospringboot.DemoSpringBootApplication;
import demo.spring.boot.demospringboot.util.DockerClientUtil;

import static java.awt.SystemColor.info;

/**
 * 2018/4/19    Created by   chao
 */


public class DockerJavaTest {

    private static Logger LOGGER = LoggerFactory.getLogger(DockerJavaTest.class);

    @Test
    public void infoCmd() {
        DockerClient loaclClient = DockerClientUtil.getLoaclClient();
        InfoCmd infoCmd = loaclClient.infoCmd();
        Info info = infoCmd.exec();
        LOGGER.info("result:{}", info);
    }

    @Test
    public void authCmd() {
        DockerClient loaclClient = DockerClientUtil.getLoaclClient();
        AuthCmd cmd = loaclClient.authCmd();
        AuthResponse response = cmd.exec();
        LOGGER.info("result:{}", response);
    }


    @Test
    public void VersionCmd() {
        DockerClient loaclClient = DockerClientUtil.getLoaclClient();
        VersionCmd cmd = loaclClient.versionCmd();
        Version response = cmd.exec();
        LOGGER.info("result:{}", response);
    }


    @Test
    public void eventsCmd() {
        DockerClient loaclClient = DockerClientUtil.getLoaclClient();
        EventsCmd cmd = loaclClient.eventsCmd();
        Map<String, List<String>> filters = cmd.getFilters();
        LOGGER.info("result:{}", filters);
    }


    @Test
    public void ListImagesCmd() {
        DockerClient loaclClient = DockerClientUtil.getLoaclClient();
        ListImagesCmd cmd = loaclClient.listImagesCmd();
        List<Image> response = cmd.withShowAll(true).exec();
        LOGGER.info("result:{}", response);
    }

//    @Test
//    public void PullImagesCmd() {
//        DockerClient loaclClient = DockerClientUtil.getLoaclClient();
//        PullImageCmd tomcat = loaclClient.pullImageCmd("centos");
//        ResultCallback resultCallback = new PullImageResultCallback();
//        tomcat.exec(resultCallback);
//        LOGGER.info("result:{}", tomcat.toString());
//    }


    @Test
    public void listContainersCmd() {
        DockerClient loaclClient = DockerClientUtil.getLoaclClient();
        ListContainersCmd cmd = loaclClient.listContainersCmd();
        cmd.withShowAll(true);//获取所有的
        List<Container> response = cmd.exec();
        LOGGER.info("result:{}", response);
    }

//    @Test m
//    public void AttachContaine() {
//        DockerClient loaclClient = DockerClientUtil.getLoaclClient();
//        AttachContainerCmd cmd = loaclClient.attachContainerCmd("35732165da77");
//        ResultCallback resultCallback = new AttachContainerResultCallback();
//        cmd.exec(resultCallback);
//        resultCallback.onNext();
//        resultCallback.onComplete();
//        LOGGER.info("result:{}", response);
//    }


    @Test
    public void createContainerCmd() {
        DockerClient loaclClient = DockerClientUtil.getLoaclClient();
        CreateContainerCmd cmd = loaclClient.createContainerCmd("mysql");
        cmd.withName("mysql_ssh");
        cmd.withEnv("MYSQL_ROOT_PASSWORD=123456");
        CreateContainerResponse response = cmd.exec();
        LOGGER.info("result:{}", response);
    }

    @Test
    public void StartContainerCmdNyId() {
        DockerClient loaclClient = DockerClientUtil.getLoaclClient();
        StartContainerCmd cmd = loaclClient.startContainerCmd("d569508262d0");
        cmd.exec();
        LOGGER.info("result:{}", "true");
    }

    @Test
    public void StopContainerCmdNyId() {
        DockerClient loaclClient = DockerClientUtil.getLoaclClient();
        StopContainerCmd cmd = loaclClient.stopContainerCmd("d569508262d0");
        cmd.exec();
        LOGGER.info("result:{}", "true");
    }

    @Test
    public void RemoveContainerCmdNyId() {
        DockerClient loaclClient = DockerClientUtil.getLoaclClient();
        RemoveContainerCmd cmd = loaclClient.removeContainerCmd("d569508262d0");
        cmd.exec();
        LOGGER.info("result:{}", "true");
    }
}
