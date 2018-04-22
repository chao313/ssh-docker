package demo.spring.boot.demospringboot.controller.docker.client;

import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.Version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import demo.spring.boot.demospringboot.framework.Code;
import demo.spring.boot.demospringboot.framework.Response;
import demo.spring.boot.demospringboot.service.docker.DockerClientService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 2018/4/21    Created by   chao
 */
@RestController
@RequestMapping(value = "/docker-client/base")
public class DockerClientBaseController {
    @Autowired
    private DockerClientService clientService;

    @ApiOperation(value = "info", notes = "docker info")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")
    })
    @GetMapping(value = "/info")
    public Response<Info> getInfo(
            @RequestParam(value = "docker-host") String dockerHost) {
        Response<Info> response = new Response<>();
        try {
            Info info = clientService.getDockerInfo(dockerHost);
            response.setCode(Code.System.OK);
            response.setMsg(Code.System.SERVER_SUCCESS_MSG);
            response.setContent(info);
        } catch (Exception e) {
            response.setCode(Code.System.FAIL);
            response.setMsg(e.toString());
            response.addException(e);
        }
        return response;
    }


    @ApiOperation(value = "version", notes = "docker version")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")
    })
    @GetMapping(value = "/version")
    public Response<Version> getVersion(
            @RequestParam(value = "docker-host") String dockerHost) {
        Response<Version> response = new Response<>();
        try {
            Version vsersion = clientService.getDockerVsersion(dockerHost);
            response.setCode(Code.System.OK);
            response.setMsg(Code.System.SERVER_SUCCESS_MSG);
            response.setContent(vsersion);
        } catch (Exception e) {
            response.setCode(Code.System.FAIL);
            response.setMsg(e.toString());
            response.addException(e);
        }
        return response;
    }


}
