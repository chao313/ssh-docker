package demo.spring.boot.demospringboot.service.jpa;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import demo.spring.boot.demospringboot.vo.JpaVo;

/**
 * jpa增删改查
 */
public interface Jpa extends JpaRepository<JpaVo, Integer> {
    /**
     * 拓展的增删改查方法
     * 查询方法以find | read | get开头
     */
    public List<JpaVo> findJpaVosByNameLike(String name);

    public List<JpaVo> getJpaVoByName(String name);

    /**
     * 拓展的增删改查方法,多条件
     */
    public List<JpaVo> findJpaVosByNameAndId(String name, Integer id);


}
