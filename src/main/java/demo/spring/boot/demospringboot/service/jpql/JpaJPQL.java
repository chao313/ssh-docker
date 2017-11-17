package demo.spring.boot.demospringboot.service.jpql;//package com.springboot.service.jpa;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import demo.spring.boot.demospringboot.vo.JpaVo;

/**
 * JPQL 增删改查，
 */

@Component
public interface JpaJPQL extends CrudRepository<JpaVo, Integer> {


    /**
     * jpql里面的表和字段都是 类和属性
     *
     * :后面的就是一个参数
     */
    @Query("select id , name , password from JpaVo where name =:name")
    List<JpaVo> findJpql(@Param(value = "name") String name);

    /**
     * delete
     */
    @Transactional
    @Modifying
    @Query("delete from JpaVo where  id =:id")
    void delJpql(@Param(value = "id") Integer id);

    /**
     * update
     *
     * @Transactional
     * @Modifying 必须要添加这两个注解
     */
    @Transactional
    @Modifying
    @Query("update  JpaVo  set name=:name , password=:password where  id =:id")
    Integer updateJpql(@Param(value = "name") String name,
                       @Param(value = "password") String password,
                       @Param(value = "id") Integer id);

    /**
     * insert
     *
     * jpa_vo :必须是数据库中存在的！，因为是原生的
     *
     * @Transactional
     * @Modifying
     * 必须
     *
     */
    @Transactional
    @Modifying
    @Query(value = "INSERT  into jpa_vo (name , password) value (name=:name,password=:password) ", nativeQuery = true)
    Integer insertJpql(@Param(value = "name") String name,
                       @Param(value = "password") String password);
}
