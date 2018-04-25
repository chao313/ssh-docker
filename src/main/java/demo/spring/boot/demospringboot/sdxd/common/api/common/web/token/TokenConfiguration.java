package demo.spring.boot.demospringboot.sdxd.common.api.common.web.token;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.RestContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.token
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/7/7     melvin                 Created
 */
public class TokenConfiguration {

    public static long getDefaultTokenExpireTime() {
        return DEFAULT_TOKEN_EXPIRE_TIME;
    }

    private static final Logger log = LoggerFactory.getLogger(TokenConfiguration.class);

    private static final String DEFAULT_TOKEN_ISSUER = "api.sdxd.com";

    private static final long DEFAULT_TOKEN_EXPIRE_TIME = 30 * 60;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 7200;

    private static final String DEFAULT_KEY_PATH = "/keys/origin.key";

    private static final SignatureAlgorithm DEFAULT_KEY_ALGORITHM = SignatureAlgorithm.HS512;

    private String keyPathPrefixKey;

    private SignatureAlgorithm keyAlgorithm;
    private String keyPath;
    private boolean keyInClasspath;

    private String issuer;
    private Long expireTime;
    private Long accessExpireTime;

    public TokenConfiguration(
            String keyPathPrefixKey, String keyPath, Boolean keyInClasspath,
            String issuer, Long expireTime, Long accessExpireTime) {
        this.keyPath = keyPath;
        this.keyPathPrefixKey = keyPathPrefixKey;
        this.keyAlgorithm = DEFAULT_KEY_ALGORITHM;
        this.keyInClasspath = keyInClasspath == null ? true : keyInClasspath;
        this.issuer = issuer;
        this.expireTime = expireTime;
        this.accessExpireTime = accessExpireTime;

        RestContext.setGlobalTokenConfiguration(this);
    }

    public String getIssuer() {
        return StringUtils.isBlank(issuer) ? DEFAULT_TOKEN_ISSUER : issuer;
    }

    public long getExpireTime() {
        return expireTime == null || expireTime <= 0 ? DEFAULT_TOKEN_EXPIRE_TIME : expireTime;
    }

    public long getAccessExpireTime() {
        return accessExpireTime == null || accessExpireTime <= 0 ? ACCESS_TOKEN_EXPIRE_TIME : accessExpireTime;
    }

    public Key getKey() {
        Key key = readKey();
        if (key == null && !keyInClasspath) {
            return createKey(getKeyPath());
        }
        return key;
    }

    private Key readKey() {
        ObjectInputStream ois = null;
        String keyLocation = getKeyPath();
        try {
            InputStream in = getKeyInputStream(keyLocation);
            if (in == null) return null;
            ois = new ObjectInputStream(in);
            return (Key) ois.readObject();
        } catch (FileNotFoundException e) {
            log.error("Read token key error: ", e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            log.error("Read token key error.", e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    log.error("Close token key error.", e);
                }
            }
        }
        return null;
    }

    private Key createKey(String path) {
        Key key = MacProvider.generateKey(getKeyAlgorithm());
        ObjectOutputStream oos = null;
        try {
            File file = new File(path);
            File parent = file.getParentFile();
            if (!parent.exists()) {
                boolean created = parent.mkdirs();
                if (created) {
                    FileOutputStream fos = new FileOutputStream(path);
                    oos = new ObjectOutputStream(fos);
                    oos.writeObject(key);
                    return key;
                }
            }
        } catch (IOException e) {
            log.error("Write token key error.", e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    log.error("Close token key error.", e);
                }
            }
        }
        return null;
    }

    private String getKeyPath() {
        String path = StringUtils.isBlank(keyPath) ? DEFAULT_KEY_PATH : keyPath;
        if (keyPathPrefixKey != null && path.contains(keyPathPrefixKey)) {
            path = path.replace(keyPathPrefixKey, RestContext.getWebRoot());
        }
        return path;
    }

    private InputStream getKeyInputStream(String location) throws FileNotFoundException {
        return keyInClasspath ?
                TokenGenerator.class.getResourceAsStream(location) :
                new FileInputStream(location);
    }

    private SignatureAlgorithm getKeyAlgorithm() {
        return keyAlgorithm == null ? DEFAULT_KEY_ALGORITHM : keyAlgorithm;
    }
}
