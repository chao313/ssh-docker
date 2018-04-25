package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

import com.google.common.collect.Lists;

import org.jooq.lambda.tuple.Tuple2;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.BiFunction;

import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;

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
 * 17/2/4     melvin                 Created
 */
public class RestUtil {

    public static BiFunction<String, String, String> PATH_CONCAT = (s, ss) -> {
        String front = s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
        String back = ss.startsWith("/") ? ss.substring(1, ss.length()) : ss;
        return front.concat(back.length() == 0 ? "" : "/").concat(back);
    };

    public static String[] productPath(String[] s1, String[] s2) {
        List<String> result = product(s1, s2, PATH_CONCAT);
        return result.toArray(new String[result.size()]);
    }

    public static List<Tuple2<String, String>> productMethod(String[] s1, String[] s2) {
        return product(s1, s2, (s, ss) -> new Tuple2<>(s.concat(":").concat(ss), ss));
    }

    private static <T> List<T> product(String[] s1, String[] s2, BiFunction<String, String, T> function) {
        List<T> result = Lists.newArrayList();
        for (String s : s1) {
            for (String ss : s2) {
                T value = function.apply(s, ss);
                result.add(value);
            }
        }
        return result;
    }

    public static boolean notApiMethod(Method method) {
        return isStatic(method.getModifiers()) ||
                !isPublic(method.getModifiers()) ||
                "toString".equals(method.getName());
    }
}
