package demo.spring.boot.demospringboot.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import demo.spring.boot.demospringboot.service.jpa.Jpa;
import demo.spring.boot.demospringboot.vo.JpaVo;

@Service
public class TransactionalService {

    @Autowired
    private Jpa jpa;


    /**
     * 事务处理
     */
    @Transactional
    public List<JpaVo> transactionalInsert() {
        JpaVo jpaVo = new JpaVo("chao", "123456");
        JpaVo jpaVo2 = new JpaVo("chao", "1234567");//肯定无法成功

        JpaVo resultVo = null;
        JpaVo resultVo2 = null;
        List<JpaVo> jpaVos = new ArrayList<>();
        try {
            resultVo = jpa.save(jpaVo);
            jpaVos.add(resultVo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            resultVo2 = jpa.save(jpaVo2);
            jpaVos.add(resultVo2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jpaVos;


    }
}
