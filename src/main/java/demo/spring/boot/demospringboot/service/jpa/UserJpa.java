package demo.spring.boot.demospringboot.service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import demo.spring.boot.demospringboot.vo.UserVo;

/**
 * 2018/4/23    Created by   chao
 */
public interface UserJpa extends JpaRepository<UserVo, String> {
    UserVo findFirstByDockerPassId(String dockerPassId);

    UserVo findUserVoByDockerPassIdAndPassword(String dockerPassId, String password);


    @Query("select salt from UserVo where dockerPassId =:dockerPassId")
    String findSaltDockerPassId(@Param(value = "dockerPassId") String dockerPassId);


}
