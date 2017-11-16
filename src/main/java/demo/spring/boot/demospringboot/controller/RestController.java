package demo.spring.boot.demospringboot.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @RestController
 * 和@Controller 加上 @ResponseBody 一样
 */
@org.springframework.web.bind.annotation.RestController
public class RestController {


    /**
     * 同时两个url
     */
    @RequestMapping(value = {"/hello", "/hi"})
    public String hello() {

        return "哈喽";
    }

    /**
     * 获取路径中的变量
     */
    @RequestMapping(value = {"/hello/{id}", "/{id}/hi"})
    public Integer hello2(@PathVariable(value = "id")
                                  Integer idPathVar) {
        return idPathVar;
    }

    /**
     * 获取参数?id=xxx 可以设置默认值 http://localhost:8081/chao/hi2 --> -1 http://localhost:8081/chao/hi2?id=
     * --> -1
     */
    @RequestMapping(value = {"/hello2", "/hi2"})
    public Integer hello3(@RequestParam(value = "id", required = false, defaultValue = "-1")
                                  Integer idParam) {
        return idParam;
    }

    /**
     * 组合mapping
     * 不能组合使用，会405禁止访问
     * @GetMapping(value = "map")
     * @PostMapping(value = "map")
     * @PutMapping(value = "map")
     * @DeleteMapping(value = "map")
     */
    @GetMapping(value = "map")
    public Integer hello4() {

        return 1;

    }


}
