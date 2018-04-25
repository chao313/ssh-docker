package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

import com.google.common.collect.Maps;



import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

import demo.spring.boot.demospringboot.sdxd.common.redis.template.RedisClientTemplate;
import rx.Observable;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConvertUtil.getGenericType;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ConvertUtil.toBase;


/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.cache
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 16/12/20     melvin                 Created
 */
public abstract class Cache {

    private RedisClientTemplate redis;

    public Cache(RedisClientTemplate redis) {
        this.redis = redis;
    }

    public void putBean(String key, Object bean) {
        Map<String, String> values = Maps.newHashMap();
        ClassUtil.access(bean, null, clazz -> clazz == Object.class, this::shouldSkip, (object, field, value) -> {
            Object fieldValue = ClassUtil.getProperty(object, field);
            if (fieldValue == null) return false;
            putValue(values, field.getType(), field.getName(), fieldValue);
            return false;
        });
        if (values.size() == 0) return;
        getRedis().hmset(key, values);
    }

    public <T> T getBean(String key, Class<T> type) {
        Map<String, String> values = getRedis().hgetAll(key);
        Object bean = ClassUtil.newInstance(type);
        if (bean == null) {
            return null;
        }
        ClassUtil.access(bean, null, clazz -> clazz == Object.class, this::shouldSkip, (object, field, value) -> {
            getValue(values, bean, field);
            return false;
        });
        //noinspection unchecked
        return (T) bean;
    }

    protected RedisClientTemplate getRedis() {
        return redis;
    }

    private boolean shouldSkip(Field field) {
        boolean ignore = field.isAnnotationPresent(FieldIgnore.class);
        return ignore || (!ConvertUtil.isBaseType(field.getType()) && !ConvertUtil.isMap(field.getType()));
    }

    private void putValue(Map<String, String> values, Class<?> fieldType, String fieldName, Object fieldValue) {
        if (ConvertUtil.isMap(fieldType)) {
            //noinspection unchecked
            Map<String, Object> map = (Map<String, Object>) fieldValue;
            map.entrySet().forEach(entry -> {
                String key = entry.getKey();
                String newKey = String.format("%s:%s", fieldName, key);
                Object value = entry.getValue();
                putValue(values, value.getClass(), newKey, value);
            });
        } else if (fieldType.isArray()) {
            Object[] arrayValue = (Object[]) fieldValue;
            putArray(values, fieldName, Observable.from(arrayValue));
        } else if (ConvertUtil.isList(fieldType)) {
            List<?> listValue = (List) fieldValue;
            putArray(values, fieldName, Observable.from(listValue));
        } else if (Date.class.isAssignableFrom(fieldType)) {
            Date date = (Date) fieldValue;
            values.put(fieldName, String.valueOf(date.getTime()));
        } else {
            values.put(fieldName, String.valueOf(fieldValue));
        }
    }

    private void getValue(Map<String, String> values, Object bean, Field field) {
        if (ConvertUtil.isMap(field.getType())) {
            Type genericType = getGenericType(field);
            Object mapValue = getMap(values, field.getName(), (Class<?>) genericType);
            ClassUtil.setProperty(bean, field, mapValue);
        } else if (field.getType().isArray() || ConvertUtil.isList(field.getType())) {
            Object arrayValue = getArray(values, field);
            ClassUtil.setProperty(bean, field, arrayValue);
        } else {
            String fieldValue = values.get(field.getName());
            if (fieldValue != null) {
                Object baseValue = toBase(fieldValue, field.getType());
                ClassUtil.setProperty(bean, field, baseValue);
            }
        }
    }

    private static Map<String, Object> getMap(Map<String, String> values, String fieldName, Class<?> fieldType) {
        Map<String, Object> map = Maps.newHashMap();
        String keyPrefix = String.format("%s:", fieldName);
        for (Map.Entry<String, String> entry : values.entrySet()) {
            if (!entry.getKey().startsWith(keyPrefix))  continue;
            String remain = entry.getKey().substring(keyPrefix.length(), entry.getKey().length());
            String[] path = remain.split(":");
            putMapValue(map, path, 0, fieldType, entry.getValue());
        }
        return map;
    }

    private static void putMapValue(Map<String, Object> map, String[] path, int index, Class<?> type, String value) {
        String p = path[index];
        if (index < (path.length - 1)) {
            //noinspection unchecked
            Map<String, Object> nest = (Map<String, Object>) map.get(p);
            if (nest == null) {
                nest = Maps.newHashMap();
                map.put(p, nest);
            }
            putMapValue(nest, path, index + 1, type, value);
            return;
        }
        Object mapValue = toBase(value, type);
        map.put(p, mapValue);
    }

    private void putArray(Map<String, String> values, String fieldName, Observable<Object> observable) {
        Integer length = observable.reduce(0, (index, o) -> {
            String fieldKey = String.format("%s_%s", fieldName, index);
            values.put(fieldKey, String.valueOf(o));
            return index + 1;
        }).toBlocking().first();
        String arrayLengthName = String.format("%s_Length", fieldName);
        values.put(arrayLengthName, String.valueOf(length));
    }

    private Object getArray(Map<String, String> values, Field field) {
        String fieldName = field.getName();
        String lengthValue = values.get(String.format("%s_Length", fieldName));
        if (StringUtils.isBlank(lengthValue)) {
            return null;
        }
        int length = Integer.parseInt(lengthValue);
        String[] strings = new String[length];
        Observable.range(0, length).reduce(0, (index, v) -> {
            strings[v] = values.get(String.format("%s_%s", fieldName, v));
            return index + 1;
        }).subscribe();
        return field.getType().isArray() ?
                ConvertUtil.toArray(strings, field.getType()) :
                ConvertUtil.toList(strings, (Class<?>) getGenericType(field));
    }

//    public static void main(String[] args) {
//        Map<String, String> values = Maps.newHashMap();
////        values.put("params:test:pt", "1");
//        values.put("params:test", "2");
//        values.put("params:c:pt:a", "3");
//        values.put("params:abc", "10");
//        values.put("params", "0");
//        Map<String, Object> map = getMap(values, "params", Integer.class);
//        System.out.println(map);
////        values.put("test", new String[]{"a", "b", "c"});
////        Object v = values.get("test");
////        Object[] as = (Object[]) v;
////        int value = Observable.from(as).reduce(0, (index, o) -> {
////            System.out.println(index + " " + o);
////            return index + 1;
////        }).toBlocking().first();
////        System.out.println(value);
////        List<String> s = Lists.newArrayList("a", "b", "c");
////        boolean list = /*s.getClass().isAssignableFrom(Collection.class);*/Collection.class.isInstance(s);
////        System.out.println(list);
////
////        Type type = values.getClass();
////        System.out.println(type.getTypeName());
//    }
}
