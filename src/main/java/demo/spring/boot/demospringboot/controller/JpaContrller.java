package demo.spring.boot.demospringboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import demo.spring.boot.demospringboot.service.JpaService;
import demo.spring.boot.demospringboot.vo.JpaVo;

@RestController
public class JpaContrller {


    @Autowired
    private JpaService jpaService;


    @RequestMapping(value = "/add")
    public JpaVo add(@RequestParam(value = "name") String name,
                     @RequestParam(value = "password") String password) {

        return jpaService.insert(new JpaVo(name, password));
    }

    @RequestMapping(value = "/findAll")
    public List<JpaVo> add() {

        return jpaService.findAll();
    }


    /**
     * 拓展查询
     */
    @RequestMapping(value = "/findByName/{name}")
    public List<JpaVo> extendJpaFindByName(@PathVariable(value = "name") String name) {

        return jpaService.findByName(name);
    }

    /**
     * 拓展多条件查询
     */
    @RequestMapping(value = "/findByName/{id}/{name}")
    public List<JpaVo> extendJpaFindByNameAndId(@PathVariable(value = "id") Integer id,
                                                @PathVariable(value = "name") String name) {

        return jpaService.findByNameAndId(name, id);
    }
    

}
