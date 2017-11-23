package demo.spring.boot.demospringboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.spring.boot.demospringboot.redis.RedisCacheService;

@RestController
public class RedisCacheController {

    @Autowired
    private RedisCacheService redisCacheService;

    @GetMapping(value="/redis/set-up-cache")
    public String redisSetupCache(){
        return  redisCacheService.redisSetupCache();
    }

    @GetMapping(value="/redis/clean-up-cache")
    public String redisCleanupCache(){
        return  redisCacheService.redisCleanupCache();
    }
}
