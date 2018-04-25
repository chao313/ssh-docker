package demo.spring.boot.demospringboot.sdxd.common.redis.dataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Package Name: com.mobanker.common.redis.dataSource
 * Description:
 * Author: qiuyangjun
 * Create Date:2015/6/11
 */
@Repository("redisDataSource")
public class RedisDataSourceImpl implements  RedisDataSource{
    private static final Logger log = LoggerFactory.getLogger(RedisDataSourceImpl.class);

    @Resource
    private ShardedJedisPool shardedJedisPool;

    @Override
    public ShardedJedisPool getShardedJedisPool(){
        return shardedJedisPool;
    }

    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }

    public ShardedJedis getRedisClient() {
        try {
            ShardedJedis shardJedis = shardedJedisPool.getResource();
            return shardJedis;
        } catch (Exception e) {
            log.error("getRedisClent error", e);
        }
        return null;
    }

    public void returnResource(ShardedJedis shardedJedis) {
        shardedJedisPool.returnResource(shardedJedis);
    }

    public void returnResource(ShardedJedis shardedJedis, boolean broken) {
        if (broken) {
            shardedJedisPool.returnBrokenResource(shardedJedis);
        } else {
            shardedJedisPool.returnResource(shardedJedis);
        }
    }
}
