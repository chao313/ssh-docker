package demo.spring.boot.demospringboot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import demo.spring.boot.demospringboot.service.TransactionalService;
import demo.spring.boot.demospringboot.vo.JpaVo;

@RestController
public class TransactionalController {
    @Autowired
    private TransactionalService transactionalInsert;

    /**
     * 事务处理
     */
    @RequestMapping(value = "/transactionalInsert")
    public List<JpaVo> transactionalInsert() {

        return transactionalInsert.transactionalInsert();
    }
}
