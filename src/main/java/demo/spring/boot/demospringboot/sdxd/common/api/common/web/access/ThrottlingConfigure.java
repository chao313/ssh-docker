package demo.spring.boot.demospringboot.sdxd.common.api.common.web.access;

import demo.spring.boot.demospringboot.sdxd.common.redis.template.RedisClientTemplate;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.configure.ConfigureEvent;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.configure.ConfigureSubscriber;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Consumer;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.configure.InCacheConfigure;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.access
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/2/20     melvin                 Created
 */
public class ThrottlingConfigure extends InCacheConfigure {

    private static final Logger log = LoggerFactory.getLogger(ThrottlingConfigure.class);

    private static final String THROTTLING_THRESHOLD_KEY = "throttling:threshold";
    private static final String THROTTLING_UPDATE_EVENT_TYPE = "throttling-threshold-changed";

    public ThrottlingConfigure(RedisClientTemplate redis, ConfigureSubscriber subscriber) {
        super(redis, subscriber);
    }

    public Map<String, String> getAll() {
        return getRedis().hgetAll(THROTTLING_THRESHOLD_KEY);
    }

    public Double get(String api, double defaultValue) {
        String value = getRedis().hget(THROTTLING_THRESHOLD_KEY, api);
        if (StringUtils.isBlank(value)) {
            put(api, defaultValue, false);
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public void put(String api, double threshold) {
        put(api, threshold, true);
    }

    void consume(Consumer<ConfigureEvent> consumer) {
        this.consume(THROTTLING_UPDATE_EVENT_TYPE, consumer);
    }

    private void put(String api, double threshold, boolean notify) {
        String value = String.valueOf(threshold);
        getRedis().hset(THROTTLING_THRESHOLD_KEY, api, value);
        if (notify) {
            configureChanged(THROTTLING_THRESHOLD_KEY, new ConfigureEvent(THROTTLING_UPDATE_EVENT_TYPE, api, value));
        }
    }
}
