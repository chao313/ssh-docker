package demo.spring.boot.demospringboot.sdxd.common.api.common.web.security;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.auth.ICredential;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.token.TokenCache;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.token.TokenConfiguration;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.token.TokenGenerator;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.FunctionE;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.JwtToken;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ProcessBizException;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.RestResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.JwtAuthentication.fail;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.JwtAuthentication.success;

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
@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private TokenCache tokenCache;

    @Autowired
    private TokenConfiguration tokenConfiguration;

    public boolean destroy(IAuthentication<JwtToken> authentication) {
        Subject subject = (Subject) authentication.getDetails();
        return tokenCache.remove(subject.getKey());
    }

    public JwtToken createAccessToken(String subject) {
        return createToken(subject, () -> TokenGenerator.createAccessToken(tokenConfiguration, subject));
    }

    IAuthentication<JwtToken> authenticate(
            ICredential credential,
            FunctionE<ICredential,
                    RestResponse<Subject>,
                    ProcessBizException> function) throws ProcessBizException {
        RestResponse<Subject> response = function.apply(credential);
        if (!response.isSuccessful()) {
            return fail(response.getCode(), response.getErrorMessage());
        }

        Subject subject = response.getContent();
//        JwtToken jwt = createToken(subject.getValue(), () -> TokenGenerator.createToken(subject, (long) (6 * 60 * 60)));
//        JwtToken jwt = createToken(subject.getValue(), () -> TokenGenerator.createToken(subject, (long) (7 * 24 * 60 * 60)));
        JwtToken jwt = createToken(subject.getKey(), () -> TokenGenerator.createToken(tokenConfiguration, subject));
        jwt = jwt.merge(subject.getParams());

        log.debug(
                "Authenticate complete with credential: {}, result: {} - {}",
                credential, response.getCode(), response.getErrorMessage());
        return success(subject, jwt, false);
    }

    private JwtToken createToken(String subject, Supplier<JwtToken> supplier) {
        JwtToken jwt = supplier.get();
        tokenCache.put(subject, jwt);
        return jwt;
    }
}
