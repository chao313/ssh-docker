package demo.spring.boot.demospringboot.sdxd.common.utils.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Created by qiuyangjun on 2016/11/10.
 *
 * @packageName:demo.spring.boot.demospringboot.sdxd.common.api.auth.dubbo.api.validations
 * @CreateDate:2016/11/10
 * @UpdateDate:2016/11/10
 * @Description:
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdCardValidator.class)
public @interface IdCardValid {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
