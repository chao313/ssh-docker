package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.configure;

import com.alibaba.dubbo.common.utils.Assert;

import java.util.function.Consumer;

import demo.spring.boot.demospringboot.sdxd.common.redis.template.RedisClientTemplate;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.configure
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/3/6     melvin                 Created
 */
public class InCacheConfigure {

    private RedisClientTemplate redis;
    private ConfigureSubscriber subscriber;

    public InCacheConfigure(RedisClientTemplate redis, ConfigureSubscriber subscriber) {
        Assert.notNull(redis, "redis template can not be null.");
        Assert.notNull(subscriber, "configure subscriber can not be null.");
        this.redis = redis;
        this.subscriber = subscriber;
    }

    public void close() {
        this.subscriber.close();
    }

    public void consume(String eventType, Consumer<ConfigureEvent> consumer) {
        this.subscriber.consume(eventType, consumer);
    }

    protected void configureChanged(String key, ConfigureEvent event) {
        this.subscriber.publish(redis, key, event);
    }

    protected RedisClientTemplate getRedis() {
        return redis;
    }
}
