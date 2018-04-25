package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.Subject;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.token.TokenConfiguration;

import org.apache.commons.lang3.StringUtils;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/1/22     melvin                 Created
 */
public class RestContext {

    private static final Logger log = LoggerFactory.getLogger(RestContext.class);

    private static final Set<Tuple2<String, String>> LOGGING_HEADERS = Sets.newHashSet(
            new Tuple2<>("source", "x-source"),
            new Tuple2<>("channel", "x-channel"),
            new Tuple2<>("app_version", "x-app-version"),
            new Tuple2<>("device_os_type", "x-device-os-type"),
            new Tuple2<>("network_type", "x-network-type"),
            new Tuple2<>("mac_address", "x-mac-address"),
            new Tuple2<>("device_id", "x-device-id"),
            new Tuple2<>("browser", "x-browser"),
            new Tuple2<>("app_name", "x-app-name"),
            new Tuple2<>("device_model", "x-device-model"));

    private static final ThreadLocal<Subject> SUBJECT_CACHE = new ThreadLocal<>();

    private static final ThreadLocal<String> CONTEXT_ID_CACHE = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, String>> LOGGING_HEADERS_CACHE = new ThreadLocal<>();
    private static final ThreadLocal<Date> REQUEST_ENTRY_CACHE = new ThreadLocal<>();
    private static final ThreadLocal<String> PRINCIPAL_CACHE = new ThreadLocal<>();
    private static final ThreadLocal<String> REMOTE_ADDRESS_CACHE = new ThreadLocal<>();

    private static final ThreadLocal<Tuple3<Boolean, Boolean, Boolean>> LOGGING_CACHE = new ThreadLocal<>();

    private static final Map<String, Tuple2<Boolean, Boolean>> LOGGING_APIS = Maps.newHashMap();

    private static final Map<String, Set<RestParameter>> API_PARAMS = Maps.newHashMap();

    private static final Map<String, Tuple2<Predicate<?>, String>> THROTTLING_RULES = Maps.newHashMap();

    private static PackageURLRequestMappingHandlerMapping PACKAGE_MAPPING;

    private static TokenConfiguration GLOBAL_TOKEN_CONFIGURATION;

    private static String GRAPHQL_API;

    private static String WEB_ROOT;

    private static String HIDE_BODY_METHOD;

    public static Subject getSubject() {
        return SUBJECT_CACHE.get();
    }

    public static void setSubject(Subject value) {
        SUBJECT_CACHE.set(value);
    }

    public static boolean isLoggingHeader(String headerName) {
        return LOGGING_HEADERS.stream().anyMatch(tuple -> tuple.v1().equals(headerName) || tuple.v2().equals(headerName));
    }

    public static boolean isHideAll(HttpServletRequest request) {
        return StringUtils.isNotBlank(RestContext.getHideBodyMethod()) &&
                RestContext.getHideBodyMethod().equals(request.getMethod().toUpperCase());
    }

    public static String getHideBodyMethod() {
        return HIDE_BODY_METHOD;
    }

    public static void setHideBodyMethod(String hideBodyMethod) {
        HIDE_BODY_METHOD = hideBodyMethod;
    }

    public static String getWebRoot() {
        return WEB_ROOT;
    }

    public static void setWebRoot(String webRoot) {
        WEB_ROOT = webRoot;
    }

    public static String getGraphqlApi() {
        return GRAPHQL_API;
    }

    public static void setGraphqlApi(String graphqlApi) {
        GRAPHQL_API = graphqlApi;
    }

    public static TokenConfiguration getGlobalTokenConfiguration() {
        return GLOBAL_TOKEN_CONFIGURATION;
    }

    public static void setGlobalTokenConfiguration(TokenConfiguration globalTokenConfiguration) {
        GLOBAL_TOKEN_CONFIGURATION = globalTokenConfiguration;
    }

    public static void setContextId(String id, Date date) {
        CONTEXT_ID_CACHE.set(id);
        REQUEST_ENTRY_CACHE.set(date);
    }

    public static String getContextId() {
        String id = CONTEXT_ID_CACHE.get();
        return id == null ? "" : id;
    }

    public static void setLoggingHeaders(Map<String, String> headers) {
        LOGGING_HEADERS_CACHE.set(headers);
    }

