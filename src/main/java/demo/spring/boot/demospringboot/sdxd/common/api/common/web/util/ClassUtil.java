package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.RestRequest;

import org.jooq.lambda.function.Function3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
 * 16/12/26     melvin                 Created
 */
public class ClassUtil {

    private static final Logger log = LoggerFactory.getLogger(ClassUtil.class);

    public static <T> void access(Object bean, Predicate<Class<?>> classStopper, Function3<Object, Field, T, Boolean> function) {
        access(bean, null, classStopper, null, function);
    }

    public static <T> void access(Object bean, T value, Predicate<Class<?>> classStopper, Predicate<Field> fieldStopper, Function3<Object, Field, T, Boolean> function) {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        access(clazz, fields, bean, value, classStopper, fieldStopper, function);
    }

    public static <T> void access(Class<?> clazz, Field[] fields, Object bean, T value, Predicate<Class<?>> classStopper, Function3<Object, Field, T, Boolean> function) {
        access(clazz, fields, bean, value, classStopper, null, function);
    }

    public static <T> void access(Class<?> clazz, Field[] fields, Object bean, T value, Predicate<Class<?>> classStopper, Predicate<Field> fieldStopper, Function3<Object, Field, T, Boolean> function) {
        if (classStopper != null && classStopper.test(clazz)) {
            return;
        }
        for (Field field : fields) {
            if (fieldStopper != null && fieldStopper.test(field)) {
                continue;
            }

            if (function != null) {
                Boolean complete = function.apply(bean, field, value);
                if (complete) {
                    break;
                }
            }
        }

        Class<?> superClass = clazz.getSuperclass();
        access(superClass, superClass.getDeclaredFields(), bean, value, classStopper, fieldStopper, function);
    }

    public static Object getProperty(Object bean, Field field) {
        try {
            field.setAccessible(true);
            return field.get(bean);
        } catch (IllegalAccessException e) {
            log.warn("Failed to get property from bean {}, field: {}, error: {}", bean.getClass().getName(), field.getName(), e.getMessage());
        }
        return null;
    }

    public static void setProperty(Object bean, Field field, Object value) {
        if (value == null) {
            return;
        }
        Class<?> fieldType = field.getType();
        Object fieldValue = null;
        if (String.class.isInstance(value) && fieldType == String.class) {
            fieldValue = value;
        } else if (fieldType.isEnum()) {
            //noinspection unchecked
            fieldValue = Enum.valueOf((Class<Enum>) fieldType, value.toString());
        } else if (fieldType.isArray()) {
            String[] values = String[].class.cast(value);
            fieldValue = ConvertUtil.toArray(values, fieldType);
        } else if (ConvertUtil.isList(fieldType)) {
            String[] values = String[].class.cast(value);
            fieldValue = ConvertUtil.toList(values, (Class<?>) ConvertUtil.getGenericType(field));
        } else if (ConvertUtil.isMap(fieldType)) {
            fieldValue = value;
        } else {
            String text = value.toString();
            fieldValue = ConvertUtil.toBase(text, fieldType);
        }
        if (fieldValue == null) {
            return;
        }
        try {
            field.setAccessible(true);
            field.set(bean, fieldValue);
        } catch (IllegalAccessException e) {
            log.warn("Failed to set property to bean {}, field: {}, error: {}", bean.getClass().getName(), field.getName(), e.getMessage());
        }
    }

    public static boolean isArrayParameter(Parameter parameter) {
        return parameter.getType().isArray();
    }

    public static boolean isListParameter(Parameter parameter) {
        return List.class.isAssignableFrom(parameter.getType());
    }

    public static boolean isArrayField(Field field) {
        return field.getType().isArray();
    }

    public static boolean isListField(Field field) {
        return List.class.isAssignableFrom(field.getType());
    }

    public static Object newInstance(Class<?> type) {
        if (type.isAssignableFrom(RestRequest.class)) {
            return new RestRequest();
        } else {
            try {
                return type.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("Init parameter bean failed.", e);
                return null;
            }
        }
    }

    public static List<Field> getPrivateFieldList(Class clazz){
        Field[] fields = clazz.getDeclaredFields();
        return Arrays.stream(fields).filter(field -> field.getModifiers() == Modifier.PRIVATE).collect(Collectors.toList());
    }

    public static void setField(Object bean, Field field, String value) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), bean.getClass());
            Method method = pd.getWriteMethod();
            method.invoke(bean, value);
        } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            log.error("Set field value failed.", e);
        }


    }

    public static Object getField(Object bean, Field field) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), bean.getClass());
            Method method = pd.getReadMethod();
            return method.invoke(bean);
        }
        catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            log.error("Get field value failed.", e);
            return null;
        }
    }

    public static <T>List<Map<String, Object>> getOptions(String clazzName) {
        try {
            List<Map<String, Object>> optionList = new ArrayList<>();
            Class<?> clazz = Class.forName(clazzName);
            T[] enumConstants = (T[])clazz.getEnumConstants();
            List<Field> fieldList = getPrivateFieldList(clazz);
            for(T t : enumConstants){
                Map<String, Object> map = new HashMap<>();
                for(Field filed : fieldList){
                    map.put(filed.getName(), getField(t, filed));
                }
                optionList.add(map);
            }
            return optionList;
        } catch (ClassNotFoundException e) {
            log.error("Class not found.", e);
            return null;
        }
    }
}
