package demo.spring.boot.demospringboot.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 生成加密salt及加密
 */
public class MD5Wrapper {

    public static String genSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[5];
        random.nextBytes(bytes);
        return bytes2Hex(bytes);
    }

    private static String bytes2Hex(byte[] src) {
        char[] res = new char[src.length * 2];
        final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        for (int i = 0, j = 0; i < src.length; i++) {
            res[j++] = hexDigits[src[i] >>> 4 & 0x0f];
            res[j++] = hexDigits[src[i] & 0x0f];
        }
        return new String(res);
    }

    public static String md5WithSalt(String src, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt.getBytes());
            return bytes2Hex(md.digest(src.getBytes()));
        } catch (NoSuchAlgorithmException e) {

        }
        return null;
    }
}
