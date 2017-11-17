package demo.spring.boot.demospringboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import demo.spring.boot.demospringboot.service.JpaJPQLService;
import demo.spring.boot.demospringboot.vo.JpaVo;

@RestController
@RequestMapping(value="/jpql")
public class JPaJPQLController {

    @Autowired
    private JpaJPQLService jpaJPQLService;

    @GetMapping(value = "/find/{name}")
    public List<JpaVo> find(@PathVariable(value = "name") String name) {

        return jpaJPQLService.find(name);


    }

    @PutMapping(value = "/update")
    public Integer update(@RequestBody JpaVo jpaVo) {

        return jpaJPQLService.update(jpaVo.getName(), jpaVo.getPassword(), jpaVo.getId());


    }

    @DeleteMapping(value = "/delete/{id}")
    public void update(@PathVariable(value = "id") Integer id) {

        jpaJPQLService.del(id);


    }

    @PostMapping(value = "/add")
    public Integer add(@RequestBody JpaVo jpaVo) {

        return jpaJPQLService.add(jpaVo.getName(), jpaVo.getPassword());

    }

}
