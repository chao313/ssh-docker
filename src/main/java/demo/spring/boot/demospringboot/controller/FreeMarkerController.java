package demo.spring.boot.demospringboot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

import demo.spring.boot.demospringboot.vo.PersonInstance;

@Controller
public class FreeMarkerController {
    @Autowired
    PersonInstance personInstance;

    @GetMapping(value="/free-marker")
    public String freeMarker(Map<String,Object> map){
        map.put("person",personInstance);
        return "freeMarker";
    }
}
