package demo.spring.boot.demospringboot.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * thymleaf 使用注意点
 * 1.添加pom依赖
 * 2.返回thymleaf的controller必须是@Controller，@RestController会解析成为json
 * 3.2：在Thymeleaf 模板文件中，标签是需要闭合的，3.0之前是需要闭合的
 * 3.2：thymeleaf 3.0+ 是可以不强制要求闭合的。
 * 4.thymeleaf有默认的配置
 */

@Controller
public class ThymeleafController {

    @GetMapping(value = "/thymeleaf")
    public String thymeleaf(){
        return "thymeleaf";
    }
}
