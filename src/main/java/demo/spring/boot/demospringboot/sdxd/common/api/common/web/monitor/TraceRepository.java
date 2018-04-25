package demo.spring.boot.demospringboot.sdxd.common.api.common.web.monitor;

import com.google.common.base.Predicate;

import com.alibaba.dubbo.rpc.RpcException;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.access.AccessEvent;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rpc.RemoteServiceInvocation;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace.HttpAccess;
import demo.spring.boot.demospringboot.sdxd.common.redis.template.RedisClientTemplate;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.function.Function;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.access.AccessEvent.CATEGORY_HTTP;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.access.AccessEvent.CATEGORY_RPC;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.MapUtil.$;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.api.service
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 2017/7/13     melvin                 Created
 */
public class TraceRepository {

    private static final String MONITORING_REQ_KEY = "monitoring:%s:req";
    private static final String MONITORING_RES_KEY = "monitoring:%s:res";
    private static final String MONITORING_ERR_KEY = "monitoring:%s:err";
    private static final String MONITORING_PERIOD_KEY = "monitoring:%s:period";
    private static final String MONITORING_TIMEOUT_KEY = "monitoring:%s:timeout";
    private static final String MONITORING_BLOCKING_KEY = "monitoring:%s:blocking";
    private static final String MONITORING_TOTAL_REQ_KEY = "monitoring:%s:total_req";
    private static final String MONITORING_TOTAL_RES_KEY = "monitoring:%s:total_res";
    private static final String MONITORING_TOTAL_ERR_KEY = "monitoring:%s:total_err";
    private static final String MONITORING_TOTAL_PERIOD_KEY = "monitoring:%s:total_period";

    private static final Map<Predicate<AccessEvent>, String> KEYS = $(
            AccessEvent::in, MONITORING_REQ_KEY,
            AccessEvent::out, MONITORING_RES_KEY,
            AccessEvent::err, MONITORING_ERR_KEY
    );

    private static final Map<Predicate<AccessEvent>, String> EX_KEYS = $(
            TraceRepository::timeout, MONITORING_TIMEOUT_KEY,
            TraceRepository::blocking, MONITORING_BLOCKING_KEY
    );

    private static final Map<Predicate<AccessEvent>, String> TOTAL_KEYS = $(
            AccessEvent::in, MONITORING_TOTAL_REQ_KEY,
            AccessEvent::out, MONITORING_TOTAL_RES_KEY,
            AccessEvent::err, MONITORING_TOTAL_ERR_KEY
    );

    private enum TraceType {
        REQ(MONITORING_REQ_KEY), RES(MONITORING_RES_KEY), ERR(MONITORING_ERR_KEY);

        private String key;

