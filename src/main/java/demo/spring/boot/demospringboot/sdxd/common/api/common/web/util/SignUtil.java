package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;



import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import demo.spring.boot.demospringboot.sdxd.common.utils.MD5Utils;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.util
 * 系统名           ：签名算法
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 2017/8/21    wenzhou.xu              Created
 */
public class SignUtil {

    /**
     * 使用MD5对入参升序排序后签名
     * @param t
     * @param key
     * @param <T>
     * @return
     */
    public static <T>String signMD5(T t, String key){
        String stringBuilder;
        //升序
        Map<String, Object> fieldMap = new TreeMap<>(String::compareTo);
        List<Field> fieldList = ClassUtil.getPrivateFieldList(t.getClass());
        fieldList.forEach(field -> fieldMap.put(field.getName(), ClassUtil.getField(t, field)));
        stringBuilder = fieldMap.keySet().stream()
                .map(fieldMap::get)
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .collect(Collectors.joining("", "", key));

        return MD5Utils.encodeByMD5(stringBuilder);
    }
}
