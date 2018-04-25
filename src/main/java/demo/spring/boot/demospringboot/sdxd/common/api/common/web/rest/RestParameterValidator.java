package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest;

import java.util.function.BiPredicate;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/1/23     melvin                 Created
 */
public class RestParameterValidator {

    public static RestParameterValidator rule(BiPredicate<String, RestParameterContext> rule, String message) {
        return new RestParameterValidator(rule, message);
    }

    private BiPredicate<String, RestParameterContext> rule;
    private String message;

    private RestParameterValidator(BiPredicate<String, RestParameterContext> rule, String message) {
        this.rule = rule;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean validate(String value, RestParameterContext context) {
        return rule.test(value, context);
    }
}
