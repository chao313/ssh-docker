package demo.spring.boot.demospringboot.sdxd.common.api.common.web.access;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.interceptor.PreHandler;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.RestContext;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ProcessBizException;

import org.jooq.lambda.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;

import java.util.Map;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.access.RuleLimit.NONE_RULE_NAME;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace.HttpTracer.DEBUG;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode.ApiError.DENIED_BY_RULE;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode.ApiError.REQUEST_TOO_BUSY;

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
 * 17/2/15     melvin                 Created
 */
public class ThrottlingHandler implements PreHandler {

    private static final Logger log = LoggerFactory.getLogger(ThrottlingHandler.class);

    private ThrottlingConfigure configure;

    private Map<String, RateLimiter> limiters = Maps.newHashMap();

    public ThrottlingHandler(ThrottlingConfigure configure) {
        Assert.notNull(configure, "throttling configure can not be null.");
        this.configure = configure;
        this.configure.consume(event -> {
            String api = event.getName();
            Double threshold = Double.parseDouble(event.getValue());
            RateLimiter limiter = getLimiter(api, threshold);
            if (limiter != null && threshold != limiter.getRate()) {
                limiter.setRate(threshold);
                DEBUG(log, "Change api {} throttling complete, new threshold: {}", api, threshold);
            }
        });
    }

    @Override
    public boolean handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HandlerMethod.class.isInstance(handler)) {
            HandlerMethod handlerMethod = HandlerMethod.class.cast(handler);
            Throttling throttlingInType = handlerMethod.getBeanType().getDeclaredAnnotation(Throttling.class);
            Throttling throttling = handlerMethod.getMethodAnnotation(Throttling.class);
            throttling = throttling != null ? throttling : throttlingInType;
            if (throttling == null) {
                return true;
            }

            RateLimit rate = throttling.rate();
            if (rate.permitsPerSecond() > 0) {
                RateLimiter limiter = getLimiter(request, rate);
                if (limiter.tryAcquire()) {
                    return true;
                }
                throw new ProcessBizException(REQUEST_TOO_BUSY);
            }

            RuleLimit rule = throttling.rule();
            if (!NONE_RULE_NAME.equals(rule.name())) {
                Tuple2<Predicate<?>, String> tuple2 = RestContext.getRule(rule.name());
                if (tuple2 == null || tuple2.v1() == null) {
                    return true;
                }
                Predicate<?> predicate = tuple2.v1();
                boolean result = predicate.test(null);
                if (result) {
                    return true;
                }
                throw new ProcessBizException(new ErrorCode(DENIED_BY_RULE.getCode(), tuple2.v2()));
            }
        }
        return true;
    }

    private RateLimiter getLimiter(HttpServletRequest request, RateLimit limit) {
        String key = request.getPathInfo().concat(":").concat(request.getMethod());
        return getLimiter(key, limit.permitsPerSecond());
    }

    private RateLimiter getLimiter(String api, double defaultThreshold) {
        RateLimiter limiter = limiters.get(api);
        if (limiter == null) {
            double threshold = getThreshold(api, defaultThreshold);
            limiter = RateLimiter.create(threshold);
            limiters.put(api, limiter);
        }
        return limiter;
    }

    private double getThreshold(String api, double defaultThreshold) {
        if (configure == null) {
            return defaultThreshold;
        }
        return this.configure.get(api, defaultThreshold);
    }
}