    public static Map<String, String> getLoggingHeaders() {
        return LOGGING_HEADERS_CACHE.get();
    }

    public static Date getRequestEntry() {
        return REQUEST_ENTRY_CACHE.get();
    }

    public static void setPrincipal(String principal) {
        PRINCIPAL_CACHE.set(principal);
    }

    public static String getPrincipal() {
        String principal = PRINCIPAL_CACHE.get();
        return principal == null ? "" : principal;
    }

    public static void setRemoteAddress(String remoteAddress) {
        REMOTE_ADDRESS_CACHE.set(remoteAddress);
    }

    public static String getRemoteAddress() {
        String remoteAddress = REMOTE_ADDRESS_CACHE.get();
        return remoteAddress == null ? "" : remoteAddress;
    }

    public static void setLogging(Tuple3<Boolean, Boolean, Boolean> logging) {
        LOGGING_CACHE.set(logging);
    }

    public static Tuple3<Boolean, Boolean, Boolean> getLogging() {
        return LOGGING_CACHE.get();
    }

    public static boolean isHideRequestBody() {
        Tuple3<Boolean, Boolean, Boolean> hide = LOGGING_CACHE.get();
        return hide == null ? false : hide.v1();
    }

    public static boolean isHideResponseBody() {
        Tuple3<Boolean, Boolean, Boolean> hide = LOGGING_CACHE.get();
        return hide == null ? false : hide.v2();
    }

    public static boolean isLogging() {
        Tuple3<Boolean, Boolean, Boolean> hide = LOGGING_CACHE.get();
        return hide != null && !hide.v3();
    }

    public static boolean isMatchPackageBase(Class<?> handlerType) {
        return PACKAGE_MAPPING != null && PACKAGE_MAPPING.isMatchPackageBase(handlerType);
    }

    public static void addRule(String name, Predicate<?> predicate, String message) {
        THROTTLING_RULES.put(name, new Tuple2<>(predicate, message));
    }

    public static Tuple2<Predicate<?>, String> getRule(String name) {
        return THROTTLING_RULES.get(name);
    }

    public static Set<RestParameter> getParameters(String path) {
        return API_PARAMS.entrySet().stream().filter(entry -> {
            String key = entry.getKey();
            return matchPath(key, path);
        }).map(Map.Entry::getValue).findFirst().orElse(null);
    }

    public static Tuple2<Boolean, Boolean> getLoggingApi(String path) {
        return LOGGING_APIS.entrySet().stream().filter(entry -> {
            String key = entry.getKey();
            return matchPath(key, path);
        }).map(Map.Entry::getValue).findFirst().orElse(null);
    }

    static void setMapping(PackageURLRequestMappingHandlerMapping mapping) {
        PACKAGE_MAPPING = mapping;
    }

    static void addParameters(String path, Set<RestParameter> parameters) {
        if (StringUtils.isBlank(path)) {
            return;
        }
        API_PARAMS.put(path, parameters);
        log.debug("Cache api params, {} -> {}", path, parameters);
    }

    static void addLoggingApi(String path, boolean request, boolean response) {
        if (StringUtils.isBlank(path)) {
            return;
        }
        LOGGING_APIS.put(path, new Tuple2<>(request, response));
        log.debug("Cache logging api: {}", path);
    }

    private static boolean matchPath(String path1, String path2) {
        if (StringUtils.isBlank(path2) && StringUtils.isNotBlank(path1)) {
            return false;
        }
        if (StringUtils.isBlank(path1) && StringUtils.isNotBlank(path2)) {
            return false;
        }
        if (path1.equals(path2)) {
            return true;
        }
        String[] p1s = path1.split("/");
        String[] p2s = path2.split("/");
        if (p1s.length != p2s.length) {
            return false;
        }
        boolean allMatch = true;
        for (int i = 0; i < p1s.length; i ++) {
            if (p1s[i].startsWith("{")) {
                if (p1s[i].contains(":")) {
                    String[] p1ss = p1s[i].split(":");
                    String[] p2ss = p2s[i].split(":");
                    if (p1ss.length != p2ss.length || !p1ss[1].equals(p2ss[1])) {
                        allMatch = false;
                    }
                }
                continue;
            }
            if (!p1s[i].equals(p2s[i])) {
                allMatch = false;
            }
        }
        return allMatch;
    }
}
