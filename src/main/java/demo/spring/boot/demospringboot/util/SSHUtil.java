package demo.spring.boot.demospringboot.util;

import com.jcraft.jsch.JSchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import demo.spring.boot.demospringboot.command.options.stream.Images;
import demo.spring.boot.demospringboot.util.help.SSHHelper;
import demo.spring.boot.demospringboot.util.help.SSHResInfo;

/**
 * 2018/4/15    Created by   chao
 */
@Component
public class SSHUtil {


    private static Logger LOGGER = LoggerFactory.getLogger(SSHUtil.class);

    @Value(value = "${ssh.host}")
    private String host;

    @Value(value = "${ssh.port}")
    private Integer port;

    @Value(value = "${ssh.user}")
    private String user;

    @Value(value = "${ssh.password}")
    private String password;

    private SSHHelper helper;

    public SSHResInfo execute(String command) {

        LOGGER.info("cmd:{}", command);

        SSHResInfo result = null;
        try {
            if (null == helper) {
                helper = new SSHHelper(host, port, user, password);
            }
            result = helper.sendCmd(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 验证是否能够连接主机
     */
    public static Boolean valid(String hostIp, Integer port, String account, String password) {
        LOGGER.info("验证主机登陆:hostIp:{}, port:{}, account:{}, password:{}", hostIp, port, account, password);
        SSHHelper sshHelper;
        try {
            sshHelper = new SSHHelper(hostIp, port, account, password);
            if (null != sshHelper) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("验证主机登陆异常：{}", e.getMessage(), e);
            LOGGER.error("验证主机登陆异常:hostIp:{}, port:{}, account:{}, password:{}", hostIp, port, account, password);
            return false;
        }

    }
}
