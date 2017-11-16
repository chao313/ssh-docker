package demo.spring.boot.demospringboot.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import demo.spring.boot.demospringboot.service.jpa.Jpa;
import demo.spring.boot.demospringboot.vo.JpaVo;

@Service
public class JpaService {

    @Autowired
    private Jpa jpa;

    /**
     * jpa 插入
     */
    public JpaVo insert(JpaVo jpaVo) {
        return jpa.save(jpaVo);
    }

    /**
     * jpa 查询
     */
    public List<JpaVo> findAll() {
        return jpa.findAll();
    }

    /**
     * jpa 拓展
     */
    public List<JpaVo> findByName(String name) {
        return jpa.findJpaVosByNameLike(name);
    }

    /**
     * jpa 拓展多接口
     */
    public List<JpaVo> findByNameAndId(String name, Integer id) {
        return jpa.findJpaVosByNameAndId(name, id);
    }


}
