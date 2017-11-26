package demo.spring.boot.demospringboot.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class Logger4jController {

    Logger logger = LoggerFactory.getLogger(Logger4jController.class);

    @GetMapping(value = "/logger")
    public String logger() {

        logger.info("记录日志的时间: {}", new Date().toString());

        return "测试logger4j";

    }
}
