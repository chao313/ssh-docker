package demo.spring.boot.demospringboot.service.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.AttachContainerCmd;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectImageCmd;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.command.KillContainerCmd;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.command.PauseContainerCmd;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.api.command.RemoveImageCmd;
import com.github.dockerjava.api.command.RestartContainerCmd;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.api.command.UnpauseContainerCmd;
import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.api.model.StreamType;
import com.github.dockerjava.api.model.Version;
import com.github.dockerjava.core.async.ResultCallbackTemplate;
import com.github.dockerjava.core.command.AttachContainerResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import demo.spring.boot.demospringboot.util.DockerClientUtil;
import demo.spring.boot.demospringboot.vo.request.ContainerRequest;

/**
 * 2018/4/20    Created by   chao
 */
@Service
public class DockerClientService {

    private static Logger LOGGER = LoggerFactory.getLogger(DockerClientService.class);

    /**
     * 获取docker的info
     */
    public Info getDockerInfo(String dockerHost) {
        LOGGER.info("连接主机ip:{}", dockerHost);
        DockerClient dockerClient
                = DockerClientUtil.getRemoteClient(dockerHost);
        InfoCmd infoCmd = dockerClient.infoCmd();
        Info info = infoCmd.exec();
        LOGGER.info("result:{}", info);
        return info;
    }

    /**
     * 获取docker的vsersion
     */
    public Version getDockerVsersion(String dockerHost) {
        LOGGER.info("连接主机ip:{}", dockerHost);
        DockerClient dockerClient
                = DockerClientUtil.getRemoteClient(dockerHost);
        VersionCmd cmd = dockerClient.versionCmd();
        Version version = cmd.exec();
        LOGGER.info("result:{}", version);
        return version;
    }

    /**
     * 获取image的inspect
     */
    public InspectImageResponse getDockerImageInspect(String dockerHost, String image) {
        DockerClient dockerClient
                = DockerClientUtil.getRemoteClient(dockerHost);
        InspectImageCmd cmd = dockerClient.inspectImageCmd(image);
        InspectImageResponse response = cmd.exec();
        LOGGER.info("result:{}", response);
        return response;
    }

    /**
     * 获取docker的images
     */
    public List<Image> getDockerImagesList(String dockerHost) {
        DockerClient dockerClient
                = DockerClientUtil.getRemoteClient(dockerHost);
        ListImagesCmd cmd = dockerClient.listImagesCmd();
        List<Image> response = cmd.exec();
        LOGGER.info("result:{}", response);
        return response;
    }

    /**
     * 获取docker的images
     */
    public Boolean removeDockerImage(String dockerHost, String imageId) {
        DockerClient dockerClient
                = DockerClientUtil.getRemoteClient(dockerHost);
        RemoveImageCmd cmd = dockerClient.removeImageCmd(imageId);
        cmd.exec();
        LOGGER.info("result:{}", true);
        return true;
    }

    /**
     * 获取docker的images
     */
    public ResultCallback<PullResponseItem> pullImage(String dockerHost, String imageName) {
        DockerClient dockerClient
                = DockerClientUtil.getRemoteClient(dockerHost);
        PullImageCmd cmd = dockerClient.pullImageCmd(imageName);
        ResultCallback pullImageResultCallback = new PullImageResultCallback();
        cmd.exec(pullImageResultCallback);
        LOGGER.info("result:{}", pullImageResultCallback);
        return pullImageResultCallback;
    }

    /**
     * 获取docker的container的list
     */
    public List<Container> getDockerContainersList(String dockerHost, Boolean bool) {
        DockerClient dockerClient
                = DockerClientUtil.getRemoteClient(dockerHost);
        ListContainersCmd cmd = dockerClient.listContainersCmd();
        cmd.withShowAll(bool);
        List<Container> response = cmd.exec();
        LOGGER.info("result:{}", response);
        return response;
    }

    /**
     * 获取docker的container的inspect
     */
    public InspectContainerResponse getDockerContainerInspect(String dockerHost, String containerId) {
        DockerClient dockerClient
                = DockerClientUtil.getRemoteClient(dockerHost);
        InspectContainerCmd cmd = dockerClient.inspectContainerCmd(containerId);
        InspectContainerResponse response = cmd.exec();
        LOGGER.info("result:{}", response);
        return response;
    }

    /**
     * 创建 container
     */
    public CreateContainerResponse createContainer(String dockerHost,
                                                   String image,
                                                   ContainerRequest requst) {
        DockerClient dockerClient
                = DockerClientUtil.getRemoteClient(dockerHost);
        CreateContainerCmd cmd = dockerClient.createContainerCmd(image);
        this.setConfig(cmd, requst);
        CreateContainerResponse response = cmd.exec();
        LOGGER.info("result:{}", response);
        return response;
    }


    /**
     * start container
     */
    public Boolean startContainerById(String dockerHost,
                                      String containerId) {
        DockerClient dockerClient
                = DockerClientUtil.getRemoteClient(dockerHost);
        StartContainerCmd cmd = dockerClient.startContainerCmd(containerId);
        cmd.exec();
        LOGGER.info("result:{}", true);
        return true;
    }

    /**
     * remove container
     */
    public Boolean stopContainerById(String dockerHost,
                                     String containerId) {
        DockerClient dockerClient
                = DockerClientUtil.getRemoteClient(dockerHost);
        StopContainerCmd cmd = dockerClient.stopContainerCmd(containerId);
        cmd.exec();
        LOGGER.info("result:{}", true);
        return true;
    }

    /**
     * remove container
     */
    public Boolean removeContainerById(String dockerHost,
                                       String containerId) {
        DockerClient dockerClient
                = DockerClientUtil.getRemoteClient(dockerHost);
        RemoveContainerCmd cmd = dockerClient.removeContainerCmd(containerId);
        cmd.exec();
        LOGGER.info("result:{}", true);
        return true;
    }

