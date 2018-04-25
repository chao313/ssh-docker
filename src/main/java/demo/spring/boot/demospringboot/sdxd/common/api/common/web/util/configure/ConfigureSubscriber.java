package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.configure;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;



import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import demo.spring.boot.demospringboot.sdxd.common.redis.template.RedisClientTemplate;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.util
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/3/3     melvin                 Created
 */
public class ConfigureSubscriber extends BinaryJedisPubSub {

    private static final Logger log = LoggerFactory.getLogger(ConfigureSubscriber.class);

    private byte[] channel;
    private Set<String> subscribedKeys;

    private Map<String, Consumer<ConfigureEvent>> consumers;

    private ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
        String name = "API-Configure-Watcher";
        return new Thread(r, name);
    });

    public ConfigureSubscriber(String channel) {
        this.channel = channel.getBytes();
        this.subscribedKeys = Sets.newHashSet();
        this.consumers = Maps.newHashMap();
    }

    public void publish(RedisClientTemplate redis, String key, ConfigureEvent event) {
        if (!subscribedKeys.contains(key)) {
            executorService.submit(() -> jedis(redis, key).subscribe(this, channel));
            subscribedKeys.add(key);
        }
        jedis(redis, key).publish(channel, event.bytes());
    }

    public void consume(String eventType, Consumer<ConfigureEvent> consumer) {
        if (StringUtils.isBlank(eventType) || consumer == null) {
            return;
        }
        consumers.put(eventType, consumer);
    }

    @Override
    public void onMessage(byte[] channel, byte[] message) {
        ConfigureEvent event = ConfigureEvent.event(message);
        Consumer<ConfigureEvent> consumer = consumers.get(event.getType());
        if (consumer != null) {
            consumer.accept(event);
        }
    }

    public void close() {
        log.debug("Start close configure watcher");
        if (this.isSubscribed()) {
            this.unsubscribe();
            log.debug("Unsubscribe channel");
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
            if (!executorService.isTerminated() || !executorService.isShutdown()) {
                executorService.shutdownNow();
                executorService.awaitTermination(10, TimeUnit.SECONDS);
            }
            log.debug("Close configure watcher complete");
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        } finally {
            if (!executorService.isTerminated()) {
                executorService.shutdownNow();
            }
        }
        if (executorService.isTerminated()) {
            log.debug("Configure watcher closed.");
        }
    }

    private Jedis jedis(RedisClientTemplate redis, String key) {
        ShardedJedis shardedJedis = redis.getRedisDataSource().getRedisClient();
        return shardedJedis.getShard(key);
    }
}
