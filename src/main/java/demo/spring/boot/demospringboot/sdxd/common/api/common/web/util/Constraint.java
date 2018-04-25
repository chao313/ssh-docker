package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

import com.google.common.collect.Lists;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ProcessBizException;

import org.jooq.lambda.tuple.Tuple2;

import java.util.List;
import java.util.function.Predicate;

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
 * 17/1/16     melvin                 Created
 */
public class Constraint<T> {

    public static <T> Constraint<T> of(T t) {
        return new Constraint<T>(t);
    }

    private T t;
    private List<Tuple2<Predicate<T>, ErrorCode>> rules;

    private Constraint(T t) {
        this.t = t;
        rules = Lists.newArrayList();
    }

    public Constraint<T> test(Predicate<T> predicate, ErrorCode code) {
        rules.add(new Tuple2<>(predicate, code));
        return this;
    }

    public T get() throws ProcessBizException {
        for (Tuple2<Predicate<T>, ErrorCode> rule : rules) {
            Predicate<T> predicate = rule.v1();
            boolean accept = predicate.test(t);
            if (accept) {
                throw new ProcessBizException(rule.v2);
            }
        }
        return t;
    }
}
