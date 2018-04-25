package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.primitives.Booleans;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.MapUtil.$;

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
public class ConvertUtil {

    static final Set<Type> BASE_TYPES = ImmutableSet.of(
            byte.class,
            short.class,
            int.class,
            long.class,
            float.class,
            double.class,
            boolean.class,
            char.class,

            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            Boolean.class,
            Character.class,

            String.class,
            Date.class,
            BigDecimal.class,

            byte[].class,
            short[].class,
            int[].class,
            long[].class,
            float[].class,
            double[].class,
            boolean[].class,
            char[].class,

            Byte[].class,
            Short[].class,
            Integer[].class,
            Long[].class,
            Float[].class,
            Double[].class,
            Boolean[].class,
            Character[].class,

            String[].class,
            Date[].class,
            BigDecimal[].class
    );

    static final Map<Predicate<Class<?>>, Function<String[], Object>> ARRAY_TRANSFORM = $(
            type -> type == byte[].class, values -> Bytes.toArray(Stream.of(values).map(Byte::parseByte).collect(Collectors.toList())),
            type -> type == short[].class, values -> Shorts.toArray(Stream.of(values).map(Short::parseShort).collect(Collectors.toList())),
            type -> type == int[].class, values -> Ints.toArray(Stream.of(values).map(Integer::parseInt).collect(Collectors.toList())),
            type -> type == long[].class, values -> Longs.toArray(Stream.of(values).map(Long::parseLong).collect(Collectors.toList())),
            type -> type == float[].class, values -> Floats.toArray(Stream.of(values).map(Float::parseFloat).collect(Collectors.toList())),
            type -> type == double[].class, values -> Doubles.toArray(Stream.of(values).map(Double::parseDouble).collect(Collectors.toList())),
            type -> type == boolean[].class, values -> Booleans.toArray(Stream.of(values).map(Boolean::parseBoolean).collect(Collectors.toList())),
            type -> type == char[].class, values -> StringUtils.join(values, "").toCharArray(),

            type -> type == Byte[].class, values -> Stream.of(values).map(Byte::parseByte).collect(Collectors.toList()).toArray(new Byte[values.length]),
            type -> type == Short[].class, values -> Stream.of(values).map(Short::parseShort).collect(Collectors.toList()).toArray(new Short[values.length]),
            type -> type == Integer[].class, values -> Stream.of(values).map(Integer::parseInt).collect(Collectors.toList()).toArray(new Integer[values.length]),
            type -> type == Long[].class, values -> Stream.of(values).map(Long::parseLong).collect(Collectors.toList()).toArray(new Long[values.length]),
            type -> type == Float[].class, values -> Stream.of(values).map(Float::parseFloat).collect(Collectors.toList()).toArray(new Float[values.length]),
            type -> type == Double[].class, values -> Stream.of(values).map(Double::parseDouble).collect(Collectors.toList()).toArray(new Double[values.length]),
            type -> type == Boolean[].class, values -> Stream.of(values).map(Boolean::parseBoolean).collect(Collectors.toList()).toArray(new Boolean[values.length]),
            type -> type == Character[].class, values -> Stream.of(values).map(value -> value.charAt(0)).collect(Collectors.toList()).toArray(new Character[values.length]),

            type -> type == String[].class, values -> values,
            type -> type == Date[].class, values -> Stream.of(values).map(value -> new Date(Long.parseLong(value))).collect(Collectors.toList()).toArray(new Date[values.length]),
            type -> type == BigDecimal[].class, values -> Stream.of(values).map(BigDecimal::new).collect(Collectors.toList()).toArray(new BigDecimal[values.length])
    );

    static final Map<Predicate<Class<?>>, Function<String[], Object>> LIST_TRANSFORM = $(
            type -> type == byte.class || type == Byte.class, values -> Stream.of(values).map(Byte::parseByte).collect(Collectors.toList()),
            type -> type == short.class || type == Short.class, values -> Stream.of(values).map(Short::parseShort).collect(Collectors.toList()),
            type -> type == int.class || type == Integer.class, values -> Stream.of(values).map(Integer::parseInt).collect(Collectors.toList()),
            type -> type == long.class || type == Long.class, values -> Stream.of(values).map(Long::parseLong).collect(Collectors.toList()),
            type -> type == float.class || type == Float.class, values -> Stream.of(values).map(Float::parseFloat).collect(Collectors.toList()),
            type -> type == double.class || type == Double.class, values -> Stream.of(values).map(Double::parseDouble).collect(Collectors.toList()),
            type -> type == boolean.class || type == Boolean.class, values -> Stream.of(values).map(Boolean::parseBoolean).collect(Collectors.toList()),
            type -> type == char.class || type == Character.class, values -> StringUtils.join(values, "").toCharArray(),
            type -> type == String.class, Lists::newArrayList,
            type -> type == Date.class, values -> Stream.of(values).map(value -> new Date(Long.parseLong(value))).collect(Collectors.toList()),
            type -> type == BigDecimal.class, values -> Stream.of(values).map(BigDecimal::new).collect(Collectors.toList())
    );

    static final Map<Predicate<Class<?>>, Function<String, Object>> BASE_TYPE_TRANSFORM = $(
            type -> type == byte.class || type == Byte.class, Byte::parseByte,
            type -> type == short.class || type == Short.class, Short::parseShort,
            type -> type == int.class || type == Integer.class, Integer::parseInt,
            type -> type == long.class || type == Long.class, Long::parseLong,
            type -> type == float.class || type == Float.class, Float::parseFloat,
            type -> type == double.class || type == Double.class, Double::parseDouble,
            type -> type == boolean.class || type == Boolean.class, Boolean::parseBoolean,
            type -> type == char.class || type == Character.class, value -> StringUtils.isNotBlank(value) ? value.charAt(0) : null,
            type -> type == String.class, value -> value,
            type -> type == Date.class, value -> StringUtils.isNotBlank(value) ? new Date(Long.parseLong(value)) : null,
            type -> type == BigDecimal.class, value -> StringUtils.isNotBlank(value) ? new BigDecimal(value) : null
    );

    public static boolean isBaseType(Type type) {
        return BASE_TYPES.contains(type);
    }

    public static Object toArray(String[] values, Class<?> type) {
        return ARRAY_TRANSFORM.entrySet().stream().
                filter(entry -> entry.getKey().test(type)).
                map(entry -> entry.getValue().apply(values)).
                findFirst().orElse(null);
    }

    public static Object toList(String[] values, Class<?> genericType) {
        return LIST_TRANSFORM.entrySet().stream().
                filter(entry -> entry.getKey().test(genericType)).
                map(entry -> entry.getValue().apply(values)).
                findFirst().orElse(null);
    }

    public static Object toBase(String value, Class<?> type) {
        return BASE_TYPE_TRANSFORM.entrySet().stream().
                filter(entry -> entry.getKey().test(type)).
                map(entry -> entry.getValue().apply(value)).
                findFirst().orElse(null);
    }

    public static boolean isList(Class<?> type) {
        return List.class.isAssignableFrom(type);
    }

    public static boolean isMap(Class<?> type) {
        return Map.class.isAssignableFrom(type);
    }

    public static Type getGenericType(Field field) {
        Type type = null;
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            Type[] actualTypes = ParameterizedType.class.cast(genericType).getActualTypeArguments();
            Type actualType = actualTypes == null || actualTypes.length == 0 ? null : actualTypes[0];
            if (isMap(field.getType())) {
                actualType = actualTypes == null || actualTypes.length < 1 ? null : actualTypes[1];
            }
            if (actualType != null) {
                type = actualType;
            }
        }
        return type;
    }
}
