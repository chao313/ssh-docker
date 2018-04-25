package demo.spring.boot.demospringboot.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Arrays;

import demo.spring.boot.demospringboot.sdxd.common.redis.dataSource.RedisDataSource;
import demo.spring.boot.demospringboot.sdxd.common.redis.dataSource.RedisDataSourceImpl;
import demo.spring.boot.demospringboot.sdxd.common.redis.template.RedisClientTemplate;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by cyj on 2017/7/21.
 */
@Configuration
@Order(10)
public class RedisConfig {

    @Value("${spring.redis.db}")
    private int databaseIndex;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.pool.min-idle}")
    private int maxTotal;
    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Bean
    public JedisPoolConfig createJedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(-1);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        return config;
    }

    @Bean
    public JedisShardInfo createJedisShardInfo() {
        String url = String.format("http://%s:%s/%s", host, port, databaseIndex);
        JedisShardInfo info = new JedisShardInfo(url);
        info.setPassword(password);
        return info;
    }

    @Bean
    public ShardedJedisPool createShardedJedisPool(JedisPoolConfig config, JedisShardInfo info) {
        ShardedJedisPool pool = new ShardedJedisPool(config, Arrays.asList(info));
        return pool;
    }

    @Bean
    public RedisDataSource createRedisDataSource(ShardedJedisPool pool) {
        RedisDataSourceImpl redisDataSource = new RedisDataSourceImpl();
        redisDataSource.setShardedJedisPool(pool);
        return redisDataSource;
    }

    @Bean
    public RedisClientTemplate createRedisClientTemplate(RedisDataSource dataSource) {
        RedisClientTemplate redisClientTemplate = new RedisClientTemplate();
        redisClientTemplate.setRedisDataSource(dataSource);
        return redisClientTemplate;
    }

}
