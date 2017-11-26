package demo.spring.boot.demospringboot.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageConverterController {

    @GetMapping(value="/converter")
    public String converter(){

        return "哈哈";
    }
}
