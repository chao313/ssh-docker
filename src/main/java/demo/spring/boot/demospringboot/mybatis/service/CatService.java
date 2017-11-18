package demo.spring.boot.demospringboot.mybatis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.spring.boot.demospringboot.mybatis.mapper.CatMapper;
import demo.spring.boot.demospringboot.mybatis.vo.Cat;

@Service
public class CatService {

    @Autowired
    private CatMapper mapper;

    public Cat queryById(Integer id) {
        return mapper.queryById(id);
    }


    public Integer insert(Cat cat) {
        return mapper.insert(cat);
    }

    public Integer update(Cat cat) {
        return mapper.updateById(cat);
    }

    public Integer delete(Integer id) {
        return mapper.deleteById(id);
    }

}
