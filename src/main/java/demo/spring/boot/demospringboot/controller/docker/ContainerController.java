package demo.spring.boot.demospringboot.controller.docker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import demo.spring.boot.demospringboot.framework.Code;
import demo.spring.boot.demospringboot.framework.Response;
import demo.spring.boot.demospringboot.service.docker.DockerService;
import demo.spring.boot.demospringboot.vo.InspectJsonVo;


/**
 * 2018/4/15    Created by   chao
 */
@RestController
@RequestMapping(value = "/container")
public class ContainerController {

    private static Logger LOGGER = LoggerFactory.getLogger(ContainerController.class);

    @Autowired
    private DockerService dockerService;

    @GetMapping(value = "/inspect/{containerName}")
    public Response<InspectJsonVo> getInspect(
            @PathVariable(value = "containerName") String containerName) {
        Response<InspectJsonVo> response = new Response<>();
        try {
            String result = dockerService.getInspectByContainerName(containerName);
            LOGGER.info("ssh结果：{}", result);
            List<InspectJsonVo> inspectVos = ((JSONArray) (JSON.parse(result))).toJavaList(InspectJsonVo.class);
            response.setCode(Code.System.OK);
            response.setMsg(Code.System.SERVER_SUCCESS_MSG);
            response.setContent(inspectVos.get(0));
        } catch (Exception e) {
            response.setCode(Code.System.FAIL);
            response.setMsg(e.toString());
            response.addException(e);
        }
        return response;
    }
}
