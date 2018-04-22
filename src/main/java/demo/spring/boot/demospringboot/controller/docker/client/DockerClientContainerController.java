package demo.spring.boot.demospringboot.controller.docker.client;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.ws.rs.BeanParam;

import demo.spring.boot.demospringboot.framework.Code;
import demo.spring.boot.demospringboot.framework.Response;
import demo.spring.boot.demospringboot.service.docker.DockerClientService;
import demo.spring.boot.demospringboot.vo.request.ContainerRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 2018/4/21    Created by   chao
 */
@RestController
@RequestMapping(value = "/docker-client/container")
public class DockerClientContainerController {

    @Autowired
    private DockerClientService clientService;

    @ApiOperation(value = "containers", notes = "docker ps")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")
    })
    @GetMapping(value = "/containers")
    public Response<List<Container>> getContainerList(
            @RequestParam(value = "docker-host") String dockerHost,
            @RequestParam(value = "all", required = false, defaultValue = "false") Boolean bool
    ) {
        Response<List<Container>> response = new Response<>();
        try {
            List<Container> result =
                    clientService.getDockerContainersList(dockerHost, bool);
            response.setCode(Code.System.OK);
            response.setMsg(Code.System.SERVER_SUCCESS_MSG);
            response.setContent(result);
        } catch (Exception e) {
            response.setCode(Code.System.FAIL);
            response.setMsg(e.toString());
            response.addException(e);
        }
        return response;
    }

    @ApiOperation(value = "inspect-container", notes = "docker inspect containerId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")
    })
    @GetMapping(value = "/inspect-container")
    public Response<InspectContainerResponse> getInspectContainer(
            @RequestParam(value = "docker-host") String dockerHost,
            @RequestParam(value = "container-id") String containerId
    ) {
        Response<InspectContainerResponse> response = new Response<>();
        try {
            InspectContainerResponse result =
                    clientService.getDockerContainerInspect(dockerHost, containerId);
            response.setCode(Code.System.OK);
            response.setMsg(Code.System.SERVER_SUCCESS_MSG);
            response.setContent(result);
        } catch (Exception e) {
            response.setCode(Code.System.FAIL);
            response.setMsg(e.toString());
            response.addException(e);
        }
        return response;
    }

    @ApiOperation(value = "create-container", notes = "docker create ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")
    })
    @GetMapping(value = "/create-container")
    public Response<CreateContainerResponse> createContainer(
            @RequestParam(value = "docker-host") String dockerHost,
            @RequestParam(value = "imageName") String image,
            @BeanParam ContainerRequest request
    ) {
        Response<CreateContainerResponse> response = new Response<>();
        try {
            CreateContainerResponse result =
                    clientService.createContainer(dockerHost, image, request);
            response.setCode(Code.System.OK);
            response.setMsg(Code.System.SERVER_SUCCESS_MSG);
            response.setContent(result);
        } catch (Exception e) {
            response.setCode(Code.System.FAIL);
            response.setMsg(e.toString());
            response.addException(e);
        }
        return response;
    }

    @ApiOperation(value = "remove-container", notes = "docker rm ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")
    })
    @GetMapping(value = "/remove-container")
    public Response<Boolean> createContainer(
            @RequestParam(value = "docker-host") String dockerHost,
            @RequestParam(value = "container-id") String containerId) {
        Response<Boolean> response = new Response<>();
        try {
            Boolean result =
                    clientService.removeContainerById(dockerHost, containerId);
            response.setCode(Code.System.OK);
            response.setMsg(Code.System.SERVER_SUCCESS_MSG);
            response.setContent(result);
        } catch (Exception e) {
            response.setCode(Code.System.FAIL);
            response.setMsg(e.toString());
            response.addException(e);
        }
        return response;
    }

    @ApiOperation(value = "start-container", notes = "docker start ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")
    })
    @GetMapping(value = "/start-container")
    public Response<Boolean> startContainer(
            @RequestParam(value = "docker-host") String dockerHost,
            @RequestParam(value = "container-id") String containerId) {
        Response<Boolean> response = new Response<>();
        try {
            Boolean result =
                    clientService.startContainerById(dockerHost, containerId);
            response.setCode(Code.System.OK);
            response.setMsg(Code.System.SERVER_SUCCESS_MSG);
            response.setContent(result);
        } catch (Exception e) {
            response.setCode(Code.System.FAIL);
            response.setMsg(e.toString());
            response.addException(e);
        }
        return response;
    }

    @ApiOperation(value = "stop-container", notes = "docker stop ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")
    })
    @GetMapping(value = "/stop-container")
    public Response<Boolean> stopContainer(
            @RequestParam(value = "docker-host") String dockerHost,
            @RequestParam(value = "container-id") String containerId) {
        Response<Boolean> response = new Response<>();
        try {
            Boolean result =
                    clientService.stopContainerById(dockerHost, containerId);
            response.setCode(Code.System.OK);
            response.setMsg(Code.System.SERVER_SUCCESS_MSG);
            response.setContent(result);
        } catch (Exception e) {
            response.setCode(Code.System.FAIL);
            response.setMsg(e.toString());
            response.addException(e);
        }
        return response;
    }

    @ApiOperation(value = "kill-container", notes = "docker kill ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")
    })
    @GetMapping(value = "/kill-container")
    public Response<Boolean> killContainer(
            @RequestParam(value = "docker-host") String dockerHost,
            @RequestParam(value = "container-id") String containerId) {
        Response<Boolean> response = new Response<>();
        try {
            Boolean result =
                    clientService.killContainerById(dockerHost, containerId);
            response.setCode(Code.System.OK);
            response.setMsg(Code.System.SERVER_SUCCESS_MSG);
            response.setContent(result);
        } catch (Exception e) {
            response.setCode(Code.System.FAIL);
            response.setMsg(e.toString());
            response.addException(e);
        }
        return response;
    }

    @ApiOperation(value = "restart-container", notes = "docker restart")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")
    })
    @GetMapping(value = "/restart-container")
    public Response<Boolean> restartContainer(
            @RequestParam(value = "docker-host") String dockerHost,
            @RequestParam(value = "container-id") String containerId) {
        Response<Boolean> response = new Response<>();
        try {
            Boolean result =
                    clientService.restartContainerById(dockerHost, containerId);
            response.setCode(Code.System.OK);
            response.setMsg(Code.System.SERVER_SUCCESS_MSG);
            response.setContent(result);
        } catch (Exception e) {
            response.setCode(Code.System.FAIL);
            response.setMsg(e.toString());
            response.addException(e);
        }
        return response;
    }

    @ApiOperation(value = "pause-container", notes = "docker pause")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")
    })
    @GetMapping(value = "/pause-container")
    public Response<Boolean> pauseContainer(
            @RequestParam(value = "docker-host") String dockerHost,
            @RequestParam(value = "container-id") String containerId) {
        Response<Boolean> response = new Response<>();
        try {
            Boolean result =
                    clientService.pauseContainerById(dockerHost, containerId);
            response.setCode(Code.System.OK);
            response.setMsg(Code.System.SERVER_SUCCESS_MSG);
            response.setContent(result);
        } catch (Exception e) {
            response.setCode(Code.System.FAIL);
            response.setMsg(e.toString());
            response.addException(e);
        }
        return response;
    }

    @ApiOperation(value = "unpause-container", notes = "docker pause")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")
    })
    @GetMapping(value = "/unpause-container")
    public Response<Boolean> unpauseContainer(
            @RequestParam(value = "docker-host") String dockerHost,
            @RequestParam(value = "container-id") String containerId) {
        Response<Boolean> response = new Response<>();
        try {
            Boolean result =
                    clientService.unpauseContainerById(dockerHost, containerId);
            response.setCode(Code.System.OK);
            response.setMsg(Code.System.SERVER_SUCCESS_MSG);
            response.setContent(result);
        } catch (Exception e) {
            response.setCode(Code.System.FAIL);
            response.setMsg(e.toString());
            response.addException(e);
        }
        return response;
    }



}
