package demo.spring.boot.demospringboot.redis;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Cacheable 的作用 主要针对方法配置，能够根据方法的请求参数对其结果进行缓存
 * @CachePut 的作用 主要针对方法配置，能够根据方法的请求参数对其结果进行缓存，和 @Cacheable 不同的是，它每次都会触发真实方法的调用
 * @CachEvict 的作用 主要针对方法配置，能够根据一定的条件对缓存进行清空
 */


@Service
public class RedisCacheService {

    //设置缓存
    //体现在redis上面的就是加入一个key-value
    //但是！ 默认的过期时间是-1
    @Cacheable(value = "chao:redis:key")
    public String redisSetupCache() {
        return "这是缓存？" + new Date().toString();
    }

    //清除缓存缓存
    @Cacheable(value = "chao:redis:key")
    public String redisCleanupCache() {
        return "这是缓存？" + new Date().toString();
    }
}
