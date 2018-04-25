package demo.spring.boot.demospringboot.sdxd.common.api.common.web.auth;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.JwtAuthenticationFilter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

import java.lang.annotation.Annotation;

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
public class AuthenticationApiBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    private JwtAuthenticationFilter authenticationFilter;

    public AuthenticationApiBeanPostProcessor(JwtAuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Override
    public boolean postProcessAfterInstantiation(final Object bean, String beanName) throws BeansException {
        boolean result = super.postProcessAfterInstantiation(bean, beanName);
        Class<?> type = bean.getClass();
        if (!IAuthenticationApi.class.isAssignableFrom(type)) {
            return result;
        }
        Authentication authentication = type.getDeclaredAnnotation(Authentication.class);
        if (authentication == null) {
            return result;
        }

        String path = authentication.path();
        Class<? extends Annotation> parameterType = authentication.parameterType();
        Class<? extends ICredential> credentialType = authentication.credentialType();
        IAuthenticationApi api = IAuthenticationApi.class.cast(bean);
        AuthenticationApiHolder holder = new AuthenticationApiHolder(path, parameterType, credentialType, api);
        authenticationFilter.addApi(holder);

        return result;
    }
}
