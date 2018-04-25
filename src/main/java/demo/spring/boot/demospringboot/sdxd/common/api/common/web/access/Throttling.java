package demo.spring.boot.demospringboot.sdxd.common.api.common.web.access;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.access.RuleLimit.NONE_RULE_NAME;

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
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Throttling {

    RateLimit rate() default @RateLimit(permitsPerSecond = -1);

    RuleLimit rule() default @RuleLimit(name = NONE_RULE_NAME);
}
