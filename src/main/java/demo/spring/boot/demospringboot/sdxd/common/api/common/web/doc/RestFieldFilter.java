package demo.spring.boot.demospringboot.sdxd.common.api.common.web.doc;

import com.fasterxml.classmate.Filter;
import com.fasterxml.classmate.members.RawField;


import java.lang.reflect.Field;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.ContextParam;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.FieldIgnore;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.doc
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 16/11/2     melvin                 Created
 */
public class RestFieldFilter implements Filter<RawField> {

    @Override
    public boolean include(RawField element) {
        Field field = element.getRawMember();
        return !field.isAnnotationPresent(ContextParam.class) && !field.isAnnotationPresent(FieldIgnore.class);
    }
}
