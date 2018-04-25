/*
 * Copyright @ 2015QIANLONG.
 * All right reserved.
 */

package demo.spring.boot.demospringboot.sdxd.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * Description:校验器：利用正则表达式校验邮箱、手机号等
 *
 * @author qiuyangjun
 * @PackageName:com.mobanker.tkj.cs.core.utils
 * @ClassName:Validator
 * @date 2015年11月18日 下午2:51:45
 */
public class ValidatorRegexTmplUtils {
    /**
     * 正则表达式：验证用户名
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";

    /**
     * 正则表达式：验证密码
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((13[0-9])|(14[5,7,9])|(15[^4,\\D])|(17[0,1,3,5-8])|(18[0-9]))\\d{8}$";
    
    /**
     * 正则表达式：验证手机号 +86  
     */
    public static final String REGEX_MOBILE_86="^((\\+?86)|(\\+86))?((13[0-9])|(14[5,7,9])|(15[^4,\\D])|(17[0,1,3,5-8])|(18[0-9]))\\d{8}$";

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

//    /**
//     * 正则表达式：验证身份证
//     */
//    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

    /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 正则表达式：验证IP地址
     */
    public static final String REGEX_IP_ADDR = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";

    /**
     * 正则表达式:验证是否是内网IP A类 10.0.0.0--10.255.255.255 B类 172.16.0.0--172.31.255.255
     * C类 192.168.0.0--192.168.255.255
     */
    public static final String REGEX_INNER_IP_ADDR = "^((192\\.168|172\\.([1][6-9]|[2]\\d|3[01]))(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){2}|10(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){3})$";

    /**
     * 校验用户名
     *
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUsername(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }

    /**
     * 校验密码
     *
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 校验手机号
     *
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 校验邮箱
     *
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 校验汉字
     *
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

//    /**
//     * 校验身份证
//     *
//     * @return 校验通过返回true，否则返回false
//     */
//    public static boolean isIDCard(String idCard) {
//        return Pattern.matches(REGEX_ID_CARD, idCard);
//    }

    /**
     * 校验URL
     *
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(REGEX_URL, url);
    }

    /**
     * 校验IP地址
     */
    public static boolean isIPAddr(String ipAddr) {
        return Pattern.matches(REGEX_IP_ADDR, ipAddr);
    }

    /**
     * Description:校验是否是内部IP(通过正则)
     *
     * @author qiuyangjun
     * @date 2015年12月3日 下午1:52:43
     */
    public static boolean isInnerIp(String ip) {
        return Pattern.matches(REGEX_INNER_IP_ADDR, ip);
    }

    /**
     * Description:校验是否是内部IP(位运算)
     *
     * @author qiuyangjun
     * @date 2015年12月3日 下午1:53:27
     */
    // public static boolean isInner(String ip) {
    // Long value = ip2Long(ip);
    // boolean flag = false;
    // for (Entry<Long, Long> entry : innerIpValue.entrySet()) {
    // if (entry.getKey() <= value && value <= entry.getValue()) {
    // flag = true;
    // }
    // }
    // return flag;
    // }

    // private static final Map<Long, Long> innerIpValue = new HashMap<Long,
    // Long>();

    // static {
    // // A类 10.0.0.0--10.255.255.255
    // // B类 172.16.0.0--172.31.255.255
    // // C类 192.168.0.0--192.168.255.255
    // // A:MIN,MAX
    // innerIpValue.put(ip2Long("10.0.0.0"), ip2Long("10.255.255.255"));
    // // B:MIN,MAX
    // innerIpValue.put(ip2Long("172.16.0.0"), ip2Long("172.31.255.255"));
    // // C:MIN,MAX
    // innerIpValue.put(ip2Long("192.168.0.0"), ip2Long("192.168.255.255"));
    // }
    public static boolean isInnerIp(String ip, String innerIps) {
        boolean flag = false;
        String[] strings = StringUtils.split(innerIps, ';');
        Map<Long, Long> map = new HashMap<Long, Long>();
        for (String string : strings) {
            String[] ips = StringUtils.split(string, ',');
            Long value1 = ip2Long(ips[0]);
            Long value2 = ip2Long(ips[1]);
            if (value1 < value2) {
                map.put(value1, value2);
            } else {
                map.put(value2, value1);
            }
        }
        Long value = ip2Long(ip);
        for (Entry<Long, Long> entry : map.entrySet()) {
            if (entry.getKey() <= value && value <= entry.getValue()) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * Description:将IP转换为数值
     *
     * @author qiuyangjun
     * @date 2015年12月3日 下午2:07:19
     */
    private static Long ip2Long(String ip) {
        String[] strings = StringUtils.split(ip, '.');
        Long result = 0l;
        for (String st : strings) {
            result = result << 8 | Long.parseLong(st);
        }
        return result;
    }

    public static void main(String[] args) {
        for (int i = 0; i <= 255; i++) {
            String ip = "10.255.255.";
            ip = ip + i;
            boolean inner = isInnerIp(ip);
            System.out.println(inner + "====================");
        }

    }
}
