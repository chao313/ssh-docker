package demo.spring.boot.demospringboot.controller.docker.client;

import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.Image;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import demo.spring.boot.demospringboot.constant.AppEnum;
import demo.spring.boot.demospringboot.framework.Code;
import demo.spring.boot.demospringboot.framework.Response;
import demo.spring.boot.demospringboot.service.docker.DockerClientService;
import demo.spring.boot.demospringboot.vo.response.ImageResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 2018/4/21    Created by   chao
 */
@RestController
@RequestMapping(value = "/docker-client/image")
public class DockerClientImageController {

    @Autowired
    private DockerClientService clientService;


    @ApiOperation(value = "inspect-image", notes = "docker inspect images")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")
    })
    @GetMapping(value = "/inspect-image")
    public Response<InspectImageResponse> getInspectImage(
            @RequestParam(value = "docker-host") String dockerHost,
            @RequestParam(value = "image-name") String imageName) {
        Response<InspectImageResponse> response = new Response<>();
        try {
            InspectImageResponse result =
                    clientService.getDockerImageInspect(dockerHost, imageName);
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

    @ApiOperation(value = "images", notes = "docker images")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")
    })
    @GetMapping(value = "/images")
    public Response<List<Image>> getImagesList(
            @RequestParam(value = "docker-host") String dockerHost) {
        Response<List<Image>> response = new Response<>();
        try {
            List<Image> result =
                    clientService.getDockerImagesList(dockerHost);
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

    @ApiOperation(value = "imageResponse", notes = "docker images")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")
    })
    @GetMapping(value = "/images-response")
    public Response<List<ImageResponse>> getImagesResponseList(
            @RequestParam(value = "docker-host") String dockerHost) {
        Response<List<ImageResponse>> response = new Response<>();
        try {
            List<Image> result =
                    clientService.getDockerImagesList(dockerHost);
            List<ImageResponse> list = new ArrayList<>();
            result.stream().forEach(vo -> {
                ImageResponse imageResponse = new ImageResponse();

                BeanUtils.copyProperties(vo, imageResponse);
                list.add(imageResponse);
            });
            list.stream().forEach(vo -> {
                if (null != vo.getRepoTags()[0]) {
                    String app = AppEnum.getApp(vo.getRepoTags()[0]);
                    vo.setApp(app + ".png");
                }
            });
            response.setCode(Code.System.OK);
            response.setMsg(Code.System.SERVER_SUCCESS_MSG);
            response.setContent(list);
        } catch (Exception e) {
            response.setCode(Code.System.FAIL);
            response.setMsg(e.toString());
            response.addException(e);
        }
        return response;
    }


    @ApiOperation(value = "remove images=", notes = "docker rmi ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")
    })
    @GetMapping(value = "/remove-image")
    public Response<Boolean> removeImage(
            @RequestParam(value = "docker-host") String dockerHost,
            @RequestParam(value = "image-id") String imageId
    ) {
        Response<Boolean> response = new Response<>();
        try {
            Boolean result =
                    clientService.removeDockerImage(dockerHost, imageId);
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
