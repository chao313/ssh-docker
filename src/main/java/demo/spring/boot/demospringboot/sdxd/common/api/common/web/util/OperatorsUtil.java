package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

import com.google.common.collect.Sets;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import static com.google.common.collect.ImmutableMap.of;

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
 * 16/11/15     melvin                 Created
 */
public class OperatorsUtil {

    /**
     * 移动: 1340;1341;1342;1343;1344;1345;1346;1347;1348;135;136;137;138;139;147;150;151;152;157;158;159;178;182;183;184;187;188;
     * 联通: 130;131;132;145;155;156;175;176;185;186;
     * 电信: 133;153;177;180;181;189;
     * 全球星: 1349;
     * 虚拟运营商: 170;171
     */

    private static final Set<String> CMCC = Sets.newHashSet(
            "1340", "1341", "1342", "1343", "1344", "1345", "1346", "1347", "1348",
            "135", "136", "137", "138", "139", "147", "150", "151", "152", "157",
            "158", "159", "178", "182", "183", "184", "187", "188");

    private static final Set<String> CUCC = Sets.newHashSet("130", "131", "132", "145", "155", "156", "175", "176", "185", "186");

    private static final Set<String> CTCC = Sets.newHashSet("133", "153", "177", "180", "181", "189");

    private static final Map<Predicate<String>, String> OPERATOR_PREDICATES = of(
            (n) -> inOperators(n, CMCC), "中国移动",
            (n) -> inOperators(n, CUCC), "中国联通",
            (n) -> inOperators(n, CTCC), "中国电信"
    );

    private static final Map<Predicate<String>, String> CONTACT_PREDICATES = of(
            (n) -> inOperators(n, CMCC), "10086",
            (n) -> inOperators(n, CUCC), "10010",
            (n) -> inOperators(n, CTCC), "10001"
    );

    public static String getOperatorsContact(String number) {
        return getOperators(number, CONTACT_PREDICATES);
    }

    public static String getOperatorsName(String number) {
        return getOperators(number, OPERATOR_PREDICATES);
    }

    private static String getOperators(String number, Map<Predicate<String>, String> predicates) {
        return predicates.entrySet().stream().
                filter(entry -> entry.getKey().test(number)).
                map(Map.Entry::getValue).
                findFirst().orElse(null);
    }

    private static boolean inOperators(String number, Set<String> operators) {
        if (StringUtils.isBlank(number)) {
            return false;
        }
        String prefix = number.substring(0, 3);
        if ("134".equals(prefix)) {
            prefix = number.substring(0, 4);
        }

        return operators.contains(prefix);
    }
}