        TraceType(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    private RedisClientTemplate redis;

    public TraceRepository(RedisClientTemplate redis) {
        this.redis = redis;
    }

    public TraceSummary summary(String category) {
        if (StringUtils.isBlank(category)) {
            return null;
        }

        String reqKey = String.format(MONITORING_TOTAL_REQ_KEY, category);
        String resKey = String.format(MONITORING_TOTAL_RES_KEY, category);
        String errKey = String.format(MONITORING_TOTAL_ERR_KEY, category);
        String periodKey = String.format(MONITORING_TOTAL_PERIOD_KEY, category);
        Map<String, String> totalReq = redis.hgetAll(reqKey);
        Map<String, String> totalRes = redis.hgetAll(resKey);
        Map<String, String> totalErr = redis.hgetAll(errKey);
        Map<String, String> totalPeriod = redis.hgetAll(periodKey);
        return new TraceSummary(totalReq, totalRes, totalErr, totalPeriod);
    }

    public Map<String, String> get(String category, String type) {
        if (StringUtils.isBlank(category) || StringUtils.isBlank(type)) {
            return null;
        }
        TraceType traceType = TraceType.valueOf(type.toUpperCase());
        String keyTemplate = traceType.getKey();
        if (StringUtils.isBlank(keyTemplate)) {
            return null;
        }
        String key = String.format(keyTemplate, category.toLowerCase());
        if (redis == null) {
            return null;
        }
        return redis.hgetAll(key);
    }

    public TraceTrouble getTrouble(String category) {
        if (StringUtils.isBlank(category)) {
            return null;
        }
        String periodKey = String.format(MONITORING_PERIOD_KEY, category);
        String timeoutKey = String.format(MONITORING_TIMEOUT_KEY, category);
        Map<String, String> period = redis.hgetAll(periodKey);
        Map<String, String> timeout = redis.hgetAll(timeoutKey);
        if (CATEGORY_HTTP.equalsIgnoreCase(category)) {
            return new TraceTrouble(period, timeout);
        }
        if (CATEGORY_RPC.equalsIgnoreCase(category)) {
            String blockingKey = String.format(MONITORING_BLOCKING_KEY, category);
            Map<String, String> blocking = redis.hgetAll(blockingKey);
            return new TraceTrouble(period, timeout, blocking);
        }
        return null;
    }

    boolean isValid(AccessEvent event) {
        String keyTemplate = getKeys(KEYS, event);
        return keyTemplate != null;
    }

    void cache(AccessEvent event) {
//        log.debug("TRC>BEG|{}", event);

        String keyTemplate = getKeys(KEYS, event);
        if (StringUtils.isBlank(keyTemplate)) {
            return;
        }

        String category = event.getCategory().toLowerCase();
        String timestamp = event.getTimestamp();
        long expireAt = event.getExpireAt();
        String key = String.format(keyTemplate, category);
        String field = String.format("%s|%s", event.getPath(), timestamp);
        if (redis != null) {
            count(redis, key, field, expireAt);
            countOthers(redis, EX_KEYS, event, e -> field);
            countOthers(redis, TOTAL_KEYS, event, AccessEvent::getPath);

            boolean hasHttpPeriod = CATEGORY_HTTP.equalsIgnoreCase(category) && event.out();
            boolean hasRpcPeriod = CATEGORY_RPC.equalsIgnoreCase(category) && (event.out() || event.err());
            if (hasHttpPeriod || hasRpcPeriod) {
                Long period = period(event, hasHttpPeriod, hasRpcPeriod);
                sumPeriod(redis, MONITORING_PERIOD_KEY, event, period, e -> field);
                sumPeriod(redis, MONITORING_TOTAL_PERIOD_KEY, event, period, AccessEvent::getPath);
            }
        }
//        log.debug("TRC>END|{}", event);
    }

    private Long period(AccessEvent event, boolean hasHttpPeriod, boolean hasRpcPeriod) {
        if (hasHttpPeriod) {
            HttpAccess httpAccess = (HttpAccess) event.getContent();
            return httpAccess.getPeriod();
        }
        if (hasRpcPeriod) {
            RemoteServiceInvocation invoke = (RemoteServiceInvocation) event.getContent();
            return invoke.getPeriod();
        }
        return null;
    }

    private static void sumPeriod(
            RedisClientTemplate redis,
            String keyTemplate,
            AccessEvent event,
            Long period,
            Function<AccessEvent, String> fieldFunction) {
        if (period != null) {
            String periodKey = String.format(keyTemplate, event.getCategory().toLowerCase());
            String field = fieldFunction.apply(event);
            String value = redis.hget(periodKey, field);
            long sum = StringUtils.isNotBlank(value) ? Long.parseLong(value) : 0;
            sum += period;
            redis.hset(periodKey, field, String.valueOf(sum));
            redis.expireAt(periodKey, event.getExpireAt());
        }
    }

    private static void countOthers(
            RedisClientTemplate redis,
            Map<Predicate<AccessEvent>, String> keys,
            AccessEvent event,
            Function<AccessEvent, String> fieldFunction) {
        String keyTemplate = getKeys(keys, event);
        if (StringUtils.isNotBlank(keyTemplate)) {
            String key = String.format(keyTemplate, event.getCategory().toLowerCase());
            count(redis, key, fieldFunction.apply(event), event.getExpireAt());
        }
    }

    private static void count(RedisClientTemplate redis, String key, String field, long expireAt) {
        boolean existTotalCounterKey = redis.exists(key);
        redis.hincrBy(key, field, 1);
        if (!existTotalCounterKey) {
            redis.expireAt(key, expireAt);
        }
    }

    private static boolean timeout(AccessEvent event) {
        boolean rpc = CATEGORY_RPC.equals(event.getCategory());
        if (rpc && (event.out() || event.err())) {
            RemoteServiceInvocation invoke = (RemoteServiceInvocation) event.getContent();
            //noinspection ThrowableResultOfMethodCallIgnored
            Throwable t = invoke.getThrowable();
            if (t instanceof RpcException) {
                return ((RpcException) t).isTimeout();
            }
        }
        return false;
    }

    private static String getKeys(Map<Predicate<AccessEvent>, String> keys, AccessEvent event) {
        for (Map.Entry<Predicate<AccessEvent>, String> entry : keys.entrySet()) {
            if (entry.getKey().apply(event)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private static boolean blocking(AccessEvent event) {
        boolean rpc = CATEGORY_RPC.equals(event.getCategory());
        if (rpc && event.in()) {
            Long period = (Long) event.getContent();
            return period > 1000;
        }
        return false;
    }
}
