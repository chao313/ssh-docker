package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

import com.google.common.base.CaseFormat;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.JwtAuthentication;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

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
 * 16/10/27     melvin                 Created
 */
public class HttpUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    private static final String UNKNOWN = "unknown";
    private static final String LOCAL_ADDRESS = "127.0.0.1";

    interface Header {
        String AUTHORIZATION = "Authorization";
        String AUTHENTICATION_TYPE_BASIC = "Basic";
        String X_AUTH_TOKEN = "X-AUTH-TOKEN";
        String WWW_Authenticate = "WWW-Authenticate";
        String X_FORWARDED_FOR = "X-Forwarded-For";
        String PROXY_CLIENT_IP = "Proxy-Client-IP";
        String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
        String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
        String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
    }

    public static String toSnakeCase(String s) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, s);
    }

    public static String toCamelCase(String s) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, s);
    }

    public static JwtAuthentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (JwtAuthentication.class.isInstance(authentication)) {
            return JwtAuthentication.class.cast(authentication);
        }
        return null;
    }

    public static String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }

    public static String getRemoteIp(HttpServletRequest request) {
        String ip = null;
        int tryCount = 1;

        while (!isIpFound(ip) && tryCount <= 6) {
            switch (tryCount) {
                case 1:
                    ip = request.getHeader(Header.X_FORWARDED_FOR);
                    if (StringUtils.isNotBlank(ip)) {
                        String[] ips = ip.split(",");
                        for (String value : ips) {
                            if (!UNKNOWN.equals(value)) {
                                ip = value;
                                break;
                            }
                        }
                    }
                    break;
                case 2:
                    ip = request.getHeader(Header.PROXY_CLIENT_IP);
                    break;
                case 3:
                    ip = request.getHeader(Header.WL_PROXY_CLIENT_IP);
                    break;
                case 4:
                    ip = request.getHeader(Header.HTTP_CLIENT_IP);
                    break;
                case 5:
                    ip = request.getHeader(Header.HTTP_X_FORWARDED_FOR);
                    break;
                default:
                    ip = request.getRemoteAddr();
            }

            tryCount++;
        }

        if (isLoopbackAddress(ip)) {
            ip = LOCAL_ADDRESS;
        }

        log.trace("Get ip from request, ip: {}", ip);
        return ip;
    }

    private static boolean isIpFound(String ip) {
        return ip != null && ip.length() > 0 && !UNKNOWN.equalsIgnoreCase(ip);
    }

    private static boolean isLoopbackAddress(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.isLoopbackAddress();
        } catch (UnknownHostException e) {
            return false;
        }
    }
}
