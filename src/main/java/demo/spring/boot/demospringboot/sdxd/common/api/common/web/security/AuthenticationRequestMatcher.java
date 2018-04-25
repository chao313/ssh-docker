package demo.spring.boot.demospringboot.sdxd.common.api.common.web.security;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.auth.AuthenticationApiHolder;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.security
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/4/20     melvin                 Created
 */
public class AuthenticationRequestMatcher implements RequestMatcher {

    private static final String MATCHED_PATH_KEY = "matched-path";

    private Set<AntPathRequestMatcher> matchers = Sets.newHashSet();
    private Map<String, AuthenticationApiHolder> apiMap = Maps.newHashMap();

//    private Map<String, IAuthenticationService<JwtToken>> serviceMap = Maps.newHashMap();
//
//    public AuthenticationRequestMatcher(IAuthenticationService<JwtToken>[] services) {
//        if (services != null) {
//            for (IAuthenticationService<JwtToken> service : services) {
//                matchers.add(new AntPathRequestMatcher(service.getSignInPath(), "POST"));
//                serviceMap.put(service.getSignInPath().concat(":").concat("POST"), service);
//            }
//        }
//    }
//
//    public IAuthenticationService<JwtToken> getMatchedPathService(HttpServletRequest request) {
//        String key = getMatchedPath(request);
//        return serviceMap.get(key);
//    }


    public void addApi(AuthenticationApiHolder api) {
        if (api == null || api.invalid()) {
            return;
        }
        String path = api.getPath();
        matchers.add(new AntPathRequestMatcher(path, "POST"));
        apiMap.put(path.concat(":").concat("POST"), api);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        long count = matchers.stream().
                filter(matcher -> matcher.matches(request)).
                map(matcher -> {
                    String key = matcher.getPattern().concat(":").concat("POST");
                    request.setAttribute(MATCHED_PATH_KEY, key);
                    return key;
                }).count();
        return count > 0;
    }

    AuthenticationApiHolder getMatchedPathApi(HttpServletRequest request) {
        String key = getMatchedPath(request);
        return apiMap.get(key);
    }

    private static String getMatchedPath(HttpServletRequest request) {
        Object value = request.getAttribute(MATCHED_PATH_KEY);
        return value == null ? null : value.toString();
    }
}
