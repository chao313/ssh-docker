package demo.spring.boot.demospringboot.mybatis.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import demo.spring.boot.demospringboot.mybatis.vo.Cat;

@Component
public interface CatMapper {

    @Select(value = "select * from t_cat where id = #{id}")
    Cat queryById(@Param(value = "id") Integer idParam);

    /**
     * insert sql 语句可以返回int 这里可以直接接受
     */
    @Insert(value = "insert into t_cat (name , age) values(#{name},#{age})")
    Integer insert(Cat cat);

    /**
     * 返回更新成功的数量
     */
    @Update(value = "update t_cat set name=#{name},age=${age} where id = ${id}")
    Integer updateById(Cat cat);

    @Delete(value = "delete from t_cat where id = #{id}")
    Integer deleteById(@Param(value = "id") Integer id);
}
