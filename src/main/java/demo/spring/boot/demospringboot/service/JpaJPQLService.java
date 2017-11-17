package demo.spring.boot.demospringboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import demo.spring.boot.demospringboot.service.jpql.JpaJPQL;
import demo.spring.boot.demospringboot.vo.JpaVo;

@Service
public class JpaJPQLService {
    @Autowired
    private JpaJPQL jpaJPQL;

    public Integer add(String name, String password) {
        return jpaJPQL.insertJpql(name, password);
    }

    public void del(Integer id) {
        jpaJPQL.delJpql(id);
    }

    public Integer update(String name, String password, Integer id) {
        return jpaJPQL.updateJpql(name, password, id);
    }

    public List<JpaVo> find(String name) {
        return jpaJPQL.findJpql(name);
    }
}