    /**
     * kill container
     */
    public Boolean killContainerById(String dockerHost,
                                     String containerId) {
        DockerClient dockerClient
                = DockerClientUtil.getRemoteClient(dockerHost);
        KillContainerCmd cmd = dockerClient.killContainerCmd(containerId);
        cmd.exec();
        LOGGER.info("result:{}", true);
        return true;
    }

    /**
     * restart container
     */
    public Boolean restartContainerById(String dockerHost,
                                        String containerId) {
        DockerClient dockerClient
                = DockerClientUtil.getRemoteClient(dockerHost);
        RestartContainerCmd cmd = dockerClient.restartContainerCmd(containerId);
        cmd.exec();
        LOGGER.info("result:{}", true);
        return true;
    }

    /**
     * pause container
     */
    public Boolean pauseContainerById(String dockerHost,
                                      String containerId) {
        DockerClient dockerClient
                = DockerClientUtil.getRemoteClient(dockerHost);
        PauseContainerCmd cmd = dockerClient.pauseContainerCmd(containerId);
        cmd.exec();
        LOGGER.info("result:{}", true);
        return true;
    }

    /**
     * unpause container
     */
    public Boolean unpauseContainerById(String dockerHost,
                                        String containerId) {
        DockerClient dockerClient
                = DockerClientUtil.getRemoteClient(dockerHost);
        UnpauseContainerCmd cmd = dockerClient.unpauseContainerCmd(containerId);
        cmd.exec();
        LOGGER.info("result:{}", true);
        return true;
    }

    /**
     * unpause container
     */
    public Boolean attachContainerById(String dockerHost,
                                       String containerId) {
        DockerClient dockerClient
                = DockerClientUtil.getRemoteClient(dockerHost);
        AttachContainerCmd cmd = dockerClient.attachContainerCmd(containerId);
        AttachContainerResultCallback resultCallback = new AttachContainerResultCallback();
        resultCallback = cmd.exec(resultCallback);
        byte[] payload = new byte[1024];
        Frame frame = new Frame(StreamType.STDOUT, payload);
        resultCallback.onNext(frame);

        LOGGER.info("result:{}", true);
        return true;
    }

    private void setConfig(CreateContainerCmd cmd, ContainerRequest request) {
        //指定标准输入输出内容类型，可选 STDIN/STDOUT/STDERR 三项
        //默认为全选
        if (StringUtils.isNotBlank(request.getName())) {
            cmd.withName(request.getName());
        }
        //主机名
        if (StringUtils.isNotBlank(request.getHostName())) {
            cmd.withHostName(request.getHostName());
        }
        //域名
        if (StringUtils.isNotBlank(request.getDomainName())) {
            cmd.withDomainName(request.getDomainName());
        }

        if (StringUtils.isNotBlank(request.getUser())) {
            cmd.withUser(request.getUser());
        }

        if (null != request.getAttachStdin()) {
            cmd.withStdInOnce(request.getAttachStdin());
        }

        if (null != request.getAttachStdout()) {
            cmd.withAttachStdout(request.getAttachStdout());
        }

        if (null != request.getAttachStderr()) {
            cmd.withAttachStderr(request.getAttachStderr());
        }

        if (ArrayUtils.isNotEmpty(request.getPortSpecs())) {
            cmd.withPortSpecs(request.getPortSpecs());
        }

        if (null != request.getTty()) {
            cmd.withTty(request.getTty());
        }

        if (null != request.getStdinOpen()) {
            cmd.withStdinOpen(request.getStdinOpen());
        }

        if (null != request.getStdInOnce()) {
            cmd.withStdInOnce(request.getStdInOnce());
        }

        if (ArrayUtils.isNotEmpty(request.getEnv())) {
            cmd.withEnv(request.getEnv());
        }

        if (ArrayUtils.isNotEmpty(request.getCmd())) {
            cmd.withCmd(request.getCmd());
        }

        if (ArrayUtils.isNotEmpty(request.getEntrypoint())) {
            cmd.withEntrypoint(request.getEntrypoint());
        }

        if (StringUtils.isNotBlank(request.getImage())) {
            cmd.withImage(request.getImage());
        }

        if (null != request.getVolumes()) {
            cmd.withVolumes(request.getVolumes().getVolumes());
        }

        if (StringUtils.isNotBlank(request.getWorkingDir())) {
            cmd.withWorkingDir(request.getWorkingDir());
        }

        if (StringUtils.isNotBlank(request.getMacAddress())) {
            cmd.withMacAddress(request.getMacAddress());
        }

        if (null != request.getNetworkDisabled()) {
            cmd.withNetworkDisabled(request.getNetworkDisabled());
        }

        if (null != request.getExposedPorts()) {
            cmd.withExposedPorts(request.getExposedPorts().getExposedPorts());
        }

        if (StringUtils.isNotBlank(request.getStopSignal())) {
            cmd.withStopSignal(request.getStopSignal());
        }

        if (null != request.getHostConfig()) {
            cmd.withHostConfig(request.getHostConfig());
        }

        if (null != request.getLabels()) {
            cmd.withLabels(request.getLabels());
        }

        if (null != request.getNetworkingConfig()) {
            cmd.withNetworkDisabled(request.getNetworkDisabled());
        }

        if (StringUtils.isNotBlank(request.getIpv4Address())) {
            cmd.withIpv4Address(request.getIpv4Address());
        }

        if (StringUtils.isNotBlank(request.getIpv6Address())) {
            cmd.withIpv6Address(request.getIpv6Address());
        }
        if (null != request.getAliases()) {
            cmd.withAliases(request.getAliases());
        }


    }
}
