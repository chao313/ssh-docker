package demo.spring.boot.demospringboot.sdxd.common.utils.validations;



import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import demo.spring.boot.demospringboot.sdxd.common.utils.IdcardValidator;

/**
 * Created by qiuyangjun on 2016/11/10.
 *
 * @packageName:demo.spring.boot.demospringboot.sdxd.common.api.auth.dubbo.api.validations
 * @CreateDate:2016/11/10
 * @UpdateDate:2016/11/10
 * @Description:
 */
public class IdCardValidator implements ConstraintValidator<IdCardValid, String> {
//    private String idNumber;

    @Override
    public void initialize(IdCardValid cardId) {
//        this.idNumber = idNumber.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        IdcardValidator validator = new IdcardValidator();
        return validator.isValidatedAllIdcard(value);
    }
}
