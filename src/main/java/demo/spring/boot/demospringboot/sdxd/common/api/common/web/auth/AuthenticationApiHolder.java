package demo.spring.boot.demospringboot.sdxd.common.api.common.web.auth;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace.HttpBodyCachingRequestWrapper;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.BiFunctionE;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.ClassUtil;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.JsonUtil;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.RequestUtil;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ProcessBizException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.MapUtil.$;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.auth
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/4/27     melvin                 Created
 */
public class AuthenticationApiHolder {

    private static final Map<Class<? extends Annotation>, BiFunctionE<HttpServletRequest, Class<? extends ICredential>, ICredential, ProcessBizException>> FUNCTIONS = $(
            BeanParam.class, (request, credentialType) -> {
                ICredential credential = (ICredential) ClassUtil.newInstance(credentialType);
                RequestUtil.bindValues(credential, request);
                return credential;
            },
            RequestBody.class, (request, credentialType) -> {
                try {
                    HttpBodyCachingRequestWrapper wrapper = new HttpBodyCachingRequestWrapper(request);
                    String body = wrapper.getBody();
                    return JsonUtil.fromJson(body, credentialType);
                } catch (IOException e) {
                    return null;
                }
            }
    );

    private String path;
    private Class<? extends Annotation> parameterType;
    private Class<? extends ICredential> credentialType;
    private IAuthenticationApi api;

    public AuthenticationApiHolder(String path, Class<? extends Annotation> parameterType, Class<? extends ICredential> credentialType, IAuthenticationApi api) {
        this.path = path;
        this.parameterType = parameterType;
        this.credentialType = credentialType;
        this.api = api;
    }

    public String getPath() {
        return path;
    }

    public IAuthenticationApi getApi() {
        return api;
    }

    public boolean invalid() {
        return StringUtils.isBlank(path) || credentialType == null || api == null;
    }

    public ICredential getCredential(HttpServletRequest request) throws ProcessBizException {
        BiFunctionE<HttpServletRequest, Class<? extends ICredential>, ICredential, ProcessBizException> function = FUNCTIONS.get(parameterType);
        return function == null ? null : function.apply(request, credentialType);
    }
}
