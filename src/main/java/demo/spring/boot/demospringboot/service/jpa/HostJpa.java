package demo.spring.boot.demospringboot.service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.spring.boot.demospringboot.vo.HostVo;

/**
 * 2018/4/27    Created by   chao
 */
public interface HostJpa extends JpaRepository<HostVo, Integer> {
}
