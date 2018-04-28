package demo.spring.boot.demospringboot.controller.docker;


import com.github.dockerjava.api.model.Info;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import demo.spring.boot.demospringboot.service.docker.DockerClientService;

@Controller
@RequestMapping(value = "/charts")
public class ChartsController {

    private static Logger LOGGER = LoggerFactory.getLogger(ChartsController.class);
    @Autowired
    private DockerClientService dockerClientService;

    @GetMapping(value = "/look")
    public String freeMarker(Map<String, Object> map, @RequestParam(value = "host-ip") String hostIp) {
        Info info = dockerClientService.getDockerInfo(hostIp);
        map.put("info", info);
        LOGGER.info("info:{}", info);
        return "look-charts";
    }
}

