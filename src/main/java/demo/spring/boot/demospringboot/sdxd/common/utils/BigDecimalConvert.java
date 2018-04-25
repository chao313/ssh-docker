package demo.spring.boot.demospringboot.sdxd.common.utils;

import org.apache.commons.beanutils.Converter;

/**
 * Created by qiuyangjun on 2015/1/18.
 */
public class BigDecimalConvert implements Converter {

    @Override
    public Object convert(Class type, Object value) {
        if(value==null){
            return null;
        }
        return value;
    }
}
