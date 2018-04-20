package demo.spring.boot.demospringboot.util;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2018/4/20    Created by   chao
 */
public class DockerClientUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(DockerClientUtil.class);

    public static DockerClient getLoaclClient() {
        DockerClientConfig clientConfig =
                DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerCmdExecFactory dockerCmdExecFactory = new JerseyDockerCmdExecFactory();
        DockerClientImpl dockerClient = DockerClientImpl.getInstance(clientConfig);
        dockerClient.withDockerCmdExecFactory(dockerCmdExecFactory);
        return dockerClient;
    }

    public static DockerClient getRemoteClient() {
        DefaultDockerClientConfig clientConfig =
                DefaultDockerClientConfig.createDefaultConfigBuilder()
                        .withRegistryUsername("root")
                        .withRegistryPassword("Ys15005113872")
                        .withDockerHost("tcp://120.77.37.251:2375")
                        .build();
        DockerCmdExecFactory dockerCmdExecFactory = new JerseyDockerCmdExecFactory();
        DockerClientImpl dockerClient = DockerClientImpl.getInstance(clientConfig);
        dockerClient.withDockerCmdExecFactory(dockerCmdExecFactory);
        return dockerClient;
    }
}
