package demo.spring.boot.demospringboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.spring.boot.demospringboot.vo.PersonInstance;

@RestController
public class YmlController {

    /**
     * 配置文件读取属性
     */
    @Autowired
    private PersonInstance personInstance;

    @Value("${time}")
    private String time;


    @RequestMapping(value = {"/yml1"})
    public PersonInstance yml1() {

        return personInstance;
    }

    @RequestMapping(value = {"/yml2"})
    public String yml2() {

        return time;
    }
}
