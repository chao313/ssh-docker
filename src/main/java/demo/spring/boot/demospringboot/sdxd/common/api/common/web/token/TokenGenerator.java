package demo.spring.boot.demospringboot.sdxd.common.api.common.web.token;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.security.Subject;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.JwtToken;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.util.Calendar;
import java.util.Map;
import java.util.stream.Collectors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode.AuthenticationError.INVALID_TOKEN;
import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode.AuthenticationError.TOKEN_EXPIRED;

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
 * 16/4/18     melvin                 Created
 */
public class TokenGenerator {

    private static final Logger log = LoggerFactory.getLogger(TokenGenerator.class);

    private static final TokenValidation FALSE = new TokenValidation(null, false, null, null, 0, false, null);

    public static JwtToken createAccessToken(TokenConfiguration configuration, String subject) {
        return createToken(configuration, new Subject(subject, true, null, null), true);
    }

    public static JwtToken createToken(TokenConfiguration configuration, Subject subject) {
        return createToken(configuration, subject, false);
    }

    private static JwtToken createToken(TokenConfiguration configuration, Subject subject, boolean access) {
        long expireTime = access ? configuration.getAccessExpireTime() : configuration.getExpireTime();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, (int) expireTime);

        Key key = configuration.getKey();
        if (key == null)    return null;

        boolean accessToken = subject.isAccess();
        JwtBuilder builder = Jwts.builder();
        Map<String, Object> params = subject.getTokenRequiredParams();
        params.forEach(builder::claim);
        String token = builder.
                setClaims(subject.getTokenRequiredParams()).
                setIssuer(configuration.getIssuer()).
                setSubject(subject.getValue()).
                claim("bornAt", System.currentTimeMillis()).
//                setExpiration(calendar.getTime()).
                claim("expiresIn", expireTime).
                claim("accessToken", accessToken).
                signWith(SignatureAlgorithm.HS256, key).
                compact();

        return new JwtToken(token, expireTime);
    }

    public static TokenValidation isValid(TokenConfiguration configuration, String token) {
        if (StringUtils.isBlank(token)) return FALSE;
        Key key = configuration.getKey();
        if (key == null)    return FALSE;
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token.trim());
            String subjectValue = claimsJws.getBody().getSubject();
            String audience = claimsJws.getBody().getAudience();
            long expiresIn = Long.parseLong(String.valueOf(claimsJws.getBody().get("expiresIn")));
            Object access = claimsJws.getBody().get("accessToken");
            boolean accessToken = access == null ? false : Boolean.class.cast(access);
            Map<String, Object> claims = claimsJws.getBody().entrySet().stream().
                    collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            Subject subject = new Subject(subjectValue, accessToken, claims, null);
            return new TokenValidation(token, true, subject, audience, expiresIn, accessToken, null);
        } catch (ExpiredJwtException e) {
            return new TokenValidation(token, false, null, null, 0, false, TOKEN_EXPIRED);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            return new TokenValidation(token, false, null, null, 0, false, INVALID_TOKEN);
        } catch (Exception e) {
            log.warn("Invalid token [{}], message: {}", token, e.getMessage());
            return FALSE;
        }
    }

    public static void main(String[] args) {
        TokenConfiguration configuration = new TokenConfiguration(null, "/keys/origin.key", true, "mapi.daxiaodai.com", 10000L, 10000L);
//        TokenValidation validation = isValid(configuration, token);
//        System.out.println(validation);
        JwtToken token = createToken(configuration, new Subject("14670"));
        System.out.println(token.getToken());
    }
}
