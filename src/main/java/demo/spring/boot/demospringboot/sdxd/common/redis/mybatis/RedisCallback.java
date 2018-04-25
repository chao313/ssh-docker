package demo.spring.boot.demospringboot.sdxd.common.redis.mybatis;

import redis.clients.jedis.ShardedJedis;

public interface RedisCallback {

//	Object doWithRedis(Jedis jedis);

	Object doWithRedis(ShardedJedis jedis);
}
