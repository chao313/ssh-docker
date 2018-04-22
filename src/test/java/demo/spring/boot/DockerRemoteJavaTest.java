package demo.spring.boot;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.api.model.AuthResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.Version;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import demo.spring.boot.demospringboot.util.DockerClientUtil;

/**
 * 2018/4/19    Created by   chao
 */


public class DockerRemoteJavaTest {

    private static Logger LOGGER = LoggerFactory.getLogger(DockerRemoteJavaTest.class);
    private static DockerClient remoteClient = DockerClientUtil.getRemoteClient();

    @Test
    public void infoCmd() {

        InfoCmd infoCmd = remoteClient.infoCmd();
        Info info = infoCmd.exec();
        LOGGER.info("result:{}", info);
    }

    @Test
    public void authCmd() {

        AuthCmd cmd = remoteClient.authCmd();
        AuthResponse response = cmd.exec();
        LOGGER.info("result:{}", response);
    }


    @Test
    public void VersionCmd() {

        VersionCmd cmd = remoteClient.versionCmd();
        Version response = cmd.exec();
        LOGGER.info("result:{}", response);
    }


    @Test
    public void eventsCmd() {

        EventsCmd cmd = remoteClient.eventsCmd();
        Map<String, List<String>> filters = cmd.getFilters();
        LOGGER.info("result:{}", filters);
    }


    @Test
    public void ListImagesCmd() {

        ListImagesCmd cmd = remoteClient.listImagesCmd();
        List<Image> response = cmd.withShowAll(true).exec();
        LOGGER.info("result:{}", response);
    }

//    @Test
//    public void PullImagesCmd() {
//        
//        PullImageCmd tomcat = loaclClient.pullImageCmd("centos");
//        ResultCallback resultCallback = new PullImageResultCallback();
//        tomcat.exec(resultCallback);
//        LOGGER.info("result:{}", tomcat.toString());
//    }


    @Test
    public void listContainersCmd() {

        ListContainersCmd cmd = remoteClient.listContainersCmd();
        cmd.withShowAll(true);//获取所有的
        List<Container> response = cmd.exec();
        LOGGER.info("result:{}", response);
    }

//    @Test m
//    public void AttachContaine() {
//        
//        AttachContainerCmd cmd = loaclClient.attachContainerCmd("35732165da77");
//        ResultCallback resultCallback = new AttachContainerResultCallback();
//        cmd.exec(resultCallback);
//        resultCallback.onNext();
//        resultCallback.onComplete();
//        LOGGER.info("result:{}", response);
//    }


    @Test
    public void createContainerCmd() {

        CreateContainerCmd cmd = remoteClient.createContainerCmd("mysql");
        cmd.withName("mysql_ssh");
        cmd.withEnv("MYSQL_ROOT_PASSWORD=123456");
        CreateContainerResponse response = cmd.exec();
        LOGGER.info("result:{}", response);
    }

    @Test
    public void StartContainerCmdNyId() {

        StartContainerCmd cmd = remoteClient.startContainerCmd("d569508262d0");
        cmd.exec();
        LOGGER.info("result:{}", "true");
    }

    @Test
    public void StopContainerCmdNyId() {

        StopContainerCmd cmd = remoteClient.stopContainerCmd("d569508262d0");
        cmd.exec();
        LOGGER.info("result:{}", "true");
    }

    @Test
    public void RemoveContainerCmdNyId() {

        RemoveContainerCmd cmd = remoteClient.removeContainerCmd("d569508262d0");
        cmd.exec();
        LOGGER.info("result:{}", "true");
    }


}
